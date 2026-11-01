package gg.wzrds.tni.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;

import java.util.List;
import java.util.Optional;

public abstract class CustomItem {
    public static Item DEFAULT_VANILLA_ITEM = Items.KNOWLEDGE_BOOK;

    public Item vanillaItem() {
        return DEFAULT_VANILLA_ITEM;
    };

    public abstract Identifier id();

    public String name() {
        return id().toString();
    };

    public int count() {
        return 1;
    };

    public int maxStackSize() {
        return 1;
    }

    public Identifier itemModel() {
        return vanillaItem().components().get(DataComponents.ITEM_MODEL);
    }

    public ItemStack createStack() {
        ItemStack itemStack = new ItemStack(vanillaItem());
        itemStack.set(DataComponents.ITEM_NAME, Component.literal(name()));
        itemStack.setCount(count());
        itemStack.set(DataComponents.MAX_STACK_SIZE, maxStackSize());
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putBoolean("custom_item", true);
        compoundTag.putString("custom_id", id().toString());
        appendTags(compoundTag);
        itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(compoundTag));
        itemStack.set(DataComponents.ITEM_MODEL, itemModel());
        customize(itemStack);
        return itemStack;
    }

    public void appendTags(CompoundTag compoundTag) {}
    public void customize(ItemStack itemStack) {}

    public boolean stackEqualsSelf(ItemStack itemStack) {
        CustomData data = itemStack.get(DataComponents.CUSTOM_DATA);
        if (data == null) return false;
        CompoundTag tag = data.copyTag();
        if (!tag.getBooleanOr("custom_item", false)) return false;
        Optional<String> stringId = tag.getString("custom_id");
        if (stringId.isEmpty()) return false;
        Identifier id = Identifier.tryParse(stringId.get());
        if (id == null) return false;
        return id().equals(id);
    }

    public void register() {}

    public List<Integer> ticks() {
        return List.of(0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19);
    }

    public void onHoldingInHand(MinecraftServer server, ServerPlayer player, ItemStack stack, InteractionHand hand) {}
}
