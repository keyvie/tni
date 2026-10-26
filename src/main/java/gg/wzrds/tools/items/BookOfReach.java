package gg.wzrds.tools.items;

import gg.wzrds.tools.CustomItems;
import gg.wzrds.tools.Tools;
import gg.wzrds.tools.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public final class BookOfReach {
    public static final Identifier ID = Tools.id("book_of_reach");

    public static void register() {
        CustomItems.register(ID, BookOfReach::create);
    }

    public static ItemStack create(ServerLevel level) {
        ItemStack stack = new ItemStack(Items.HEART_OF_THE_SEA);

        stack.set(
            DataComponents.ITEM_NAME,
            Component.literal("Book of Reach")
        );
        stack.setDamageValue(0);
        stack.setCount(1);
        stack.set(DataComponents.MAX_STACK_SIZE, 1);

        stack.set(
            DataComponents.ATTRIBUTE_MODIFIERS,
            ItemAttributeModifiers.builder()
                .add(Attributes.BLOCK_INTERACTION_RANGE,
                    new AttributeModifier(
                        Tools.id("increased_pickup_range"),
                        8,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                    ),
                    EquipmentSlotGroup.HAND
                ).build()
        );

        stack.set(
            DataComponents.ITEM_MODEL,
            Identifier.fromNamespaceAndPath(
                Config.get().resource_pack_namespace,
                "book_of_reach"
            )
        );

        CompoundTag tag = new CompoundTag();
        tag.putBoolean(ID.toString(), true);
        CustomItems.addTag(tag);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        return stack;
    }
}
