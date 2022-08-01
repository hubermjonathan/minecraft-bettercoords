package com.hubermjonathan.minecraft;

import com.hubermjonathan.minecraft.mixin.CurrentFpsAccessor;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
    private BetterCoordsConfig config;
    private final ItemRenderer itemRenderer;

    public Overlay(MinecraftClient minecraftClient) {
        this.minecraftClient = minecraftClient;
        this.textRenderer = minecraftClient.textRenderer;
        this.config = AutoConfig.getConfigHolder(BetterCoordsConfig.class).getConfig();
        this.itemRenderer = minecraftClient.getItemRenderer();
    }

    public void draw(MatrixStack matrixStack) {
        if (this.config.overlayElements.toggleMainOverlay) {
            drawMainOverlay(matrixStack);
        }

        if (this.config.overlayElements.toggleOresOverlay) {
            drawOreOverlay(matrixStack);
        }
    }

    private void drawMainOverlay(MatrixStack matrixStack) {
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

    private void drawOreOverlay(MatrixStack matrixStack) {
        final double yCoord = this.clientPlayerEntity.getY();
        int top = 4;

        if (yCoord >= 0 && yCoord <= 320) {
            drawOre(yCoord, 95, new ItemStack(Items.COAL), matrixStack, top);
            top += this.textRenderer.fontHeight + 6;
        }

        if (yCoord >= -16 && yCoord <= 112) {
            drawOre(yCoord, 48, new ItemStack(Items.RAW_COPPER), matrixStack, top);
            top += this.textRenderer.fontHeight + 6;
        }

        if (yCoord >= -64 && yCoord <= 64) {
            drawOre(yCoord, -1, new ItemStack(Items.LAPIS_LAZULI), matrixStack, top);
            top += this.textRenderer.fontHeight + 6;
        }

        if (yCoord >= -64 && yCoord <= 320) {
            drawOre(yCoord, 15, new ItemStack(Items.RAW_IRON), matrixStack, top);
            top += this.textRenderer.fontHeight + 6;
        }

        if (yCoord >= -64 && yCoord <= 256) {
            drawOre(yCoord, 32, new ItemStack(Items.RAW_GOLD), matrixStack, top);
            top += this.textRenderer.fontHeight + 6;
        }

        if (yCoord >= -64 && yCoord <= 16) {
            drawOre(yCoord, -59, new ItemStack(Items.REDSTONE), matrixStack, top);
            top += this.textRenderer.fontHeight + 6;
        }

        if (yCoord >= -64 && yCoord <= 16) {
            drawOre(yCoord, -59, new ItemStack(Items.DIAMOND), matrixStack, top);
            top += this.textRenderer.fontHeight + 6;
        }

        // TODO: add emeralds to ore overlay
//        if ((getBiome() != null
//                || getBiome().equals("Meadow")
//                || getBiome().equals("Grove")
//                || getBiome().equals("Snowy Slopes")
//                || getBiome().equals("Jagged Peaks")
//                || getBiome().equals("Frozen Peaks")
//                || getBiome().equals("Stony Peaks")
//                || getBiome().equals("Windswept Hills")
//                || getBiome().equals("Windswept Forest")
//                || getBiome().equals("Windswept Gravelly Hills")) && yCoord >= -16 && yCoord <= 320) {
//            drawOre(yCoord, 236, new ItemStack(Items.EMERALD), matrixStack, top);
//        }
    }

    private void drawOre(double yCoord, int idealLevel, ItemStack item, MatrixStack matrixStack, int top) {
        this.itemRenderer.renderInGuiWithOverrides(
                item,
                this.minecraftClient.getWindow().getScaledWidth() - 20,
                top);

        if (yCoord < idealLevel) {
            this.textRenderer.drawWithShadow(
                    matrixStack,
                    "↑",
                    this.minecraftClient.getWindow().getScaledWidth() - 28,
                    top + 4,
                    0x00E0E0E0
            );
        } else if (yCoord > idealLevel) {
            this.textRenderer.drawWithShadow(
                    matrixStack,
                    "↓",
                    this.minecraftClient.getWindow().getScaledWidth() - 28,
                    top + 4,
                    0x00E0E0E0
            );
        } else {
            this.textRenderer.drawWithShadow(
                    matrixStack,
                    "-",
                    this.minecraftClient.getWindow().getScaledWidth() - 28,
                    top + 4,
                    0x00E0E0E0
            );
        }
    }
}
