package net.evermod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;

public class EverGui {

  public static void blit(Object poseStackObj, ResourceLocation texture, int texW, int texH,
      int screenW, int screenH) {
    PoseStack poseStack = (PoseStack) poseStackObj;
    RenderSystem.setShaderTexture(0, texture);
    GuiComponent.blit(poseStack, 0, 0, screenW, screenH, 0.0F, 0.0F, texW, texH, texW, texH);
  }
}
