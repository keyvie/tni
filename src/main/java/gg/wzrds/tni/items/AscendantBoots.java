package gg.wzrds.tni.items;

import gg.wzrds.tni.ToolsAndItems;
import gg.wzrds.tni.config.Config;
import gg.wzrds.tni.enchantments.Swiftness;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;


public class AscendantBoots extends CustomItem {
    public static final Identifier ID = ToolsAndItems.id("ascendant_boots");

    public Identifier id () {
        return ID;
    }

    @Override
    public Item vanillaItem() {
        return Items.NETHERITE_BOOTS;
    }

    @Override
    public String name() {
        return "Ascendant Boots";
    }

    @Override
    public Identifier itemModel() {
        return Identifier.fromNamespaceAndPath(
            Config.get().resource_pack_namespace,
            "ascendant_boots"
        );
    }

    @Override
    public void customize(ItemStack stack) {
        stack.set(
            DataComponents.EQUIPPABLE,
            Equippable.builder(EquipmentSlot.FEET)
                .setAsset(ResourceKey.create(
                    EquipmentAssets.ROOT_ID,
                    Identifier.fromNamespaceAndPath(Config.get().resource_pack_namespace, "ascendant")
                ))
                .build()
        );
        stack.set(
            DataComponents.UNBREAKABLE,
            Unit.INSTANCE
        );
        Registry<Enchantment> enchatRegistry = ToolsAndItems.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
        stack.enchant(Swiftness.get(ToolsAndItems.getServer().registryAccess()), 1);
        stack.enchant(enchatRegistry.getOrThrow(Enchantments.PROTECTION), 4);
        stack.enchant(enchatRegistry.getOrThrow(Enchantments.DEPTH_STRIDER), 3);
        stack.enchant(enchatRegistry.getOrThrow(Enchantments.SOUL_SPEED), 3);
    }
}
