
package gg.wzrds.tni.commands.suggestions;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import gg.wzrds.tni.items.CustomItems;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.Identifier;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CustomItemSuggestionProvider implements SuggestionProvider<CommandSourceStack> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<CommandSourceStack> context, SuggestionsBuilder builder) {
        Set<Identifier> customItems = CustomItems.registry.keySet();
        for (Identifier customItem : customItems) {
            builder.suggest(customItem.toString());
        }
        return builder.buildFuture();
    }
}
