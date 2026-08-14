package net.evermod.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.evermod.client.gui.render.GradientStyle;
import net.evermod.client.gui.render.GradientStyle.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Main public-facing graphics wrapper.
 * Provides high-level layout utilities, matrix manipulations, and convenience methods.
 *
 * @author Wipodev
 */
public class EverGraphics extends AbstractEverGraphics {

  public EverGraphics(PoseStack poseStack) {
    super(poseStack);
  }

  public static EverGraphics of(PoseStack poseStack) {
    return new EverGraphics(poseStack);
  }

  public void drawRect(int x, int y, int width, int height, int backgroundColor) {
    drawRect(x, y, width, height, backgroundColor, null, null);
  }

  public void drawRect(int x, int y, int width, int height, int backgroundColor, int borderColor) {
    drawRect(x, y, width, height, backgroundColor, Border.DEFAULT, BorderColor.all(borderColor));
  }

  public void drawRect(int x, int y, int width, int height, int backgroundColor, Border border,
      BorderColor borderColor) {
    InnerBounds bounds = InnerBounds.of(border, x, y, width, height);

    fill(bounds.innerX(), bounds.innerY(), bounds.innerX2(), bounds.innerY2(),
        GradientStyle.solid(backgroundColor));

    if (border != null && borderColor != null) {
      fillBorder(x, y, width, height, border, borderColor);
    }
  }

  public void drawGradientRect(int x, int y, int width, int height, int colorFrom, int colorTo) {
    drawGradientRect(x, y, width, height, colorFrom, colorTo, null);
  }

  public void drawGradientRect(int x, int y, int width, int height, int colorFrom, int colorTo,
      Direction direction) {
    drawGradientRect(x, y, width, height, colorFrom, colorTo, direction, null, null);
  }

  public void drawGradientRect(int x, int y, int width, int height, GradientStyle style) {
    drawGradientRect(x, y, width, height, style, null, null);
  }

  public void drawGradientRect(int x, int y, int width, int height, int colorFrom, int colorTo,
      Direction direction, Border border, BorderColor borderColor) {
    Direction d = direction != null ? direction : Direction.VERTICAL;
    drawGradientRect(x, y, width, height, GradientStyle.gradient(colorFrom, colorTo, d), border,
        borderColor);
  }

  public void drawGradientRect(int x, int y, int width, int height, GradientStyle style,
      Border border, BorderColor borderColor) {
    InnerBounds bounds = InnerBounds.of(border, x, y, width, height);

    fill(bounds.innerX(), bounds.innerY(), bounds.innerX2(), bounds.innerY2(), style);

    if (border != null && borderColor != null) {
      fillBorder(x, y, width, height, border, borderColor);
    }
  }

  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height) {
    drawTexture(texture, x, y, width, height, 1.0F, 1.0F, 1.0F, 1.0F);
  }

  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height,
      Border border, BorderColor borderColor) {
    drawTexture(texture, x, y, width, height, 0.0F, 0.0F, width, height, width, height, 1.0F, 1.0F,
        1.0F, 1.0F, border, borderColor);
  }

  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height,
      float red, float green, float blue, float alpha) {
    drawTexture(texture, x, y, width, height, 0.0F, 0.0F, width, height, width, height, red, green,
        blue, alpha, null, null);
  }

  public void drawTexture(ResourceLocation texture, int x, int y, float uOffset, float vOffset,
      int width, int height) {
    drawTexture(texture, x, y, width, height, uOffset, vOffset, width, height, 256, 256, 1.0F, 1.0F,
        1.0F, 1.0F, null, null);
  }

  public void drawTexture(ResourceLocation texture, int x, int y, float uOffset, float vOffset,
      int width, int height,
      float red, float green, float blue, float alpha) {
    drawTexture(texture, x, y, width, height, uOffset, vOffset, width, height, 256, 256, red, green,
        blue, alpha, null, null);
  }

  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height,
      float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
    drawTexture(texture, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth,
        textureHeight, 1.0F, 1.0F, 1.0F, 1.0F, null, null);
  }

  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height,
      float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight,
      float red, float green, float blue, float alpha) {
    drawTexture(texture, x, y, width, height, uOffset, vOffset, uWidth, vHeight, textureWidth,
        textureHeight, red, green, blue, alpha, null, null);
  }

  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height,
      float uOffset, float vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight,
      float red, float green, float blue, float alpha, Border border, BorderColor borderColor) {

    InnerBounds bounds = InnerBounds.of(border, x, y, width, height);

    float minU = uOffset / (float) textureWidth;
    float maxU = (uOffset + (float) uWidth) / (float) textureWidth;
    float minV = vOffset / (float) textureHeight;
    float maxV = (vOffset + (float) vHeight) / (float) textureHeight;

    blit(texture,
        (float) bounds.innerX(), (float) bounds.innerY(),
        (float) bounds.innerX2(), (float) bounds.innerY2(),
        minU, maxU, minV, maxV,
        red, green, blue, alpha);

    if (border != null && borderColor != null) {
      fillBorder(x, y, width, height, border, borderColor);
    }
  }

  public void drawCenteredString(String text, int x, int y, int color, boolean shadow) {
    int textWidth = this.font.width(text);
    drawString(text, x - textWidth / 2, y, color, shadow);
  }

  public void drawCenteredString(Component component, int x, int y, int color, boolean shadow) {
    int textWidth = this.font.width(component);
    drawString(component, x - textWidth / 2, y, color, shadow);
  }
}
