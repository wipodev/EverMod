package net.evermod.eventbus;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class EverBus {

  public static IEventBus getBus() {
    return FMLJavaModLoadingContext.get().getModEventBus();
  }
}
