package net.evermod.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

/**
 * Unified graphics context abstraction for EverMod.
 * Serves as the central low-level rendering engine for screens, overlays, and HUD elements,
 * encapsulating RenderSystem state changes, shader bindings, and matrix operations.
 *
 * @author Wipodev
 */
public class EverGraphics extends GuiComponent {

  /** The wrapped underlying native graphics context (e.g., PoseStack or GuiGraphics). */
  private final Object nativeContext;

  /** The active font renderer instance for text drawing operations. */
  private final Font font;

  /**
   * Constructs a new EverGraphics instance wrapping the native graphics context.
   *
   * @param nativeContext Native graphics context object (e.g., PoseStack or GuiGraphics).
   */
  public EverGraphics(Object nativeContext) {
    this.nativeContext = nativeContext;
    this.font = Minecraft.getInstance().font;
  }

  /**
   * Factory method to obtain or wrap an EverGraphics instance.
   *
   * @param nativeContext Native graphics context instance.
   * @return An EverGraphics wrapper instance.
   */
  public static EverGraphics of(Object nativeContext) {
    if (nativeContext instanceof EverGraphics everGraphics) {
      return everGraphics;
    }
    return new EverGraphics(nativeContext);
  }

  /**
   * Retrieves the wrapped raw native graphics context.
   *
   * @return Native context object.
   */
  public Object getNativeContext() {
    return this.nativeContext;
  }

  /**
   * Retrieves the active Font renderer instance.
   *
   * @return Minecraft Font instance.
   */
  public Font getFont() {
    return this.font;
  }

  /**
   * Extracts the active PoseStack from the underlying context.
   *
   * @return The active PoseStack.
   */
  public PoseStack getPoseStack() {
    if (this.nativeContext instanceof PoseStack poseStack) {
      return poseStack;
    }
    return (PoseStack) this.nativeContext;
  }

  // --- RENDER SYSTEM STATE HELPERS ---

  /**
   * Enables standard alpha blending for transparent textures and shapes.
   */
  public void enableBlend() {
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
  }

  /**
   * Disables alpha blending.
   */
  public void disableBlend() {
    RenderSystem.disableBlend();
  }

  /**
   * Resets the active shader tint color back to fully opaque white.
   */
  public void resetColor() {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  /**
   * Sets a custom color tint for subsequent texture rendering operations.
   *
   * @param red   Red color component [0.0F - 1.0F].
   * @param green Green color component [0.0F - 1.0F].
   * @param blue  Blue color component [0.0F - 1.0F].
   * @param alpha Transparency alpha component [0.0F - 1.0F].
   */
  public void setColor(float red, float green, float blue, float alpha) {
    RenderSystem.setShaderColor(red, green, blue, alpha);
  }

  // --- SCISSORING (CLIPPING) MANAGEMENT ---

  /**
   * Restricts rendering output to a sub-region of the screen.
   * Takes scaled GUI coordinates and automatically converts them to window pixel coordinates.
   *
   * @param x      Start X position in GUI coordinates.
   * @param y      Start Y position in GUI coordinates.
   * @param width  Region width in GUI coordinates.
   * @param height Region height in GUI coordinates.
   */
  public void activateScissor(int x, int y, int width, int height) {
    GuiComponent.enableScissor(x, y, x + Math.max(0, width), y + Math.max(0, height));
  }

  /**
   * Disables region scissoring.
   */
  public void deactivateScissor() {
    RenderSystem.disableScissor();
  }

  // --- RECTANGLES AND GRADIENTS ---

  /**
   * Draws a solid colored rectangle.
   *
   * @param x1    Start X position.
   * @param y1    Start Y position.
   * @param x2    End X position.
   * @param y2    End Y position.
   * @param color ARGB formatted color code.
   */
  public void fill(int x1, int y1, int x2, int y2, int color) {
    this.enableBlend();
    fill(getPoseStack(), x1, y1, x2, y2, color);
    this.resetColor();
  }

  /**
   * Draws an unfilled rectangle border.
   *
   * @param x      Start X position.
   * @param y      Start Y position.
   * @param width  Border width.
   * @param height Border height.
   * @param color  ARGB border color.
   */
  public void drawOutlineRect(int x, int y, int width, int height, int color) {
    fill(x, y, x + width, y + 1, color); // Top
    fill(x, y + height - 1, x + width, y + height, color); // Bottom
    fill(x, y, x + 1, y + height, color); // Left
    fill(x + width - 1, y, x + width, y + height, color); // Right
  }

  /**
   * Draws a vertical color gradient rectangle.
   *
   * @param x1        Start X position.
   * @param y1        Start Y position.
   * @param x2        End X position.
   * @param y2        End Y position.
   * @param colorFrom Top color code in ARGB.
   * @param colorTo   Bottom color code in ARGB.
   */
  public void fillGradient(int x1, int y1, int x2, int y2, int colorFrom, int colorTo) {
    this.enableBlend();
    fillGradient(getPoseStack(), x1, y1, x2, y2, colorFrom, colorTo, 0);
    this.resetColor();
  }

  // --- TEXTURE RENDER METHODS ---

  /**
   * Draws a texture quad with default 256x256 dimensions using safe RenderSystem states.
   *
   * @param texture Texture resource location.
   * @param x       Screen target X position.
   * @param y       Screen target Y position.
   * @param width   Render width on screen.
   * @param height  Render height on screen.
   */
  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height) {
    this.drawTexture(texture, x, y, width, height, 256, 256);
  }

