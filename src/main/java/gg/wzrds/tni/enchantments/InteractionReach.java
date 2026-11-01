package gg.wzrds.tni.enchantments;

import gg.wzrds.tni.ToolsAndItems;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public final class InteractionReach {
    public static final ResourceKey<Enchantment> KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        ToolsAndItems.id("interaction_reach")
    );

    public static Holder<Enchantment> get(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(KEY);
    }

    public static void register() {
        PlayerBlockBreakEvents.AFTER.register(InteractionReach::onPlayerBlockBreak);
    }

    private static void onPlayerBlockBreak(Level level, Player player, BlockPos blockPos, BlockState blockState, @Nullable BlockEntity blockEntity) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (EnchantmentHelper.getItemEnchantmentLevel(get(level.registryAccess()), stack) <= 0) continue;
            stack.hurtAndBreak(1, player, hand);
            return;
        }
    }
}
