package gg.wzrds.tni.mixin;

import gg.wzrds.tni.ToolsAndItems;
import gg.wzrds.tni.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapedRecipe.class)
public class ShapedRecipeMixin {

    @Inject(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void blockCustomItemsFromRegularRecipies(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack item : input.items()) {
            Identifier itemModelId = item.get(DataComponents.ITEM_MODEL);
            if (itemModelId == null) continue;
            if (itemModelId.getNamespace().equals(ToolsAndItems.MOD_ID) || itemModelId.getNamespace().equals(Config.get().resource_pack_namespace)) {
                cir.setReturnValue(false);
                return;
            }
        }
    }

}
