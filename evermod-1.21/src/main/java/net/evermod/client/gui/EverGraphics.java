package net.evermod.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Unified graphics context abstraction for EverMod targetting Minecraft 1.20.1+.
 * Wraps Minecraft's native {@link GuiGraphics} instance into a consistent API.
 *
 * @author Wipodev
 */
public class EverGraphics {
  private final GuiGraphics guiGraphics;
  private final Font font;

  /**
   * Constructs a new EverGraphics instance wrapping the native graphics object.
   * Handles type safety for 1.21 GuiGraphics context.
   *
   * @param nativeContext Native graphics context (GuiGraphics in 1.20.1+).
   */
  public EverGraphics(Object nativeContext) {
    if (nativeContext instanceof GuiGraphics graphics) {
      this.guiGraphics = graphics;
    } else {
      throw new IllegalArgumentException(
          "Expected nativeContext to be an instance of GuiGraphics for 1.20.1+");
    }
    this.font = Minecraft.getInstance().font;
  }

  /**
   * Gets the underlying raw native GuiGraphics instance.
   *
   * @return Native GuiGraphics context.
   */
  public GuiGraphics getGuiGraphics() {
    return guiGraphics;
  }

  /**
   * Gets the underlying raw native graphics object.
   *
   * @return Raw native context instance.
   */
  public Object getNativeContext() {
    return guiGraphics;
  }

  /**
   * Retrieves the PoseStack instance from the wrapped GuiGraphics context.
   *
   * @return The active PoseStack.
   */
  public PoseStack getPoseStack() {
    return guiGraphics.pose();
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
    guiGraphics.fill(x1, y1, x2, y2, color);
  }

  /**
   * Draws a rectangle filled with a vertical color gradient.
   *
   * @param x1 Start X coordinate.
   * @param y1 Start Y coordinate.
   * @param x2 End X coordinate.
   * @param y2 End Y coordinate.
   * @param colorFrom Top color code in ARGB format.
   * @param colorTo Bottom color code in ARGB format.
   */
  public void fillGradient(int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
    guiGraphics.fillGradient(x1, y1, x2, y2, colorFrom, colorTo);
  }

  /**
   * Renders a textured quad using the native GuiGraphics context.
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
    guiGraphics.blit(texture, x, y, 0, 0.0F, 0.0F, width, height, textureWidth, textureHeight);
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
    guiGraphics.drawString(this.font, text, x, y, color, shadow);
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
    guiGraphics.drawString(this.font, component, x, y, color, shadow);
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
