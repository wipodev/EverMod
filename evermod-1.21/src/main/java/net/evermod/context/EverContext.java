package net.evermod.context;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Version-specific context implementation for Forge 1.21.
 * 
 * @author Wipodev
 */
public class EverContext implements IEverContext {

  private final FMLJavaModLoadingContext context;

  public EverContext(FMLJavaModLoadingContext context) {
    this.context = context;
  }

  @Override
  public IEventBus getEventBus() {
    return context.getModEventBus();
  }

  @Override
  public void registerConfig(ForgeConfigSpec spec, String fileName) {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, spec, fileName);
  }
}
