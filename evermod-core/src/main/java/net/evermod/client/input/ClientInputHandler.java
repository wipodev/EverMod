package net.evermod.client.input;

import com.mojang.blaze3d.platform.InputConstants;
import net.evermod.client.gui.screens.EverScreenDemo;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Handles client-side key binding registrations and key press events.
 */
public class ClientInputHandler {

  /** Key mapping for opening the EverScreen demo interface */
  public static final KeyMapping DEMO_KEY_MAPPING = new KeyMapping(
      "key.evermod.open_demo",
      KeyConflictContext.IN_GAME,
      InputConstants.Type.KEYSYM,
      GLFW.GLFW_KEY_K,
      "key.categories.evermod");

  /**
   * Initializes and registers event listeners for client input handling.
   *
   * @param modEventBus The mod event bus from FMLJavaModLoadingContext.
   */
  public static void register(IEventBus modEventBus) {
    // Register key mapping with the mod event bus
    modEventBus.addListener(ClientInputHandler::onRegisterKeyMappings);

    // Register tick event handler to the forge event bus
    MinecraftForge.EVENT_BUS.register(new ForgeEvents());
  }

  /**
   * Called by Forge to register custom key mappings.
   *
   * @param event The key mappings registration event.
   */
  private static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
    event.register(DEMO_KEY_MAPPING);
  }

  /**
   * Internal listener class for Forge runtime events.
   */
  private static class ForgeEvents {

    /**
     * Listens to client ticks to check if the registered key is consumed.
     *
     * @param event The client tick event.
     */
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
      // Check only during the END phase to avoid processing twice per tick
      if (event.phase != TickEvent.Phase.END) {
        return;
      }

      Minecraft mc = Minecraft.getInstance();

      // Ensure player and world exist before opening GUI
      if (mc.player == null || mc.level == null) {
        return;
      }

      // Consume all pending key presses
      while (DEMO_KEY_MAPPING.consumeClick()) {
        // Open the demo screen
        mc.setScreen(new EverScreenDemo());
      }
    }
  }
}
