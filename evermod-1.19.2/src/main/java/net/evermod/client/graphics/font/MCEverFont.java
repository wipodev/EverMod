package net.evermod.client.graphics.font;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.network.chat.Component;

/**
 * Minecraft implementation of the {@link EverFont} interface.
 *
 * @author Wipodev
 */
public class MCEverFont extends EverFont {

  @Override
  protected void renderBatch(String text, float x, float y, int color, boolean shadow,
      PoseStack pose, BufferSource bufferSource) {
    this.font.drawInBatch(
        text, x, y, color, shadow, pose.last().pose(), bufferSource, false, 0, 15728880);
  }

  @Override
  protected void renderBatch(Component text, float x, float y, int color, boolean shadow,
      PoseStack pose, BufferSource bufferSource) {
    this.font.drawInBatch(
        text.getVisualOrderText(), x, y, color, shadow, pose.last().pose(), bufferSource, false, 0,
        15728880);
  }


}
