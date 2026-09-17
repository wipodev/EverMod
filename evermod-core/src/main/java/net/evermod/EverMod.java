package net.evermod;

import net.evermod.config.ConfigManager;
import net.evermod.context.IEverContext;
import net.evermod.logging.EverLogger;
import net.evermod.network.ChannelManager;
import net.evermod.network.NetworkAdapter;
import net.evermod.network.annotations.EverPacketDirection;
import net.evermod.network.packets.PlaySoundPacket;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Main initialization class for the EverMod Framework.
 * 
 * @author Wipodev
 */
@Mod(EverMod.EVER_ID)
public class EverMod {

  public static final String EVER_ID = "evermod";
  public static final String AUTHOR = "Wipodev";
  public static final String FRAMEWORK_NAME = "EverMod Framework";
  public static final String VERSION = "0.1.0";
  public static final EverLogger LOGGER = new EverLogger(FRAMEWORK_NAME);

  /**
   * Main constructor executed directly by Forge during mod discovery.
   */
  public EverMod(FMLJavaModLoadingContext context) {
    LOGGER.info("{} v{} by {} has been loaded as a library mod.", FRAMEWORK_NAME, VERSION, AUTHOR);

    ChannelManager.init(new NetworkAdapter());
    ChannelManager.register(EVER_ID);
    ChannelManager.registerPacket(EVER_ID, PlaySoundPacket.class,
        buffer -> PlaySoundPacket.decode(buffer), EverPacketDirection.TO_CLIENT);
  }

  /**
   * Initializes the EverMod abstraction framework modules for the target mod.
   * 
   * @param modid The unique identifier of the mod leveraging the framework.
   * @param modEventBus The mod event bus provided by Forge.
   */
  public static void init(String modid, IEverContext context) {
    if (modid == null || modid.isEmpty()) {
      throw new IllegalArgumentException("The Mod ID cannot be null or empty.");
    }

    LOGGER.info("Initializing {} v{} by {} for target mod: {}", FRAMEWORK_NAME, VERSION, AUTHOR,
        modid);
    ChannelManager.autoRegister(modid);
    ConfigManager.init(modid, context);
  }
}
