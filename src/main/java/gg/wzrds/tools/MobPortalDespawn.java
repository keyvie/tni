 package gg.wzrds.tools;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityLevelChangeEvents;

import java.util.HashMap;
import java.util.Map;

public class MobPortalDespawn {
    public static Map<Mob, Long> IMMUNE_MOBS = new HashMap<>();

    public static void init() {
        ServerEntityLevelChangeEvents.AFTER_ENTITY_CHANGE_LEVEL.register(
            (originalEntity, newEntity, origin, destination) -> {
                if (!(newEntity instanceof Mob mob)) {
                    return;
                }
                long immuneSeconds = 60;
                long gameTicks = 20*immuneSeconds;
                IMMUNE_MOBS.put(mob, origin.getDefaultClockTime() + gameTicks);
            }
        );

    }

    public static boolean isImmune(Mob mob) {
        Long expiresAt = IMMUNE_MOBS.get(mob);
        if (expiresAt == null) {
            return false;
        }

        ServerLevel level = (ServerLevel) mob.level();
        long currentTime = level.getDefaultClockTime();

        if (currentTime < expiresAt) {
            return true;
        }
        IMMUNE_MOBS.remove(mob);
        return false;
    }
}
