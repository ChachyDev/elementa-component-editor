package dev.chachy.editor.debug.utils;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ScreenUtils {
    public static ScreenUtils INSTANCE = new ScreenUtils();

    private Screen currentScreen;

    public ScreenUtils() {
        ClientTickEvents.START_CLIENT_TICK.register(this::onStartTick);
    }

    public void apply(Screen screen) {
        this.currentScreen = screen;
    }

    public void onStartTick(MinecraftClient client) {
        if (currentScreen != null) {
            MinecraftClient.getInstance().setScreen(this.currentScreen);
            this.currentScreen = null;
        }
    }
}
