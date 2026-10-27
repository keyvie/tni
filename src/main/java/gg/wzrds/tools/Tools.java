package gg.wzrds.tools;

import gg.wzrds.tools.commands.CommandManager;
import gg.wzrds.tools.config.Config;
import gg.wzrds.tools.enchantments.Excavation;
import gg.wzrds.tools.enchantments.Swiftness;
import gg.wzrds.tools.items.Ascendant;
import gg.wzrds.tools.items.BookOfReach;
import gg.wzrds.tools.items.Magnet;
import gg.wzrds.tools.recipes.BookOfReachRecipe;
import gg.wzrds.tools.recipes.MagnetRecipe;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tools implements ModInitializer {
	public static final String MOD_ID = "tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Load da config
		Config.load();

		// Setup for the permission system
		Permissions.register();

		// Loads all commands
		CommandManager.register();

		// Enchantments
		Excavation.register();
		Swiftness.register();

		// Items
		Ascendant.register();
		BookOfReach.register();
		Magnet.register();

		// Recipes
		BookOfReachRecipe.register();
		MagnetRecipe.register();
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
	public static boolean owned(Identifier id) { return id.getNamespace().equals(MOD_ID); }
}
