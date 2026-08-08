package net.evermod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Unified graphics context abstraction for EverMod.
 * Extends GuiComponent to gain access to protected rendering methods 
 * such as fillGradient while hiding version-specific breaking changes.
 *
 * @author Wipodev
 */
public class EverGraphics extends GuiComponent {
  private final Object nativeContext;
  private final Font font;

  /**
   * Constructs a new EverGraphics instance wrapping the native graphics object.
   *
   * @param nativeContext Native graphics context (e.g., PoseStack or GuiGraphics).
   */
  public EverGraphics(Object nativeContext) {
    this.nativeContext = nativeContext;
    this.font = Minecraft.getInstance().font;
  }

  /**
   * Gets the underlying raw native graphics object.
   *
   * @return Raw native context instance.
   */
  public Object getNativeContext() {
    return nativeContext;
  }

  /**
   * Retrieves the PoseStack instance from the wrapped context.
   *
   * @return The active PoseStack.
   */
  public PoseStack getPoseStack() {
    if (nativeContext instanceof PoseStack poseStack) {
      return poseStack;
    }
    return (PoseStack) nativeContext;
  }

  /**
   * Draws a solid colored rectangle on screen.
   *
   * @param x1 Start X coordinate.
   * @param y1 Start Y coordinate.
   * @param x2 End X coordinate.
   * @param y2 End Y coordinate.
   * @param color ARGB color value (e.g., 0xFFFFFFFF for solid white).
   */
  public void fill(int x1, int y1, int x2, int y2, int color) {
    fill(getPoseStack(), x1, y1, x2, y2, color);
  }

  /**
   * Draws a rectangle filled with a vertical color gradient.
   * Accesses the protected fillGradient method inherited from GuiComponent.
   *
   * @param x1 Start X coordinate.
   * @param y1 Start Y coordinate.
   * @param x2 End X coordinate.
   * @param y2 End Y coordinate.
   * @param colorFrom Top color code in ARGB format.
   * @param colorTo Bottom color code in ARGB format.
   */
  public void fillGradient(int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
    fillGradient(getPoseStack(), x1, y1, x2, y2, colorFrom, colorTo, 0);
  }

  /**
   * Renders a textured quad using the current graphics context.
   *
   * @param texture Texture resource location.
   * @param x Screen target X position.
   * @param y Screen target Y position.
   * @param width Render width on screen.
   * @param height Render height on screen.
   * @param textureWidth Original texture sheet width.
   * @param textureHeight Original texture sheet height.
   */
  public void blit(ResourceLocation texture, int x, int y, int width, int height, int textureWidth,
      int textureHeight) {
    RenderSystem.setShaderTexture(0, texture);
    blit(getPoseStack(), x, y, width, height, 0.0F, 0.0F, textureWidth, textureHeight, textureWidth,
        textureHeight);
  }

  /**
   * Draws a plain text string with optional shadow.
   *
   * @param text Text string to draw.
   * @param x Screen target X position.
   * @param y Screen target Y position.
   * @param color Text color in ARGB.
   * @param shadow True to enable drop shadow effect.
   */
  public void drawString(String text, int x, int y, int color, boolean shadow) {
    if (shadow) {
      drawString(getPoseStack(), this.font, text, x, y, color);
    } else {
      drawString(getPoseStack(), this.font, text, x, y, color);
    }
  }

  /**
   * Draws a formatted Minecraft Component with optional shadow.
   *
   * @param component Text component to draw.
   * @param x Screen target X position.
   * @param y Screen target Y position.
   * @param color Text color in ARGB.
   * @param shadow True to enable drop shadow effect.
   */
  public void drawString(Component component, int x, int y, int color, boolean shadow) {
    drawString(component.getString(), x, y, color, shadow);
  }

  /**
   * Pushes a new transformation matrix onto the matrix stack.
   */
  public void push() {
    getPoseStack().pushPose();
  }

  /**
   * Pops the top transformation matrix off the matrix stack.
   */
  public void pop() {
    getPoseStack().popPose();
  }

  /**
   * Applies translation (offset) to the transformation matrix.
   *
   * @param x Translation along the X axis.
   * @param y Translation along the Y axis.
   * @param z Translation along the Z axis.
   */
  public void translate(float x, float y, float z) {
    getPoseStack().translate(x, y, z);
  }

  /**
   * Applies scaling factors to the transformation matrix.
   *
   * @param x Scale factor along X axis.
   * @param y Scale factor along Y axis.
   * @param z Scale factor along Z axis.
   */
  public void scale(float x, float y, float z) {
    getPoseStack().scale(x, y, z);
  }
}
