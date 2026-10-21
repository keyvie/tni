package gg.wzrds.tools.commands;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;

public class Config {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("config")
                .requires(source ->
                        Permissions.check(source, "wzrds.tools.config", false)
                )
                .executes(gg.wzrds.tools.commands.Items::execute)
        );
    }

    public static int execute(CommandContext<CommandSourceStack> context) {
            return 1;
        }
}
