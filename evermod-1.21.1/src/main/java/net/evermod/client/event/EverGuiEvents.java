package net.evermod.client.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class EverGuiEvents {

  private static final String EVER_ID = "evermod:main_overlay";

  @SubscribeEvent
  public static void onOverlayEvent(CustomizeGuiOverlayEvent event) {

    float partialTick = event.getPartialTick();
    int width = event.getGuiGraphics().guiWidth();
    int height = event.getGuiGraphics().guiHeight();

    EverRenderGuiOverlayEvent.Pre preEvent = new EverRenderGuiOverlayEvent.Pre(
        event.getGuiGraphics().pose(), partialTick, EVER_ID, width, height);

    if (!MinecraftForge.EVENT_BUS.post(preEvent)) {
      MinecraftForge.EVENT_BUS
          .post(new EverRenderGuiOverlayEvent.Post(event.getGuiGraphics().pose(),
              partialTick, EVER_ID, width, height));
    }
  }
}
