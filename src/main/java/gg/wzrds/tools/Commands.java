package gg.wzrds.tools;

import gg.wzrds.tools.commands.StaffMode;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public final class Commands {

    public static void init() {
        CommandRegistrationCallback.EVENT.register((
            dispatcher,
            registryAccess,
            environment
        ) -> {
            StaffMode.register(dispatcher);
        });
    }
}
