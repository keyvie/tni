
package gg.wzrds.tools.commands.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import gg.wzrds.tools.config.Config;
import net.minecraft.commands.CommandSourceStack;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PermissionGroupSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        Set<String> groups = Config.get().permissions.groups.keySet();
        for (String group : groups) {
            builder.suggest(group);
        }
        return builder.buildFuture();
    }
}
