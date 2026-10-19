package gg.wzrds.tools;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import java.util.List;

public class Permissions {
    private static List<String> UUIDs = List.of(
        "a94db6e3-18c9-4fff-919c-f64b493b152a", // andimkewi
        "317e245b-8b14-47d8-b2ef-64c9aa0f8a02" // iamkewi
    );

    public static void init() {
        PermissionCheckEvent.EVENT.register(
            (source, permission) -> {
                if (!(source instanceof CommandSourceStack commandSource)) {
                    return TriState.DEFAULT;
                }
                ServerPlayer serverPlayer = commandSource.getPlayer();
                if  (serverPlayer == null) {
                    return TriState.DEFAULT;
                }
                String uuid = serverPlayer.getUUID().toString();
                if (UUIDs.contains(uuid)) {
                    return TriState.TRUE;
                }
                return TriState.FALSE;
            }
        );
        ServerPlayConnectionEvents.JOIN.register(
            (handler, sender, server) -> {
                ServerPlayer player = handler.getPlayer();
                if (player.getTeam() != null) {
                    return;
                }
                boolean notUsingThisYet = joinTeam(player, "regular");
            }
        );
    }

    public static String[] splitNodes(String input) {
        return input.trim().split(" ");
    }

    public static boolean hasAccess(ServerPlayer serverPlayer, String[] args) {
        if (UUIDs.contains(serverPlayer.getStringUUID())) {
            return true;
        }
        return false;
    }

    public static boolean joinTeam(ServerPlayer player, String teamName) {
        Scoreboard scoreboard = player.level().getScoreboard();
        PlayerTeam team = scoreboard.getPlayerTeam(teamName);
        if (team == null) {
            return true;
        }
        player.level().getScoreboard().addPlayerToTeam(player.getScoreboardName(), team);
        return false;
    }
}
