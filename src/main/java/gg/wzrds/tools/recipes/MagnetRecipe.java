package gg.wzrds.tools.recipes;

import com.mojang.serialization.MapCodec;
import gg.wzrds.tools.items.Magnet;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class MagnetRecipe extends CustomRecipe {
    public static final MagnetRecipe SELF = new MagnetRecipe();
    public static final RecipeSerializer<MagnetRecipe> SERIALIZER = new RecipeSerializer<>(MapCodec.unit(SELF), StreamCodec.unit(SELF));

    public static void register() {
        Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, Magnet.ID, SERIALIZER);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (input.width() != 3 || input.height() != 3) {
            return false;
        }
        Item[] pattern = {
                null, null, null,
                Items.EXPERIENCE_BOTTLE, Items.BOOK, Items.EXPERIENCE_BOTTLE,
                null, Items.ENDER_PEARL, null
        };
        var index = 0;
        for (Item item : pattern) {
            ItemStack stack = input.getItem(index);
            index++;
            if (item == null && !stack.isEmpty()) {
                return false;
            } else if (item == null) {
                continue;
            }
            if (!stack.is(item)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return Magnet.create(null);
    }

    @Override
    public RecipeSerializer<MagnetRecipe> getSerializer() {
        return SERIALIZER;
    }
}