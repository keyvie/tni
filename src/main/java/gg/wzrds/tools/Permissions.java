package gg.wzrds.tools;

import gg.wzrds.tools.config.Config;
import gg.wzrds.tools.config.PermissionTeam;
import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.Team;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public final class Permissions {
    public static boolean hasOpLevel(ServerPlayer serverPlayer, PermissionLevel permissionLevel) {
        Permission permission = new Permission.HasCommandLevel(permissionLevel);
        return serverPlayer.permissions().hasPermission(permission);
    }

    public static String[] splitNodes(String input) {
        return input.trim().split("\\.");
    }

    private static Boolean hasAccess(List<String> allow, List<String> deny, String node) {
        String[] parts = splitNodes(node);
        List<String> nodes = new ArrayList<>();
        StringBuilder prefix = new StringBuilder();
        for (String part : parts) {
            if (!prefix.isEmpty()) {
                prefix.append(".");
            }
            prefix.append(part);
            nodes.add(prefix.toString());
        }
        Collections.reverse(nodes);
        for (String n : nodes) {
            if (deny.contains(n)) {
                return false;
            }
            if (allow.contains(n)) {
                return true;
            }
        }
        return null;
    }

    public static Boolean hasAccess(ServerPlayer serverPlayer, String node) {
        String uuid = serverPlayer.getUUID().toString();
        Team team = serverPlayer.getTeam();
        if (team == null) {
            return null;
        }
        PermissionTeam permissionTeam = Config.get().permissions.get(team.getName());
        if (permissionTeam == null) {
            return null;
        }

        return hasAccess(permissionTeam.allow, permissionTeam.deny, node);
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
                Boolean hasPermission = hasAccess(serverPlayer, permission);
                if (hasOpLevel(serverPlayer, PermissionLevel.OWNERS)) {
                    return TriState.TRUE;
                }
                if (Boolean.FALSE.equals(hasPermission)) {
                    return TriState.FALSE;
                }
                if (Boolean.TRUE.equals(hasPermission)) {
                    return TriState.TRUE;
                }
                if (hasOpLevel(serverPlayer, PermissionLevel.MODERATORS)) {
                    return TriState.DEFAULT;
                }
                return TriState.DEFAULT;
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
            return false;
        }
        player.level().getScoreboard().addPlayerToTeam(player.getScoreboardName(), team);
        return true;
    }
}
