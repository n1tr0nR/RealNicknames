package com.nitron.nickname;

import com.nitron.nickname.commands.NicknameCommand;
import com.nitron.nickname.config.Config;
import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.GameRules;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class RealNickname implements ModInitializer {
	public static final String MOD_ID = "nickname";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	/*public static final GameRules.Key<GameRules.BooleanRule> ALLOW_NAME_COLOR =
			GameRuleRegistry.register("allowNameColor", GameRules.Category.PLAYER, GameRuleFactory.createBooleanRule(false));*/

	@Override
	public void onInitialize() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, registrationEnvironment) -> {
			NicknameCommand.register(dispatcher);
		});
		MidnightConfig.init(MOD_ID, Config.class);
	}

	private static final Pattern HEX_PATTERN = Pattern.compile("^#?[a-fA-F0-9]{6}$");

	public static boolean isValidHex(String str) {
		return str != null && HEX_PATTERN.matcher(str).matches();
	}

	public static int convertToHex(String hexString) {
		hexString = hexString.replace("#", ""); // Remove # if present
		return Integer.parseInt(hexString, 16); // Convert to an int
	}
}