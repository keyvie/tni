package gg.wzrds.tools;

import gg.wzrds.tools.enchantments.Excavation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public final class CustomEnchantments {

    private static ResourceKey<Enchantment> key(String path) {
        Identifier id = Tools.id(path);
        return ResourceKey.create(Registries.ENCHANTMENT, id);
    }

    public static final ResourceKey<Enchantment> EXCAVATION = key("excavation");

    public static void init() {
        Excavation.init();
    }
}
