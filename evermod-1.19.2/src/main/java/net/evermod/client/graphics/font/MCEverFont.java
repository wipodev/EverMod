package net.evermod.client.graphics.font;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

/**
 * Minecraft implementation of the {@link EverFont} interface.
 *
 * @author Wipodev
 */
public class MCEverFont implements EverFont {

  private final Font font = Minecraft.getInstance().font;

  @Override
  public void drawString(PoseStack poseStack, String text, float x, float y, int color,
      boolean shadow) {
    if (text == null || text.isEmpty()) {
      return;
    }
    RenderSystem.disableDepthTest();
    MultiBufferSource.BufferSource bufferSource =
        Minecraft.getInstance().renderBuffers().bufferSource();
    this.font.drawInBatch(text, x, y, color, shadow, poseStack.last().pose(), bufferSource, false,
        0, 15728880);
    bufferSource.endBatch();
  }

  @Override
  public void drawString(PoseStack poseStack, Component component, float x, float y, int color,
      boolean shadow) {
    if (component == null) {
      return;
    }
    RenderSystem.disableDepthTest();
    MultiBufferSource.BufferSource bufferSource =
        Minecraft.getInstance().renderBuffers().bufferSource();
    this.font.drawInBatch(component.getVisualOrderText(), x, y, color, shadow,
        poseStack.last().pose(), bufferSource, false, 0, 15728880);
    bufferSource.endBatch();
  }

  @Override
  public int width(String text) {
    return text == null ? 0 : this.font.width(text);
  }

  @Override
  public int width(Component component) {
    return component == null ? 0 : this.font.width(component);
  }

  @Override
  public int fontHeight() {
    return this.font.lineHeight;
  }

  @Override
  public String plainSubstrByWidth(String text, int maxWidth) {
    return this.font.plainSubstrByWidth(text, maxWidth);
  }

  @Override
  public String plainSubstrByWidth(String text, int maxWidth, boolean reverse) {
    return this.font.plainSubstrByWidth(text, maxWidth, reverse);
  }
}
