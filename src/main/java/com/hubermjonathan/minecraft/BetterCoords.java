package com.hubermjonathan.minecraft;

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

	@Override
	public void onInitializeClient() {
		LOGGER.info("<<<<< BetterCoords started >>>>>");

		this.registerKeyBind();
	}

	private void registerKeyBind() {
		KeyBinding toggleOverlayKey = new KeyBinding(
				"key.bettercoords.toggle_overlay",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_M,
				"category.bettercoords.overlay"
		);

		KeyBinding toggleOverlayKeyBinding = KeyBindingHelper.registerKeyBinding(toggleOverlayKey);

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.player == null) return;

			if (toggleOverlayKeyBinding.wasPressed()) {
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
}
