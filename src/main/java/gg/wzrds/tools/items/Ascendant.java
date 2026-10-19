package gg.wzrds.tools.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.minecraft.world.item.equipment.Equippable;

public class Ascendant {

    public static ResourceKey<EquipmentAsset> ASCENDANT_BOOTS = ResourceKey.create(
        EquipmentAssets.ROOT_ID,
        Identifier.fromNamespaceAndPath("wzrds", "ascendant")
    );

    public static ItemStack createBoots() {
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
        stack.set(
            DataComponents.UNBREAKABLE,
            Unit.INSTANCE
        );
        return stack;
    }
}
