package gg.wzrds.tools.items;

import gg.wzrds.tools.Tools;
import gg.wzrds.tools.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemAttributeModifiers;


public class BookOfReach extends CustomItem {
    public static final Identifier ID = Tools.id("book_of_reach");

    public Identifier id () {
        return ID;
    }

    @Override
    public Item vanillaItem() {
        return Items.KNOWLEDGE_BOOK;
    }

    @Override
    public String name() {
        return "Book of Reach";
    }

    @Override
    public Identifier itemModel() {
        return Identifier.fromNamespaceAndPath(
            Config.get().resource_pack_namespace,
            "book_of_reach"
        );
    }

    @Override
    public void customize(ItemStack stack) {
        stack.set(
            DataComponents.ATTRIBUTE_MODIFIERS,
            ItemAttributeModifiers.builder()
                .add(Attributes.BLOCK_INTERACTION_RANGE,
                    new AttributeModifier(
                        Tools.id("increased_pickup_range"),
                        2,
                        AttributeModifier.Operation.ADD_VALUE
                    ),
                    EquipmentSlotGroup.HAND
                ).build()
        );
    }
}
