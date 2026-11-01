package gg.wzrds.tni.mixin;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.KnowledgeBookItem;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KnowledgeBookItem.class)
public class KnowledgeBookItemMixin {
    @Inject(
        method = "use",
        at = @At("HEAD"),
        cancellable = true
    )
    private void dontConsumeCustomItems(
        Level level,
        Player player,
        InteractionHand hand,
        CallbackInfoReturnable<InteractionResult> cir
    ) {
        CustomData data = player.getItemInHand(hand).get(DataComponents.CUSTOM_DATA);
        if (data != null && data.copyTag().getBooleanOr("custom_item", false)) {
            cir.setReturnValue(InteractionResult.PASS);
        }
    }
}
