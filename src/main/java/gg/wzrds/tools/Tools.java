package gg.wzrds.tools;

import gg.wzrds.tools.commands.CommandManager;
import gg.wzrds.tools.config.Config;
import gg.wzrds.tools.enchantments.Excavation;
import gg.wzrds.tools.enchantments.Swiftness;
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
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
