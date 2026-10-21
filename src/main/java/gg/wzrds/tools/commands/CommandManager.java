package gg.wzrds.tools.commands;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

public final class CommandManager {

    public static void respondWithStringSuccess(CommandContext<CommandSourceStack> context, String text, boolean broadcast) {
        context.getSource().sendSuccess(() -> Component.literal(text), broadcast);
    }

    public static void respondWithStringFailure(CommandContext<CommandSourceStack> context, String text) {
        context.getSource().sendFailure(Component.literal(text));
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
            dispatcher,
            registryAccess,
            environment
        ) -> {
            ConfigCommand.register(dispatcher);
            ItemsCommand.register(dispatcher);
            StaffModeCommand.register(dispatcher);
        });
    }
}
