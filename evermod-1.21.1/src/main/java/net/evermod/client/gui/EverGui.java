package net.evermod.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class EverGui {

  public static void blit(Object guiGraphicsObj, ResourceLocation texture, int texW, int texH,
      int screenW, int screenH) {
    GuiGraphics guiGraphics = (GuiGraphics) guiGraphicsObj;
    guiGraphics.blit(texture, 0, 0, screenW, screenH, 0.0F, 0.0F, texW, texH, texW, texH);
  }
}
