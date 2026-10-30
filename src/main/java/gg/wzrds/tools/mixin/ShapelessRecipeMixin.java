package gg.wzrds.tools.mixin;

import gg.wzrds.tools.Tools;
import gg.wzrds.tools.config.Config;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ShapelessRecipe.class)
public class ShapelessRecipeMixin {

    @Inject(
        method = "matches(Lnet/minecraft/world/item/crafting/CraftingInput;Lnet/minecraft/world/level/Level;)Z",
        at = @At("HEAD"),
        cancellable = true
    )
    private void blockCustomItemsFromRegularRecipies(CraftingInput input, Level level, CallbackInfoReturnable<Boolean> cir) {
        for (ItemStack item : input.items()) {
            Identifier itemModelId = item.get(DataComponents.ITEM_MODEL);
            if (itemModelId == null) continue;
            if (itemModelId.getNamespace().equals(Tools.MOD_ID) || itemModelId.getNamespace().equals(Config.get().resource_pack_namespace)) {
                cir.setReturnValue(false);
                return;
            }
        }
    }

}
