package gg.wzrds.tni.enchantments;

import gg.wzrds.tni.ToolsAndItems;
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

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class Excavation {

    public static final ResourceKey<Enchantment> KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ToolsAndItems.id("excavation")
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

                if (player.isCrouching()) {
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

                int[][] blocksToBreak = switch (enchantLevel) {
                    case 1 -> new int[][] {
                            {-1, 0},
                            {1, 0}
                    };
                    case 2 -> new int[][] {
                            {0, 1},
                            {-1, 0},
                            {1, 0},
                            {0, -1}
                    };
                    case 3 -> new int[][] {
                            {-1, 1},
                            {0, 1},
                            {1, 1},
                            {-1, 0},
                            {1, 0},
                            {-1, -1},
                            {0, -1},
                            {1, -1}
                    };
                    default -> new int[][] {};
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
                    boolean plane = pitch > 45 || pitch < -45;

                    for (int[] target : blocksToBreak) {
                        BlockPos targetPos = getTargetBlockPos(
                            pos,
                            direction,
                            target,
                            plane
                        );

                        Block targetBlock = level.getBlockState(targetPos).getBlock();
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
            BlockPos originalPos,
            Direction direction,
            int[] target,
            boolean plane
    ) {
        int horizontal = target[0];
        int vertical = target[1];
        int x = 0;
        int y = 0;
        int z = 0;

        switch (direction) {
            case NORTH -> {
                x = -horizontal;
                if (plane) {
                    z = -vertical;
                } else {
                    y = vertical;
                }
            }
            case SOUTH -> {
                x = horizontal;
                if (plane) {
                    z = vertical;
                } else {
                    y = vertical;
                }
            }
            case EAST -> {
                z = horizontal;
                if (plane) {
                    x = vertical;
                } else {
                    y = vertical;
                }
            }
            case WEST -> {
                z = -horizontal;
                if (plane) {
                    x = -vertical;
                } else {
                    y = vertical;
                }
            }
        }

        return originalPos.offset(x, y, z);
    }
}
