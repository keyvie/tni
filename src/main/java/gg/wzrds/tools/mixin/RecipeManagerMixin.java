package gg.wzrds.tools.mixin;

import gg.wzrds.tools.Tools;
import gg.wzrds.tools.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(RecipeManager.class)
public class RecipeManagerMixin {

    @Inject(
        method = "getRecipeFor(Lnet/minecraft/world/item/crafting/RecipeType;Lnet/minecraft/world/item/crafting/RecipeInput;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;",
        at = @At("RETURN"),
            cancellable = true)
    private void getRecipeFor(
            RecipeType<?> type,
            RecipeInput input,
            Level level,
            CallbackInfoReturnable<Optional<RecipeHolder<?>>> ci
    ) {
        Optional<RecipeHolder<?>> oRecipe = ci.getReturnValue();
        if (oRecipe.isEmpty()) {
            return;
        }
        RecipeHolder<?> recipe = oRecipe.get();

        for (var i = 0; i < input.size(); i++){
            ItemStack stack = input.getItem(i);
            boolean shouldReturn = checkForCustomItems(recipe, stack);
            if (shouldReturn) {
                ci.setReturnValue(Optional.empty());
                return;
            }
        }
    }

    @Unique
    private static boolean checkForCustomItems(RecipeHolder<?> recipe, ItemStack stack) {
        Identifier recipeId = recipe.id().identifier();

        Identifier itemModel = stack.get(DataComponents.ITEM_MODEL);
        if (itemModel == null) {
            return false;
        }
        if (itemModel.getNamespace().equals(Identifier.DEFAULT_NAMESPACE)) {
            return false;
        }
        if (!itemModel.getNamespace().equals(Config.get().resource_pack_namespace)) {
            return false;
        }
        return !Tools.owned(recipeId);
    }
}
