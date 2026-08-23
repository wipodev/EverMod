package net.evermod.client.graphics.font;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.network.chat.Component;

/**
 * Abstract base implementation of {@link EverFont} that handles common font operations.
 * Subclasses only need to bridge version-specific drawInBatch calls.
 *
 * @author Wipodev
 */
public abstract class EverFont {

  protected final Font font = Minecraft.getInstance().font;

  /**
   * Internal bridge method to execute version-specific drawInBatch for plain text.
   */
  protected abstract void renderBatch(String text, float x, float y, int color, boolean shadow,
      PoseStack pose, BufferSource bufferSource);

  /**
   * Internal bridge method to execute version-specific drawInBatch for formatted sequences.
   */
  protected abstract void renderBatch(Component text, float x, float y, int color, boolean shadow,
      PoseStack pose, BufferSource bufferSource);

  /**
   * Draws a plain string at the specified coordinates.
   *
   * @param poseStack matrix stack for rendering transformations
   * @param text string content to render
   * @param x X screen position
   * @param y Y screen position
   * @param color ARGB color code
   * @param shadow whether to render text shadow
   */
  public void drawString(
      PoseStack poseStack, String text, float x, float y, int color, boolean shadow) {
    if (text == null || text.isEmpty()) {
      return;
    }
    RenderSystem.disableDepthTest();
    BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

    this.renderBatch(text, x, y, color, shadow, poseStack, bufferSource);

    bufferSource.endBatch();
  }

  /**
   * Draws a formatted Component at the specified coordinates.
   *
   * @param poseStack matrix stack for rendering transformations
   * @param component text component to render
   * @param x X screen position
   * @param y Y screen position
   * @param color ARGB color code
   * @param shadow whether to render text shadow
   */
  public void drawString(
      PoseStack poseStack, Component component, float x, float y, int color, boolean shadow) {
    if (component == null) {
      return;
    }
    RenderSystem.disableDepthTest();
    BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

    this.renderBatch(component, x, y, color, shadow, poseStack, bufferSource);

    bufferSource.endBatch();
  }

  /**
   * Calculates the width in pixels of a given plain string.
   *
   * @param text input string
   * @return pixel width of the rendered string
   */
  public int width(String text) {
    return text == null ? 0 : this.font.width(text);
  }

  /**
   * Calculates the width in pixels of a formatted Component.
   *
   * @param component input text component
   * @return pixel width of the rendered component
   */
  public int width(Component component) {
    return component == null ? 0 : this.font.width(component);
  }

  /**
   * Gets the line height of the font in pixels (typically 9 in vanilla Minecraft).
   *
   * @return font height in pixels
   */
  public int fontHeight() {
    return this.font.lineHeight;
  }

  /**
   * Truncates a string so that its total rendered width does not exceed the maximum allowed width.
   *
   * @param text target string to trim
   * @param maxWidth maximum width in pixels
   * @return substring fitting within specified pixel constraint
   */
  public String plainSubstrByWidth(String text, int maxWidth) {
    return this.font.plainSubstrByWidth(text, maxWidth);
  }

  /**
   * Truncates a string so that its total rendered width does not exceed the maximum allowed width.
   *
   * @param text target string to trim
   * @param maxWidth maximum width in pixels
   * @param reverse if true, trims from right-to-left
   * @return substring fitting within specified pixel constraint
   */
  public String plainSubstrByWidth(String text, int maxWidth, boolean reverse) {
    return this.font.plainSubstrByWidth(text, maxWidth, reverse);
  }
}
