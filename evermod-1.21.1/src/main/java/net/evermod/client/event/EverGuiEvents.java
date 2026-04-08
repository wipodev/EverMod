package net.evermod.client.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.evermod.resources.EverLocation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EverGuiEvents {

  private static final String EVER_ID = "evermod:main_overlay";
  // Definimos un ID único para nuestra capa en el registro de Forge
  private static final ResourceLocation EVER_LAYER_ID =
      EverLocation.parse("evermod", "main_overlay");

  @SubscribeEvent
  public static void onOverlayEvent(AddGuiOverlayLayersEvent event) {

    event.getLayeredDraw().addWithCondition(EVER_LAYER_ID,
        (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {

          float partialTick = deltaTracker.getGameTimeDeltaPartialTick(true);
          int width = guiGraphics.guiWidth();
          int height = guiGraphics.guiHeight();

          // Disparamos el PRE
          EverRenderGuiOverlayEvent.Pre preEvent =
              new EverRenderGuiOverlayEvent.Pre(guiGraphics, partialTick, EVER_ID, width, height);

          if (!MinecraftForge.EVENT_BUS.post(preEvent)) {
            // Disparamos el POST
            MinecraftForge.EVENT_BUS.post(new EverRenderGuiOverlayEvent.Post(guiGraphics,
                partialTick, EVER_ID, width, height));
          }
        },
        // Condición: No dibujar si el HUD está oculto
        () -> !Minecraft.getInstance().options.hideGui);
  }
}