  /**
   * Draws a texture quad with custom sheet dimensions.
   *
   * @param texture       Texture resource location.
   * @param x             Screen target X position.
   * @param y             Screen target Y position.
   * @param width         Render width on screen.
   * @param height        Render height on screen.
   * @param textureWidth  Original texture sheet width.
   * @param textureHeight Original texture sheet height.
   */
  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height,
      int textureWidth, int textureHeight) {
    this.drawTexture(texture, x, y, width, height, 1.0F, 1.0F, 1.0F, 1.0F, textureWidth,
        textureHeight);
  }

  /**
   * Draws a tinted texture quad with custom RGBA values (e.g., overlay vignettes or colored buttons).
   *
   * @param texture       Texture resource location.
   * @param x             Screen target X position.
   * @param y             Screen target Y position.
   * @param width         Render width on screen.
   * @param height        Render height on screen.
   * @param red           Red tint factor [0.0F - 1.0F].
   * @param green         Green tint factor [0.0F - 1.0F].
   * @param blue          Blue tint factor [0.0F - 1.0F].
   * @param alpha         Alpha factor [0.0F - 1.0F].
   * @param textureWidth  Original texture sheet width.
   * @param textureHeight Original texture sheet height.
   */
  public void drawTexture(ResourceLocation texture, int x, int y, int width, int height, float red,
      float green, float blue, float alpha, int textureWidth, int textureHeight) {
    this.enableBlend();
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, texture);
    this.setColor(red, green, blue, alpha);

    blit(getPoseStack(), x, y, width, height, 0.0F, 0.0F, textureWidth, textureHeight, textureWidth,
        textureHeight);

    this.resetColor();
    this.disableBlend();
  }

  // --- TEXT RENDERING METHODS ---

  /**
   * Draws plain text with drop shadow support.
   *
   * @param text   Text string to render.
   * @param x      Screen target X position.
   * @param y      Screen target Y position.
   * @param color  ARGB color code.
   * @param shadow True to enable shadow.
   */
  public void drawString(String text, int x, int y, int color, boolean shadow) {
    if (shadow) {
      this.font.drawShadow(getPoseStack(), text, x, y, color);
    } else {
      this.font.draw(getPoseStack(), text, x, y, color);
    }
  }

  /**
   * Draws a rich Minecraft Component, preserving color codes and internal formatting.
   *
   * @param component Formatted text component.
   * @param x         Screen target X position.
   * @param y         Screen target Y position.
   * @param color     ARGB default color.
   * @param shadow    True to enable shadow.
   */
  public void drawString(Component component, int x, int y, int color, boolean shadow) {
    if (shadow) {
      this.font.drawShadow(getPoseStack(), component.getVisualOrderText(), x, y, color);
    } else {
      this.font.draw(getPoseStack(), component.getVisualOrderText(), x, y, color);
    }
  }

  /**
   * Draws centered text on screen.
   *
   * @param text   Text string to render.
   * @param x      Center X coordinate.
   * @param y      Target Y coordinate.
   * @param color  ARGB color code.
   * @param shadow True to enable shadow.
   */
  public void drawCenteredString(String text, int x, int y, int color, boolean shadow) {
    int width = this.font.width(text);
    this.drawString(text, x - width / 2, y, color, shadow);
  }

  /**
   * Draws a centered rich Minecraft Component preserving visual formatting.
   *
   * @param component Formatted text component.
   * @param x         Center X coordinate.
   * @param y         Target Y coordinate.
   * @param color     ARGB color code.
   * @param shadow    True to enable shadow.
   */
  public void drawCenteredString(Component component, int x, int y, int color, boolean shadow) {
    int width = this.font.width(component);
    this.drawString(component, x - width / 2, y, color, shadow);
  }

  // --- ITEM STACK RENDERING ---

  /**
   * Safely renders an ItemStack along with its decorations (e.g., item count, durability bar).
   *
   * @param stack Target ItemStack to draw.
   * @param x     Screen target X position.
   * @param y     Screen target Y position.
   */
  public void renderItem(ItemStack stack, int x, int y) {
    if (!stack.isEmpty()) {
      this.enableBlend();
      Minecraft.getInstance().getItemRenderer().renderAndDecorateItem(stack, x, y);
      Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(this.font, stack, x, y);
      this.resetColor();
    }
  }

  // --- MATRIX TRANSFORMATIONS ---

  /**
   * Pushes a new pose matrix onto the PoseStack.
   */
  public void push() {
    getPoseStack().pushPose();
  }

  /**
   * Pops the top pose matrix from the PoseStack.
   */
  public void pop() {
    getPoseStack().popPose();
  }

  /**
   * Applies translation offsets to the matrix.
   *
   * @param x Offset X coordinate.
   * @param y Offset Y coordinate.
   * @param z Offset Z coordinate.
   */
  public void translate(float x, float y, float z) {
    getPoseStack().translate(x, y, z);
  }

  /**
   * Applies scaling transformations to the matrix.
   *
   * @param x Scale factor X.
   * @param y Scale factor Y.
   * @param z Scale factor Z.
   */
  public void scale(float x, float y, float z) {
    getPoseStack().scale(x, y, z);
  }
}
