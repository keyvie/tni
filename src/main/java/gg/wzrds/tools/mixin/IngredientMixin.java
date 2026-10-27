package gg.wzrds.tools.mixin;

import gg.wzrds.tools.Tools;
import gg.wzrds.tools.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Ingredient.class)
public class IngredientMixin {
    @Inject(
            method = "test(Lnet/minecraft/world/item/ItemStack;)Z",
            at = @At("HEAD"),
            cancellable = true
    )
    private void checkCustomModel(ItemStack input, CallbackInfoReturnable<Boolean> ci) {
        Identifier itemModel = input.get(DataComponents.ITEM_MODEL);

        if (itemModel == null) {
            return;
        }

        if (itemModel.getNamespace().equals(Config.get().resource_pack_namespace)) {
            ci.setReturnValue(false);
        }
    }
}
