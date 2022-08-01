package com.hubermjonathan.minecraft;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "bettercoords")
public class BetterCoordsConfig implements ConfigData {
    @ConfigEntry.Gui.TransitiveObject
    public OverlayElements overlayElements = new OverlayElements();

    public static class OverlayElements {
        public boolean toggleMainOverlay = true;
        public boolean toggleOresOverlay = false;
    }
}
