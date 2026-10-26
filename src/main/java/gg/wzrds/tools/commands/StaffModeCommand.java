package gg.wzrds.tools.commands;

import com.mojang.brigadier.arguments.BoolArgumentType;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import gg.wzrds.tools.data.StaffModeData.Position;
import gg.wzrds.tools.data.StaffModeData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.resources.Identifier;

import java.util.Set;
import java.util.UUID;

public final class StaffModeCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("staff-mode")
            .requires(source ->
                Permissions.check(
                    source,
                    "wzrds.tools.staff-mode",
                    false
                )
            )
            .executes((context) -> execute(context, true))
            .then(Commands
                .argument("return", BoolArgumentType.bool())
                .executes((context) -> execute(context, BoolArgumentType.getBool(context, "return")))
            )
        );
    }

    public static int execute(CommandContext<CommandSourceStack> context, boolean shouldReturn) {
        ServerPlayer serverPlayer = context.getSource().getPlayer();
        if (serverPlayer == null) {
            return 0;
        }
        UUID uuid = serverPlayer.getUUID();
        MinecraftServer server = context.getSource().getServer();
        StaffModeData staffModeData = StaffModeData.GetSelf(server);

        Position position = staffModeData.getPosition(uuid);

        if (position == null) {
            position = new Position(
                serverPlayer.level().dimension().identifier().toString(),
                serverPlayer.getX(),
                serverPlayer.getY(),
                serverPlayer.getZ(),
                serverPlayer.getYRot(),
                serverPlayer.getXRot()
            );
            staffModeData.setPosition(uuid, position);
            serverPlayer.setGameMode(GameType.SPECTATOR);
            context.getSource().sendSuccess(() -> Component.literal("Staff Mode has been Enabled"), false);
            return 1;
        }

        staffModeData.deletePosition(uuid);
        ResourceKey<Level> dimension = ResourceKey.create(
                Registries.DIMENSION,
                Identifier.parse(position.dimension())
        );
        ServerLevel level = server.getLevel(dimension);
        if (level == null) {
            level = serverPlayer.level();
        }
        if  (shouldReturn) {
            serverPlayer.teleportTo(
                level,
                position.x(),
                position.y(),
                position.z(),
                Set.of(),
                position.yaw(),
                position.pitch(),
                false
            );
        }

        serverPlayer.setGameMode(GameType.SURVIVAL);
        context.getSource().sendSuccess(() -> Component.literal("Staff Mode has been Disabled"), false);
        return 1;
    }
}
