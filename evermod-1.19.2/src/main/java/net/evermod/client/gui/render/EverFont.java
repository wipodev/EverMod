package net.evermod.client.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

/**
 *
 * @author Wipodev
 */
public class EverFont implements IEverFont {

  private final Font font = Minecraft.getInstance().font;

  @Override
  public void drawString(PoseStack poseStack, String text, float x, float y, int color,
      boolean shadow) {
    if (text == null || text.isEmpty()) {
      return;
    }
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
