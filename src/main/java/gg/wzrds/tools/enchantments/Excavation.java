package gg.wzrds.tools.enchantments;

import gg.wzrds.tools.Tools;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Excavation {

    public static final ResourceKey<Enchantment> KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        Tools.id("excavation")
    );

    public static Holder<Enchantment> get(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(KEY);
    }

    private static final Set<UUID> currentExcavating = new HashSet<>();

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(
            (level, player, pos, state, blockEntity) -> {
                if (!(player instanceof ServerPlayer serverPlayer)) {
                    return;
                }

                ItemStack mainHandItemStack = player.getMainHandItem();
                Block originalBlock = state.getBlock();

                if (mainHandItemStack.isEmpty()) {
                    return;
                }

                int enchantLevel = EnchantmentHelper.getItemEnchantmentLevel(
                    get(level.registryAccess()),
                    mainHandItemStack
                );

                int[] blocksToBreak = switch (enchantLevel) {
                    case 1 -> new int[] {4, 6};
                    case 2 -> new int[] {2, 4, 6, 8};
                    case 3 -> new int[] {1, 2, 3, 4, 6, 7, 8, 9};
                    default -> new int[] {};
                };

                if (blocksToBreak.length == 0) {
                    return;
                }

                UUID playerUUID = player.getUUID();
                if (currentExcavating.contains(playerUUID)) {
                    return;
                }
                currentExcavating.add(playerUUID);

                try {
                    Direction direction = player.getDirection();
                    float pitch = serverPlayer.getXRot();

                    for (int blockToBreak : blocksToBreak) {
                        BlockPos targetPos = getTargetBlockPos(
                                pos,
                                direction,
                                pitch,
                                blockToBreak
                        );

                        BlockState targetState = level.getBlockState(targetPos);
                        Block targetBlock = targetState.getBlock();

                        if (targetBlock != originalBlock) {
                            continue;
                        }

                        serverPlayer.gameMode.destroyBlock(targetPos);
                    }
                } finally {
                    currentExcavating.remove(playerUUID);
                }
            }
        );
    }

    private static BlockPos getTargetBlockPos(
            BlockPos originalBlockPos,
            Direction direction,
            float pitch,
            int blockNumber
    ) {
        int offset = (blockNumber - 1) % 3 - 1;
        int yOffset = 1 - (blockNumber - 1) / 3;

        if (pitch < -45.0f || pitch > 45.0f) {
            return switch (direction) {
                case NORTH -> originalBlockPos.offset(offset, 0, yOffset);
                case SOUTH -> originalBlockPos.offset(-offset, 0, -yOffset);
                case EAST -> originalBlockPos.offset(-yOffset, 0, offset);
                case WEST -> originalBlockPos.offset(yOffset, 0, -offset);
                default -> originalBlockPos;
            };
        }

        // Looking straight ahead.
        return switch (direction) {
            case NORTH -> originalBlockPos.offset(-offset, yOffset, 0);
            case SOUTH -> originalBlockPos.offset(offset, yOffset, 0);
            case EAST -> originalBlockPos.offset(0, yOffset, offset);
            case WEST -> originalBlockPos.offset(0, yOffset, -offset);
            default -> originalBlockPos;
        };
    }
}
