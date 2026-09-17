package net.evermod.context;

import java.util.function.Supplier;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.IExtensionPoint;

/**
 * Interface defining version-agnostic mod context operations.
 * 
 * @author Wipodev
 */
public interface IEverContext {

  /**
   * Retrieves the mod event bus.
   * 
   * @return The mod event bus instance.
   */
  IEventBus getEventBus();

  /**
   * Registers a configuration specification into the platform runtime.
   * 
   * @param spec Generated ForgeConfigSpec instance.
   * @param fileName Target config file name.
   */
  void registerConfig(ForgeConfigSpec spec, String fileName);

  <T extends Record & IExtensionPoint<T>> void registerExtensionPoint(
      Class<? extends IExtensionPoint<T>> point, Supplier<T> extension);
}
