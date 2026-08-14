package net.evermod.eventbus;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Helper class for accessing the Forge mod event bus across different Minecraft versions.
 */
public class EverBus {

  /**
   * Retrieves the mod event bus for the current mod context.
   * Suppresses both removal and unused suppression warnings across legacy and modern Forge toolchains.
   *
   * @return The {@link IEventBus} instance for mod event registration.
   */
  @SuppressWarnings({"removal", "all"})
  public static IEventBus getBus() {
    return FMLJavaModLoadingContext.get().getModEventBus();
  }
}
