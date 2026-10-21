package gg.wzrds.tools.commands;

import gg.wzrds.tools.items.Ascendant;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.world.item.ItemStack;

public final class ItemsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("items")
            .requires(source ->
                Permissions.check(source, "wzrds.tools.items", false)
            )
            .then(
                Commands.literal("spawn")
                .executes(ItemsCommand::executeSpawn)
            )
        );
    }

    public static int executeSpawn(CommandContext<CommandSourceStack> context) {
        ServerPlayer serverPlayer = context.getSource().getPlayer();
        if (serverPlayer == null) {
            return 0;
        }
        ServerLevel level = context.getSource().getLevel();
        ItemStack ascendantBoots = Ascendant.createBoots(level);
        if (serverPlayer.getInventory().add(ascendantBoots)) {
            serverPlayer.drop(ascendantBoots, false, Prediction.PREDICTED);
        }
        return 1;
    }
}
