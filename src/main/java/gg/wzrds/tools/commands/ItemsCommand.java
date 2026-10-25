package gg.wzrds.tools.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import gg.wzrds.tools.CustomItems;
import gg.wzrds.tools.commands.suggestions.CustomItemSuggestionProvider;
import gg.wzrds.tools.items.Ascendant;
import gg.wzrds.tools.items.Magnet;
import gg.wzrds.tools.items.BookOfReach;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.resources.Identifier;
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
                .then(Commands
                    .argument("item", IdentifierArgument.id())
                    .suggests(new CustomItemSuggestionProvider())
                    .executes(ItemsCommand::executeSpawn)
                )
            )
        );
    }

    public static int executeSpawn(CommandContext<CommandSourceStack> context) {
        ServerPlayer serverPlayer = context.getSource().getPlayer();
        if (serverPlayer == null) {
            return 0;
        }
        ServerLevel level = context.getSource().getLevel();
        Identifier id = IdentifierArgument.getId(context, "item");
        ItemStack item = CustomItems.create(level, id);
        if (serverPlayer.getInventory().add(item)) {
            serverPlayer.drop(item, false, Prediction.PREDICTED);
        }
        return 1;
    }
}
