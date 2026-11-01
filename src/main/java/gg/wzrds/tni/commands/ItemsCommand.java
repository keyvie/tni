package gg.wzrds.tni.commands;

import gg.wzrds.tni.commands.suggestions.CustomItemSuggestionProvider;
import gg.wzrds.tni.items.CustomItem;
import gg.wzrds.tni.items.CustomItems;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.Commands;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.IdentifierArgument;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Prediction;
import net.minecraft.world.item.ItemStack;

public final class ItemsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("items")
            .requires(source ->
                Permissions.check(source, "wzrds.tni.items", false)
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
        Identifier id = IdentifierArgument.getId(context, "item");
        CustomItem item = CustomItems.registry.get(id);
        if (item == null) {
            CommandManager.respondWithStringFailure(context, "Couldn't find the item with that ID");
            return 0;
        }
        ItemStack stack = item.createStack();
        if (!serverPlayer.getInventory().add(stack)) {
            serverPlayer.drop(stack, false, Prediction.PREDICTED);
        }
        return 1;
    }
}
