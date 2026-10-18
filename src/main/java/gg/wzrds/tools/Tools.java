package gg.wzrds.tools;

import gg.wzrds.tools.enchantments.Excavation;
import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Tools implements ModInitializer {
	public static final String MOD_ID = "tools";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Loading {}", MOD_ID);
		Commands.init();
		CustomEnchantments.init();
		MobPortalDespawn.init();
		Permissions.init();
		LOGGER.info("Loaded {}", MOD_ID);
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
