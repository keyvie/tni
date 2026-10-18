package gg.wzrds.tools.permissions;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public class Teams {
    public static void init() {
        ServerPlayConnectionEvents.JOIN.register(
            (handler, sender, server) -> {
                ServerPlayer player = handler.getPlayer();
                if (player.getTeam() != null) {
                    return;
                }
                int notUsingThisYet = joinTeam(player, "regular");
            }
        );
    }

    public static int joinTeam(ServerPlayer player, String teamName) {
        Scoreboard scoreboard = player.level().getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            return 0;
        }
        player.level().getScoreboard().addPlayerToTeam(player.getScoreboardName(), team);
        return 1;
    }
}
