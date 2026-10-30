package gg.wzrds.tools;

import gg.wzrds.tools.commands.CommandManager;
import gg.wzrds.tools.config.Config;
import gg.wzrds.tools.enchantments.Excavation;
import gg.wzrds.tools.enchantments.Swiftness;
import gg.wzrds.tools.items.AscendantBoots;
import gg.wzrds.tools.items.ItemTick;
import gg.wzrds.tools.recipes.BookOfReachRecipe;
import gg.wzrds.tools.recipes.MagnetRecipe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;

import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tools implements ModInitializer {
	public static final String MOD_ID = "tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static MinecraftServer server;

	public static MinecraftServer getServer() {
		return server;
	}

	@Override
	public void onInitialize() {
		ServerLifecycleEvents.SERVER_STARTED.register(server -> Tools.server = server);
		ServerLifecycleEvents.SERVER_STOPPED.register(_ -> Tools.server = null);

		// Load da config
		Config.load();

		// Setup for the permission system
		Permissions.register();

		// Loads all commands
		CommandManager.register();

		// Enchantments
		Excavation.register();
		Swiftness.register();

		// Item Ticks
		ItemTick.register();

		// Recipes
		BookOfReachRecipe.register();
		MagnetRecipe.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static boolean owned(Identifier id) { return id.getNamespace().equals(MOD_ID); }
}
