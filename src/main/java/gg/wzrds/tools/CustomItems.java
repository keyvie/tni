package gg.wzrds.tools;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class CustomItems {
    public static final Map<Identifier, Function<ServerLevel, ItemStack>> ITEMS = new HashMap<>();
    public static final String TAG = Tools.id("custom_item").toString();

    public static void register(Identifier id, Function<ServerLevel, ItemStack> factory) {
        ITEMS.put(id, factory);
    }

    public static ItemStack create(ServerLevel level, Identifier id) {
        Function<ServerLevel, ItemStack> factory = ITEMS.get(id);
        if (factory == null) {
            return null;
        }
        return factory.apply(level);
    }

    public static void addTag(CompoundTag tag) {
        tag.putBoolean(TAG, true);
    }

    public static boolean is(ItemStack stack) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return false;
        }
        if (customData.isEmpty()) {
            return false;
        }
        CompoundTag tag = customData.copyTag();
        return tag.contains(TAG);
    }

    public static boolean is(ItemStack stack, Identifier id) {
        CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            return false;
        }
        if (customData.isEmpty()) {
            return false;
        }
        CompoundTag tag = customData.copyTag();
        return tag.contains(id.toString());
    }
}
