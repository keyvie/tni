package gg.wzrds.tools.enchantments;

import gg.wzrds.tools.Tools;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public final class Swiftness {
    public static final ResourceKey<Enchantment> KEY = ResourceKey.create(
        Registries.ENCHANTMENT,
        Tools.id("swiftness")
    );

    public static Holder<Enchantment> get(RegistryAccess registryAccess) {
        return registryAccess.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(KEY);
    }

    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register((server) -> {
            for (ServerPlayer player : server.getPlayerList().getPlayers()) {
                ItemStack feetStack = player.getItemBySlot(EquipmentSlot.FEET);
                if (feetStack.isEmpty()) {
                    return;
                }
                int enchantLevel = feetStack.getEnchantments().getLevel(get(server.registryAccess()));
                if (enchantLevel > 0) {
                    player.addEffect(new MobEffectInstance(MobEffects.SPEED, 10 * 20, enchantLevel-1, true, false, false));
                }
            }
        });
    }
}
