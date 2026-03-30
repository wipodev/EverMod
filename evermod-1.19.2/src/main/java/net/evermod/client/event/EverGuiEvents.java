package net.evermod.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EverGuiEvents {

  @SubscribeEvent
  public static void onVanillaPre(RenderGuiOverlayEvent.Pre event) {
    EverRenderGuiOverlayEvent.Pre everEvent = new EverRenderGuiOverlayEvent.Pre(
        event.getPoseStack(), event.getPartialTick(), event.getOverlay().id().toString(),
        event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight());
    if (MinecraftForge.EVENT_BUS.post(everEvent))
      event.setCanceled(true);
  }

  @SubscribeEvent
  public static void onVanillaPost(RenderGuiOverlayEvent.Post event) {
    MinecraftForge.EVENT_BUS.post(new EverRenderGuiOverlayEvent.Post(event.getPoseStack(),
        event.getPartialTick(), event.getOverlay().id().toString(),
        event.getWindow().getGuiScaledWidth(), event.getWindow().getGuiScaledHeight()));
  }
}
