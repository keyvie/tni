package gg.wzrds.tni;

import gg.wzrds.tni.commands.CommandManager;
import gg.wzrds.tni.config.Config;
import gg.wzrds.tni.enchantments.Excavation;
import gg.wzrds.tni.enchantments.InteractionReach;
import gg.wzrds.tni.enchantments.Swiftness;
import gg.wzrds.tni.items.CustomItems;
import gg.wzrds.tni.recipes.BookOfReachRecipe;
import gg.wzrds.tni.recipes.MagnetRecipe;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.resources.Identifier;

import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ToolsAndItems implements ModInitializer {
	public static final String MOD_ID = "tni";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static MinecraftServer server;

	public static MinecraftServer getServer() {
		return server;
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> ToolsAndItems.server = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(_ -> ToolsAndItems.server = null);

		// Load da config
		Config.load();

		// Setup for the permission system
		Permissions.register();

		// Loads all commands
		CommandManager.register();

		// Enchantments
		Excavation.register();
		InteractionReach.register();
		Swiftness.register();

		// Items
		CustomItems.register();

		// Recipes
		BookOfReachRecipe.register();
		MagnetRecipe.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static boolean owned(Identifier id) { return id.getNamespace().equals(MOD_ID); }
}
