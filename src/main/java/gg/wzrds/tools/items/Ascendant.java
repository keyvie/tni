package gg.wzrds.tools.items;

import gg.wzrds.tools.Config;
import gg.wzrds.tools.enchantments.Swiftness;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

public class Ascendant {

    public static ResourceKey<EquipmentAsset> ASCENDANT_BOOTS = ResourceKey.create(
        EquipmentAssets.ROOT_ID,
        Identifier.fromNamespaceAndPath(Config.settings.resource_pack_namespace, "ascendant")
    );

    public static ItemStack createBoots(ServerLevel level) {
        ItemStack stack = new ItemStack(Items.NETHERITE_BOOTS);

        stack.set(
            DataComponents.CUSTOM_NAME,
            Component.literal("Ascendant Boots")
        );
        stack.setDamageValue(0);
        stack.setCount(1);
        stack.set(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.FEET)
                .setAsset(ASCENDANT_BOOTS)
                .build()
        );
        stack.set(DataComponents.ITEM_MODEL, Identifier.fromNamespaceAndPath("wzrds", "ascendant_boots"));
        stack.set(
            DataComponents.UNBREAKABLE,
            Unit.INSTANCE
        );
        Registry<Enchantment> enchatRegistry = level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        stack.enchant(Swiftness.get(level.registryAccess()), 1);
        stack.enchant(enchatRegistry.getOrThrow(Enchantments.PROTECTION), 4);
        stack.enchant(enchatRegistry.getOrThrow(Enchantments.DEPTH_STRIDER), 3);
        stack.enchant(enchatRegistry.getOrThrow(Enchantments.SOUL_SPEED), 3);
        return stack;
    }
}
