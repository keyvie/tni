package gg.wzrds.tools.mixin;

import gg.wzrds.tools.MobPortalDespawn;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {

    @Inject(at = @At("HEAD"), method = "checkDespawn", cancellable = true)
    private void preventPortalDespawns(CallbackInfo ci) {
        Mob mob = ( Mob ) ( Object ) this;

        if (MobPortalDespawn.isImmune(mob)) {
            ci.cancel();
        }
    }
}