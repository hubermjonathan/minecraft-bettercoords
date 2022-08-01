package com.hubermjonathan.minecraft;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterCoords implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("bettercoords");
	private ConfigHolder<BetterCoordsConfig> configHolder;

	@Override
	public void onInitializeClient() {
		LOGGER.info("<<<<< BetterCoords started >>>>>");

		this.configHolder = AutoConfig.register(BetterCoordsConfig.class, Toml4jConfigSerializer::new);

		this.registerToggleMainOverlayKeyBind();
		this.registerShareCoordinatesKeyBind();
		this.registerToggleOresOverlayKeyBind();
	}

	private void registerToggleMainOverlayKeyBind() {
		KeyBinding toggleMainOverlayKey = new KeyBinding(
				"Toggle Main Overlay",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_N,
				"BetterCoords"
		);

		KeyBinding toggleMainOverlayKeyBinding = KeyBindingHelper.registerKeyBinding(toggleMainOverlayKey);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (toggleMainOverlayKeyBinding.wasPressed()) {
				BetterCoordsConfig config = this.configHolder.getConfig();

				config.overlayElements.toggleMainOverlay = !config.overlayElements.toggleMainOverlay;
				AutoConfig.getConfigHolder(BetterCoordsConfig.class).save();
			}
		});
	}

	private void registerShareCoordinatesKeyBind() {
		KeyBinding shareCoordinatesKey = new KeyBinding(
				"Share Coordinates",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_M,
				"BetterCoords"
		);

		KeyBinding shareCoordinatesKeyBinding = KeyBindingHelper.registerKeyBinding(shareCoordinatesKey);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (shareCoordinatesKeyBinding.wasPressed()) {
				String coordinates = String.format(
						"x: %.0f y: %.0f z: %.0f",
						client.player.getX(),
						client.player.getY(),
						client.player.getZ()
				);

				client.player.sendChatMessage(coordinates, Text.of(coordinates));
			}
		});
	}

	private void registerToggleOresOverlayKeyBind() {
		KeyBinding toggleOresOverlayKey = new KeyBinding(
				"Toggle Ores Overlay",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_B,
				"BetterCoords"
		);

		KeyBinding toggleOresOverlayKeyBinding = KeyBindingHelper.registerKeyBinding(toggleOresOverlayKey);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (toggleOresOverlayKeyBinding.wasPressed()) {
				BetterCoordsConfig config = this.configHolder.getConfig();

				config.overlayElements.toggleOresOverlay = !config.overlayElements.toggleOresOverlay;
				AutoConfig.getConfigHolder(BetterCoordsConfig.class).save();
			}
		});
	}
}
