package gg.wzrds.tools.mixin;

import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Commands.class)
public class CommandsMixin {

    // I've not figured out a way to get this working yet sadly
    @Redirect(method = "fillUsableCommands", at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/tree/CommandNode;canUse(Ljava/lang/Object;)Z"
    ))
    private static boolean hasAccess(CommandNode<CommandSourceStack> child, Object commandFilter) {
        return true;
    }

    // My thought is to inject something at the top taht keeps track of the commands somehow
    @Inject(method = "fillUsableCommands", at = @At("HEAD"))
    private static void idk() {}
}