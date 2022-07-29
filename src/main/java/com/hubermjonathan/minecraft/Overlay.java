package com.hubermjonathan.minecraft;

import com.hubermjonathan.minecraft.mixin.CurrentFpsAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.biome.Biome;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class Overlay {
    private final MinecraftClient minecraftClient;
    private final TextRenderer textRenderer;
    private ClientPlayerEntity clientPlayerEntity;

    public Overlay(MinecraftClient minecraftClient) {
        this.minecraftClient = minecraftClient;
        this.textRenderer = minecraftClient.textRenderer;
    }

    public void draw(MatrixStack matrixStack) {
        this.clientPlayerEntity = this.minecraftClient.player;

        List<String> gameInfo = getGameInfo();
        int top = 4;

        for (String line : gameInfo) {
            if (line == null) continue;

            this.textRenderer.drawWithShadow(
                    matrixStack,
                    line,
                    4,
                    top,
                    0x00E0E0E0
            );

            top += this.textRenderer.fontHeight + 2;
        }
    }

    private List<String> getGameInfo() {
        List<String> gameInfo = new ArrayList<>();

        gameInfo.add(getPlayerCoordinates());
        gameInfo.add(getBiome());
        gameInfo.add(getFPS());

        return gameInfo;
    }

    private String getPlayerCoordinates() {
        return String.format(
                "%.0f / %.0f / %.0f (%s %s)",
                this.clientPlayerEntity.getX(),
                this.clientPlayerEntity.getY(),
                this.clientPlayerEntity.getZ(),
                StringUtils.capitalize(this.clientPlayerEntity.getHorizontalFacing().asString()),
                getOffset(this.clientPlayerEntity.getHorizontalFacing())
        );
    }

    private String getOffset(Direction facing) {
        String offset = "";

        if (facing.getOffsetX() > 0) {
            offset += "+X";
        } else if (facing.getOffsetX() < 0) {
            offset += "-X";
        }

        if (facing.getOffsetZ() > 0) {
            offset += " +Z";
        } else if (facing.getOffsetZ() < 0) {
            offset += " -Z";
        }

        return offset.trim();
    }

    private String getBiome() {
        if (this.minecraftClient.world != null) {
            RegistryEntry<Biome> biome = this.minecraftClient.world.getBiome(this.clientPlayerEntity.getBlockPos());
            Identifier biomeIdentifier = this.minecraftClient.world.getRegistryManager().get(Registry.BIOME_KEY).getId(biome.value());

            if (biomeIdentifier != null) {
                return Text.translatable("biome." + biomeIdentifier.getNamespace() + "." + biomeIdentifier.getPath()).getString();
            }

            return null;
        }

        return null;
    }

    private String getFPS() {
        return String.format(
                "%d fps",
                ((CurrentFpsAccessor) MinecraftClient.getInstance()).getCurrentFps()
        );
    }
}
