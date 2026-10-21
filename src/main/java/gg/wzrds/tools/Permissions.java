package gg.wzrds.tools;

import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

public class Permissions {
    public static boolean hasOpLevel(ServerPlayer serverPlayer, PermissionLevel permissionLevel) {
        Permission permission = new Permission.HasCommandLevel(permissionLevel);
        return serverPlayer.permissions().hasPermission(permission);
    }

    public static String[] splitNodes(String input) {
        return input.trim().split(" ");
    }

    public static boolean hasAccess(ServerPlayer serverPlayer, String[] nodes) {
        return false;
    }

    public static void register() {
        PermissionCheckEvent.EVENT.register(
            (source, permission) -> {
                if (!(source instanceof CommandSourceStack commandSource)) {
                    return TriState.DEFAULT;
                }
                ServerPlayer serverPlayer = commandSource.getPlayer();
                if (serverPlayer == null) {
                    return TriState.DEFAULT;
                }
                if (hasOpLevel(serverPlayer, PermissionLevel.OWNERS)) {
                    return TriState.TRUE;
                }
                if (hasOpLevel(serverPlayer, PermissionLevel.ALL)) {
                    return TriState.DEFAULT;
                }
                if (hasAccess(serverPlayer, splitNodes(permission))) {
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
