package net.evermod;

import net.evermod.config.ConfigManager;
import net.evermod.logging.EverLogger;
import net.evermod.network.ChannelManager;
import net.minecraftforge.eventbus.api.IEventBus;

public class EverMod {
  public static final EverLogger LOGGER = new EverLogger("EverMod");

  public static void init(String modid, IEventBus modEventBus) {
    if (modid == null || modid.isEmpty()) {
      throw new IllegalArgumentException("The Mod ID cannot be null or empty.");
    }

    LOGGER.info("Cargando modulos del framework EverMod...");
    ChannelManager.autoRegister(modid);
    ConfigManager.init(modid, modEventBus);
  }
}
