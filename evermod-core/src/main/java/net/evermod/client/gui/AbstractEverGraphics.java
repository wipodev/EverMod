package net.evermod.client.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.evermod.client.gui.render.EverFont;
import net.evermod.client.gui.render.EverTesselator;
import net.evermod.client.gui.render.GradientStyle;
import net.evermod.client.gui.render.IEverBufferBuilder;
import net.evermod.client.gui.render.IEverFont;
import net.evermod.client.gui.render.IEverTesselator;
import net.evermod.math.EverMatrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Base rendering engine containing primitive operations, shader state management,
 * and unified drawing methods with optional borders.
 *
 * @author Wipodev
 */
public abstract class AbstractEverGraphics {

  protected static final IEverTesselator TESSELATOR = new EverTesselator();
  private final ScissorStack scissorStack = new ScissorStack();
  protected final PoseStack poseStack;
  protected final IEverFont font;

  protected AbstractEverGraphics(PoseStack poseStack) {
    this.poseStack = poseStack;
    this.font = new EverFont();
  }

  protected EverMatrix4f getActiveMatrix() {
    return new EverMatrix4f(this.poseStack.last().pose());
  }

  public PoseStack getPoseStack() {
    return this.poseStack;
  }

  public IEverFont getFont() {
    return this.font;
  }

  public void enableBlend() {
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
  }

  public void disableBlend() {
    RenderSystem.disableBlend();
  }

  protected void setupColorShader() {
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
  }

  public void resetColor() {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
  }

  public void setColor(float red, float green, float blue, float alpha) {
    RenderSystem.setShaderColor(red, green, blue, alpha);
  }

  public void enableScissor(double x, double y, double width, double height) {
    ScreenRectangle activeRect = this.scissorStack.push(new ScreenRectangle(x, y, width, height));
    this.applyScissor(activeRect);
  }

  public void enableScissor(int x, int y, int width, int height) {
    this.enableScissor((double) x, (double) y, (double) width, (double) height);
  }

  public void disableScissor() {
    this.applyScissor(this.scissorStack.pop());
  }

  private void applyScissor(ScreenRectangle rect) {
    if (rect == null || rect.width() <= 0.0 || rect.height() <= 0.0) {
      RenderSystem.disableScissor();
      return;
    }

    Window window = Minecraft.getInstance().getWindow();
    double scale = window.getGuiScale();
    double windowHeight = (double) window.getHeight();
    double screenX = rect.x() * scale;
    double screenY = windowHeight - (rect.bottomEdge() * scale);
    double screenWidth = rect.width() * scale;
    double screenHeight = rect.height() * scale;

    RenderSystem.enableScissor(
        Math.max(0, (int) screenX),
        Math.max(0, (int) screenY),
        Math.max(0, (int) screenWidth),
        Math.max(0, (int) screenHeight));
  }

  protected static void fillTrapezoid(IEverBufferBuilder builder, EverMatrix4f matrix,
      float x1, float y1, float x2, float y2,
      float x3, float y3, float x4, float y4,
      GradientStyle style) {

    builder.vertex(matrix, x4, y4, 0.0F, style.r4(), style.g4(), style.b4(), style.a4());
    builder.vertex(matrix, x3, y3, 0.0F, style.r3(), style.g3(), style.b3(), style.a3());
    builder.vertex(matrix, x2, y2, 0.0F, style.r2(), style.g2(), style.b2(), style.a2());
    builder.vertex(matrix, x1, y1, 0.0F, style.r1(), style.g1(), style.b1(), style.a1());
  }

  protected static void blitTrapezoid(
      IEverBufferBuilder builder, EverMatrix4f matrix,
      float x1, float y1, float x2, float y2,
      float x3, float y3, float x4, float y4,
      float minU, float maxU, float minV, float maxV) {

    builder.vertex(matrix, x1, y1, 0.0F, minU, maxV);
    builder.vertex(matrix, x2, y2, 0.0F, maxU, maxV);
    builder.vertex(matrix, x3, y3, 0.0F, maxU, minV);
    builder.vertex(matrix, x4, y4, 0.0F, minU, minV);
  }

