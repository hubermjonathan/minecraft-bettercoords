package com.hubermjonathan.minecraft.mixin;

import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface CurrentFpsAccessor {
    @Accessor("currentFps")
    int getCurrentFps();
}
