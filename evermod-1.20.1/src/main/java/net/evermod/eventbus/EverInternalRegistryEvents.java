package net.evermod.eventbus;

import net.evermod.world.item.EverItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EverInternalRegistryEvents {

  @SubscribeEvent
  public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
    EverItem.TAB_MAP.forEach((item, tab) -> {
      if (event.getTabKey().equals(tab.getVanillaTab())) {
        event.accept(item);
      }
    });
  }
}