  public void fill(int x1, int y1, int x2, int y2, GradientStyle style) {
    int minX = Math.min(x1, x2);
    int maxX = Math.max(x1, x2);
    int minY = Math.min(y1, y2);
    int maxY = Math.max(y1, y2);

    EverMatrix4f matrix = getActiveMatrix();
    this.setupColorShader();

    IEverBufferBuilder builder = TESSELATOR.beginPositionColor();
    fillTrapezoid(builder, matrix, minX, minY, maxX, minY, maxX, maxY, minX, maxY, style);
    TESSELATOR.draw();

    this.disableBlend();
  }

  public void fillBorder(int x, int y, int width, int height, Border border,
      BorderColor borderColor) {
    if (border == null || borderColor == null) {
      return;
    }

    InnerBounds bounds = InnerBounds.of(border, x, y, width, height);

    EverMatrix4f matrix = getActiveMatrix();
    this.setupColorShader();
    IEverBufferBuilder builder = TESSELATOR.beginPositionColor();

    if (bounds.top() > 0) {
      fillTrapezoid(builder, matrix, x, y, bounds.x2(), y, bounds.innerX2(), bounds.innerY(),
          bounds.innerX(), bounds.innerY(),
          GradientStyle.solid(borderColor.top()));
    }
    if (bounds.bottom() > 0) {
      fillTrapezoid(builder, matrix, bounds.innerX(), bounds.innerY2(), bounds.innerX2(),
          bounds.innerY2(), bounds.x2(), bounds.y2(), x, bounds.y2(),
          GradientStyle.solid(borderColor.bottom()));
    }
    if (bounds.left() > 0) {
      fillTrapezoid(builder, matrix, x, y, bounds.innerX(), bounds.innerY(), bounds.innerX(),
          bounds.innerY2(), x, bounds.y2(),
          GradientStyle.solid(borderColor.left()));
    }
    if (bounds.right() > 0) {
      fillTrapezoid(builder, matrix, bounds.innerX2(), bounds.innerY(), bounds.x2(), y, bounds.x2(),
          bounds.y2(), bounds.innerX2(), bounds.innerY2(),
          GradientStyle.solid(borderColor.right()));
    }

    TESSELATOR.draw();
    RenderSystem.disableBlend();
  }

  public void blit(ResourceLocation texture, float x1, float y1, float x2, float y2,
      float minU, float maxU, float minV, float maxV,
      float red, float green, float blue, float alpha) {

    EverMatrix4f matrix = getActiveMatrix();

    this.enableBlend();
    RenderSystem.enableDepthTest();
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
    RenderSystem.setShaderTexture(0, texture);
    this.setColor(red, green, blue, alpha);

    IEverBufferBuilder builder = TESSELATOR.beginPositionTex();

    // Pass 4 coordinates directly to the blitTrapezoid helper
    blitTrapezoid(
        builder, matrix,
        x1, y2, // Bottom-Left
        x2, y2, // Bottom-Right
        x2, y1, // Top-Right
        x1, y1, // Top-Left
        minU, maxU, minV, maxV);

    TESSELATOR.draw();
    this.resetColor();
    this.disableBlend();
  }

  public void drawString(String text, int x, int y, int color, boolean shadow) {
    this.font.drawString(this.poseStack, text, x, y, color, shadow);
  }

  public void drawString(Component component, int x, int y, int color, boolean shadow) {
    this.font.drawString(this.poseStack, component, x, y, color, shadow);
  }

  public void push() {
    this.poseStack.pushPose();
  }

  public void pop() {
    this.poseStack.popPose();
  }

  public void translate(float x, float y, float z) {
    this.poseStack.translate(x, y, z);
  }

  public void scale(float x, float y, float z) {
    this.poseStack.scale(x, y, z);
  }
}
