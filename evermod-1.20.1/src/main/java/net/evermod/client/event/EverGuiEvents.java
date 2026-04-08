package net.evermod.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EverGuiEvents {

  private static final String EVER_ID = "evermod:main_overlay";

  @SubscribeEvent
  public static void onVanillaPre(RenderGuiOverlayEvent.Pre event) {
    if (event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) {
      EverRenderGuiOverlayEvent.Pre everEvent =
          new EverRenderGuiOverlayEvent.Pre(event.getGuiGraphics(), event.getPartialTick(), EVER_ID,
              event.getGuiGraphics().guiWidth(), event.getGuiGraphics().guiHeight());
      if (MinecraftForge.EVENT_BUS.post(everEvent)) {
        event.setCanceled(true);
      }
    }
  }

  @SubscribeEvent
  public static void onVanillaPost(RenderGuiOverlayEvent.Post event) {
    if (event.getOverlay().id().equals(VanillaGuiOverlay.HOTBAR.id())) {
      MinecraftForge.EVENT_BUS
          .post(new EverRenderGuiOverlayEvent.Post(event.getGuiGraphics(), event.getPartialTick(),
              EVER_ID, event.getGuiGraphics().guiWidth(), event.getGuiGraphics().guiHeight()));
    }
  }
}
