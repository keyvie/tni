package gg.wzrds.tools.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import gg.wzrds.tools.Tools;
import gg.wzrds.tools.commands.suggestions.PermissionGroupSuggestionProvider;
import gg.wzrds.tools.config.Config;
import gg.wzrds.tools.config.PermissionGroup;
import gg.wzrds.tools.config.PermissionUser;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.server.players.NameAndId;

import java.util.Collection;

public class ConfigCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands
            .literal("config")
            .requires(source ->
                    Permissions.check(source, "wzrds.tools.config", false)
            )
            .then(Commands
                .literal("reload")
                .executes(ConfigCommand::executeReload)
            )
            .then(Commands
                .literal("permissions")
                .then(Commands
                    .literal("group")
                    .then(Commands
                        .literal("create")
                        .then(Commands
                            .argument("name", StringArgumentType.string())
                            .executes(ConfigCommand::executePermissionsGroupCreate)
                        )
                    )
                    .then(Commands
                        .literal("delete")
                        .then(Commands
                            .argument("name", StringArgumentType.string())
                            .suggests(new PermissionGroupSuggestionProvider())
                            .executes(ConfigCommand::executePermissionsGroupDelete)
                        )
                    )
                    .then(Commands
                        .literal("allow")
                        .then(Commands
                            .argument("name", StringArgumentType.string())
                            .suggests(new PermissionGroupSuggestionProvider())
                            .then(Commands
                                .argument("permission", StringArgumentType.greedyString())
                                .executes(ConfigCommand::executePermissionsGroupAllow)
                            )
                        )

                    )
                    .then(Commands
                        .literal("deny")
                        .then(Commands
                            .argument("name", StringArgumentType.string())
                            .suggests(new PermissionGroupSuggestionProvider())
                            .then(Commands
                                .argument("permission", StringArgumentType.greedyString())
                                .executes(ConfigCommand::executePermissionsGroupDeny)
                            )
                        )
                    )
                )
                .then(Commands
                    .literal("user")
                    .then(Commands.argument("player", GameProfileArgument.gameProfile())
                        .then(Commands
                            .literal("group")
                            .then(Commands
                                .literal("grant")
                                .then(Commands
                                    .argument("name", StringArgumentType.string())
                                    .suggests(new PermissionGroupSuggestionProvider())
                                    .executes(ConfigCommand::executePermissionsUserGroupGrant)
                                )
                            )
                            .then(Commands
                                .literal("revoke")
                                .then(Commands
                                    .argument("name", StringArgumentType.string())
                                    .suggests(new PermissionGroupSuggestionProvider())
                                    .executes(ConfigCommand::executePermissionsUserGroupRevoke)
                                )
                            )
                        )
                        .then(Commands
                            .literal("allow")
                            .then(Commands
                                .argument("permission", StringArgumentType.greedyString())
                                .executes(ConfigCommand::executePermissionsUserAllow)
                            )
                        )
                        .then(Commands
                            .literal("deny")
                            .then(Commands
                                .argument("permission", StringArgumentType.greedyString())
                                .executes(ConfigCommand::executePermissionsUserDeny)
                            )
                        )
                        .then(Commands
                            .literal("remove")
                            .executes(ConfigCommand::executePermissionsUserRemove)
                        )
                    )
                )
            )
        );
    }

    public static int executeReload(CommandContext<CommandSourceStack> context) {
        Config.load();
        CommandManager.respondWithStringSuccess(
            context,
            "Reloaded config for " + Tools.MOD_ID,
            false
        );
        return 1;
    }

    public static int executePermissionsGroupCreate(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        PermissionGroup group = Config.get().permissions.groups.get(name);
        if (group != null) {
            CommandManager.respondWithStringFailure(context, "A group with that name already exists");
            return 0;
        }
        Config.get().permissions.groups.put(name, new PermissionGroup());
        Config.save();
        CommandManager.respondWithStringSuccess(context, String.format("Created '%s' group", name), false);
        return 1;
    }

    public static int executePermissionsGroupDelete(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        PermissionGroup group = Config.get().permissions.groups.get(name);
        if (group == null) {
            CommandManager.respondWithStringFailure(context, "That group doesnt exist");
            return 0;
        }
        Config.get().permissions.groups.remove(name);
        Config.save();
        CommandManager.respondWithStringSuccess(context, String.format("Deleted '%s' group", name), false);
        return 1;
    }


    public static int executePermissionsGroupAllow(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        String permission = StringArgumentType.getString(context, "permission");
        PermissionGroup group = Config.get().permissions.groups.get(name);
        if (group == null) {
            CommandManager.respondWithStringFailure(context, "No group with that name exists");
            return 0;
        }
        if (group.allow.contains(permission)) {
            group.allow.remove(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Removed '%s' from '%s' group's allow list", permission, name), false);
        } else {
            group.allow.add(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Added '%s' to '%s' group's allow list", permission, name), false);
        }
        group.deny.remove(permission);
        Config.save();
        return 1;
    }

    public static int executePermissionsGroupDeny(CommandContext<CommandSourceStack> context) {
        String name = StringArgumentType.getString(context, "name");
        String permission = StringArgumentType.getString(context, "permission");
        PermissionGroup group = Config.get().permissions.groups.get(name);
        if (group == null) {
            CommandManager.respondWithStringFailure(context, "No group with that name exists");
            return 0;
        }
        if (group.deny.contains(permission)) {
            group.deny.remove(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Removed '%s' from '%s' group's deny list", permission, name), false);
        } else {
            group.deny.add(permission);
            CommandManager.respondWithStringSuccess(context, String.format("Added '%s' to '%s' group's deny list", permission, name), false);
        }
        group.allow.remove(permission);
        Config.save();
        return 1;
    }

    // User

    public static int executePermissionsUserGroupGrant(CommandContext<CommandSourceStack> context) {
        NameAndId profile;
        try {
            Collection<NameAndId> profiles = GameProfileArgument.getGameProfiles(context, "player");
            if (profiles.size() != 1) {
                CommandManager.respondWithStringFailure(context, "This argument only supports one player");
                return 0;
            }
            profile = profiles.iterator().next();
        } catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }

        String name = StringArgumentType.getString(context, "name");
        PermissionGroup group = Config.get().permissions.groups.get(name);
        if (group == null) {
            CommandManager.respondWithStringFailure(context, "No group with that name exists");
            return 0;
        }
        String uuid = profile.id().toString();
        if (group.uuids.contains(uuid)) {
            CommandManager.respondWithStringFailure(
                context,
                String.format("%s is already in %s group", profile.name(), name)
            );
            return 0;
        }

        group.uuids.add(uuid);
        CommandManager.respondWithStringSuccess(
            context,
            String.format("%s has been granted %s group", profile.name(), name),
            false
        );
        Config.save();
        return 1;
    }

    public static int executePermissionsUserGroupRevoke(CommandContext<CommandSourceStack> context) {
        NameAndId profile;
        try {
            Collection<NameAndId> profiles = GameProfileArgument.getGameProfiles(context, "player");
            if (profiles.size() != 1) {
                CommandManager.respondWithStringFailure(context, "This argument only supports one player");
                return 0;
            }
            profile = profiles.iterator().next();
        } catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }

        String name = StringArgumentType.getString(context, "name");
        PermissionGroup group = Config.get().permissions.groups.get(name);
        if (group == null) {
            CommandManager.respondWithStringFailure(context, "No group with that name exists");
            return 0;
        }
        String uuid = profile.id().toString();
        if (!group.uuids.contains(uuid)) {
            CommandManager.respondWithStringFailure(
                context,
                String.format("%s is not in %s group", profile.name(), name)
            );
            return 0;
        }

        group.uuids.remove(uuid);
        CommandManager.respondWithStringSuccess(
            context,
            String.format("%s has been revoked from %s group", profile.name(), name),
            false
        );
        Config.save();
        return 1;
    }

    public static int executePermissionsUserAllow(CommandContext<CommandSourceStack> context) {
        NameAndId profile;
        try {
            Collection<NameAndId> profiles = GameProfileArgument.getGameProfiles(context, "player");
            if (profiles.size() != 1) {
                CommandManager.respondWithStringFailure(context, "This argument only supports one player");
                return 0;
            }
            profile = profiles.iterator().next();
        } catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }
        String uuid =  profile.id().toString();

        String permission = StringArgumentType.getString(context, "permission");
        PermissionUser user = Config.get().permissions.users.computeIfAbsent(uuid, key -> new PermissionUser());
        user.deny.remove(permission);
        if (user.allow.contains(permission)) {
            user.allow.remove(permission);
            CommandManager.respondWithStringSuccess(
                context,
                String.format("Unallowed %s from %s", permission, profile.name()),
                false
            );
        } else {
            user.allow.add(permission);
            CommandManager.respondWithStringSuccess(
                context,
                String.format("Allowed %s for %s", permission, profile.name()),
                false
            );
        }
        Config.save();
        return 1;
    }

    public static int executePermissionsUserDeny(CommandContext<CommandSourceStack> context) {
        NameAndId profile;
        try {
            Collection<NameAndId> profiles = GameProfileArgument.getGameProfiles(context, "player");
            if (profiles.size() != 1) {
                CommandManager.respondWithStringFailure(context, "This argument only supports one player");
                return 0;
            }
            profile = profiles.iterator().next();
        } catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }
        String uuid =  profile.id().toString();

        String permission = StringArgumentType.getString(context, "permission");
        PermissionUser user = Config.get().permissions.users.computeIfAbsent(uuid, key -> new PermissionUser());
        user.allow.remove(permission);
        if (user.deny.contains(permission)) {
            user.deny.remove(permission);
            CommandManager.respondWithStringSuccess(
                context,
                String.format("Undenied %s from %s", permission, profile.name()),
                false
            );
        } else {
            user.deny.add(permission);
            CommandManager.respondWithStringSuccess(
                context,
                String.format("Denied %s for %s", permission, profile.name()),
                false
            );
        }
        Config.save();
        return 1;
    }

    public static int executePermissionsUserRemove(CommandContext<CommandSourceStack> context) {
        NameAndId profile;
        try {
            Collection<NameAndId> profiles = GameProfileArgument.getGameProfiles(context, "player");
            if (profiles.size() != 1) {
                CommandManager.respondWithStringFailure(context, "This argument only supports one player");
                return 0;
            }
            profile = profiles.iterator().next();
        } catch (CommandSyntaxException e) {
            CommandManager.respondWithStringFailure(context, e.getMessage());
            return 0;
        }
        String uuid = profile.id().toString();
        boolean success = false;

        if (Config.get().permissions.users.containsKey(uuid)) {
            Config.get().permissions.users.remove(uuid);
            success = true;
        }

        Collection<PermissionGroup> groups = Config.get().permissions.groups.values();
        for (PermissionGroup group : groups) {
            if (group.uuids.contains(uuid)) {
                group.uuids.remove(uuid);
                success = true;
            }
        }

        if (!success) {
            CommandManager.respondWithStringFailure(
                context,
                String.format("%s was not found in the permissions config", profile.name())
            );
            return 0;
        }
        Config.save();
        CommandManager.respondWithStringSuccess(
                context,
                String.format("%s was removed from the permissions config", profile.name()),
                true
        );
        return 1;
    }
}
