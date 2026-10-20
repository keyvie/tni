package gg.wzrds.tools.commands;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class Commands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((
            dispatcher,
            registryAccess,
            environment
        ) -> {
            Rights.register(dispatcher);
            Items.register(dispatcher);
            StaffMode.register(dispatcher);
        });
    }
}
