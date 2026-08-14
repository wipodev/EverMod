package net.evermod;

import net.evermod.client.input.ClientInputHandler;
import net.evermod.config.ConfigManager;
import net.evermod.logging.EverLogger;
import net.evermod.network.ChannelManager;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Main initialization class for the EverMod Framework.
 * 
 * <p><strong>Intellectual Property & Trademark Notice:</strong><br>
 * This source code is injected into the target mod's SourceSets under the LGPLv3 license.
 * The "EverMod" name and framework identity are the exclusive property of <code>Wipodev</code>.
 * Source code injection does not grant rights to use the "EverMod" trademark for third-party mod branding.
 * </p>
 * 
 * @author Wipodev
 */
public class EverMod {

  public static final String EVER_ID = "evermod";
  public static final String AUTHOR = "Wipodev";
  public static final String FRAMEWORK_NAME = "EverMod Framework";
  public static final String VERSION = "2.0.0";
  public static final EverLogger LOGGER = new EverLogger("EverMod");

  /**
  * Private constructor to prevent direct instantiation of utility class.
  */
  private EverMod() {
    throw new UnsupportedOperationException(
        "EverMod is an initialization utility class and cannot be instantiated.");
  }

  /**
   * Initializes the EverMod abstraction framework modules for the target mod.
   * 
   * @param modid The unique identifier of the mod leveraging the framework.
   * @param modEventBus The mod event bus provided by Forge.
   */
  public static void init(String modid, IEventBus modEventBus) {
    if (modid == null || modid.isEmpty()) {
      throw new IllegalArgumentException("The Mod ID cannot be null or empty.");
    }

    LOGGER.info("Initializing {} v{} by {} for target mod: {}", FRAMEWORK_NAME, VERSION, AUTHOR,
        modid);
    ChannelManager.autoRegister(modid);
    ConfigManager.init(modid, modEventBus);
    ClientInputHandler.register(modEventBus);
  }
}
