package gg.wzrds.tni.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gg.wzrds.tni.ToolsAndItems;
import gg.wzrds.tni.config.Config;
import gg.wzrds.tni.config.PermissionTeam;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.TeamArgument;
import net.minecraft.world.scores.PlayerTeam;

public final class ConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
            .literal("config")
            .requires(source ->
                    Permissions.check(source, "wzrds.tni.config", false)
            )
            .then(Commands
                .literal("reload")
                .executes(ConfigCommand::executeReload)
            )
            .then(Commands
                .literal("permissions")
                .then(Commands
                    .literal("allow")
                    .then(Commands
                        .argument("team", TeamArgument.team())
                        .then(Commands
                            .argument("permission", StringArgumentType.greedyString())
                            .executes(ConfigCommand::executePermissionsAllow)
                        )
                    )
                )
                .then(Commands
                    .literal("deny")
                    .then(Commands
                        .argument("team", TeamArgument.team())
                        .then(Commands
                            .argument("permission", StringArgumentType.greedyString())
                            .executes(ConfigCommand::executePermissionsDeny)
                        )
                    )
                )
                .then(Commands
                    .literal("remove")
                    .then(Commands
                        .argument("team", TeamArgument.team())
                        .then(Commands
                            .argument("permission", StringArgumentType.greedyString())
                            .executes(ConfigCommand::executePermissionsRemove)
                        )
                    )
                )
                .then(Commands
                    .literal("delete")
                    .then(Commands
                        .argument("team", TeamArgument.team())
                        .executes(ConfigCommand::executePermissionsDelete)
                    )
                )
            )
        );
    }

    public static int executeReload(CommandContext<CommandSourceStack> context) {
        Config.load();
        CommandManager.respondWithStringSuccess(
            context,
            "Reloaded config for " + ToolsAndItems.MOD_ID,
            false
        );
        return 1;
    }

    public static int executePermissionsAllow(CommandContext<CommandSourceStack> context) {
        PlayerTeam team;
        try {
            team = TeamArgument.getTeam(context, "team");
        }  catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }
        PermissionTeam permissionTeam = Config.get().permissions.computeIfAbsent(team.getName(), _ -> new PermissionTeam());
        String permission = StringArgumentType.getString(context, "permission");
        if (permissionTeam.allow.contains(permission)) {
            permissionTeam.allow.remove(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Removed %s from %s teams's allowed overrides", permission, team.getName()), false);
        } else {
            permissionTeam.allow.add(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Added %s to %s teams's allowed overrides", permission, team.getName()), false);
        }
        permissionTeam.deny.remove(permission);
        Config.save();
        return 1;
    }

    public static int executePermissionsDeny(CommandContext<CommandSourceStack> context) {
        PlayerTeam team;
        try {
            team = TeamArgument.getTeam(context, "team");
        }  catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }
        PermissionTeam permissionTeam = Config.get().permissions.computeIfAbsent(team.getName(), _ -> new PermissionTeam());
        String permission = StringArgumentType.getString(context, "permission");
        if (permissionTeam.deny.contains(permission)) {
            permissionTeam.deny.remove(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Removed %s from %s teams's denied overrides", permission, team.getName()), false);
        } else {
            permissionTeam.deny.add(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Added %s to %s teams's denied overrides", permission, team.getName()), false);
        }
        permissionTeam.allow.remove(permission);
        Config.save();
        return 1;
    }

    public static int executePermissionsRemove(CommandContext<CommandSourceStack> context) {
        PlayerTeam team;
        try {
            team = TeamArgument.getTeam(context, "team");
        }  catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }
        PermissionTeam permissionTeam = Config.get().permissions.computeIfAbsent(team.getName(), _ -> new PermissionTeam());
        String permission = StringArgumentType.getString(context, "permission");
        permissionTeam.allow.remove(permission);
        permissionTeam.deny.remove(permission);
        CommandManager.respondWithStringSuccess(context, String.format("Removed %s from %s teams's overrides", permission, team.getName()), false);
        Config.save();
        return 1;
    }

    public static int executePermissionsDelete(CommandContext<CommandSourceStack> context) {
        PlayerTeam team;
        try {
            team = TeamArgument.getTeam(context, "team");
        }  catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }
        PermissionTeam permissionTeam = Config.get().permissions.get(team.getName());
        if (permissionTeam == null) {
            CommandManager.respondWithStringFailure(context, "That team doesnt have any permission overrides");
            return 0;
        }
        Config.get().permissions.remove(team.getName());
        Config.save();
        CommandManager.respondWithStringSuccess(context, String.format("Deleted %s team's permission overrides", team.getName()), false);
        return 1;
    }
}
