package gg.wzrds.tools.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.tree.CommandNode;
import gg.wzrds.tools.Permissions;
import gg.wzrds.tools.Tools;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CommandDispatcher.class)
public class CommandDispatcherMixin {
	@Redirect(method = "parseNodes", at = @At(value = "INVOKE", target = "Lcom/mojang/brigadier/tree/CommandNode;canUse(Ljava/lang/Object;)Z"))
	private boolean canUse(
		CommandNode<CommandSourceStack> childNode,
		Object source,
		CommandNode<CommandSourceStack> node,
		StringReader stringReader,
		CommandContextBuilder<CommandSourceStack> contextSoFar
	) {
		if (!(source instanceof CommandSourceStack commandSource)) {
			return false;
		}
		if (childNode.canUse(commandSource)) {
			return true;
		}
		String[] args = Permissions.splitNodes(stringReader.getString());
		if (args.length == 0) {
			return false;
		}
		ServerPlayer serverPlayer = commandSource.getPlayer();
		if (!(Permissions.hasAccess(serverPlayer, args))) {
			return false;
		}
		return true;
	}
}