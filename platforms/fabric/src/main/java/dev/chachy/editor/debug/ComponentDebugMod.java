package dev.chachy.editor.debug;

import dev.chachy.editor.debug.screen.DebugScreen;
import dev.chachy.editor.debug.utils.ScreenUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.util.InputUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentDebugMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("elementa-component-editor");

    @Override
    public void onInitialize() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
                dispatcher.register(ClientCommandManager.literal("component_debug").executes((context) -> {
                    ScreenUtils.INSTANCE.apply(new DebugScreen());
                    return 0;
                }));
            }
        );
    }
}
