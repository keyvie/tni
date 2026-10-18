package gg.wzrds.tools.mixin;

import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Mob.class)
public class MobMixin {

    @ModifyArg(method = "checkDespawn", at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/world/level/Level;getNearestPlayer(Lnet/minecraft/world/entity/Entity;D)Lnet/minecraft/world/entity/player/Player;"
    ), index = 1)
    private double preventPortalDespawns(double maxDist) {
        return 25.0 * 16.0;
    }
}