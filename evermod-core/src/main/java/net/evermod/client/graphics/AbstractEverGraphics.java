package net.evermod.client.graphics;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.evermod.client.graphics.font.EverFont;
import net.evermod.client.graphics.font.MCEverFont;
import net.evermod.client.graphics.geometry.InnerBounds;
import net.evermod.client.graphics.geometry.ScreenRectangle;
import net.evermod.client.graphics.pipeline.MCEverTesselator;
import net.evermod.client.graphics.pipeline.EverBufferBuilder;
import net.evermod.client.graphics.pipeline.EverTesselator;
import net.evermod.client.graphics.scissor.ScissorStack;
import net.evermod.client.graphics.style.Border;
import net.evermod.client.graphics.style.BorderColor;
import net.evermod.client.graphics.style.GradientStyle;
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

  protected static final EverTesselator TESSELATOR = new MCEverTesselator();
  protected static final EverFont FONT = new MCEverFont();
  private final ScissorStack scissorStack = new ScissorStack();
  protected final PoseStack poseStack;

  protected AbstractEverGraphics(PoseStack poseStack) {
    this.poseStack = poseStack;
  }

  protected EverMatrix4f getActiveMatrix() {
    return new EverMatrix4f(this.poseStack.last().pose());
  }

  public PoseStack getPoseStack() {
    return this.poseStack;
  }

  public static EverTesselator getSharedTesselator() {
    return TESSELATOR;
  }

  public EverTesselator getTesselator() {
    return TESSELATOR;
  }

  public static EverFont getSharedFont() {
    return FONT;
  }

  public EverFont getFont() {
    return FONT;
  }

  public void setupTexShader() {
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.disableDepthTest();
    RenderSystem.setShader(GameRenderer::getPositionTexShader);
  }

  protected void setupColorShader() {
    RenderSystem.enableBlend();
    RenderSystem.defaultBlendFunc();
    RenderSystem.disableDepthTest();
    RenderSystem.setShader(GameRenderer::getPositionColorShader);
  }

  protected void restoreState() {
    RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    RenderSystem.disableBlend();
    RenderSystem.disableDepthTest();
  }

  public void setColor(float red, float green, float blue, float alpha) {
    RenderSystem.setShaderColor(red, green, blue, alpha);
  }

  public void enableScissor(double x, double y, double width, double height) {
    EverMatrix4f matrix = this.getActiveMatrix();
    double globalX = x + matrix.getTranslationX();
    double globalY = y + matrix.getTranslationY();
    ScreenRectangle activeRect =
        this.scissorStack.push(new ScreenRectangle(globalX, globalY, width, height));
    this.applyScissor(activeRect);
  }

  public void enableScissor(double width, double height) {
    EverMatrix4f matrix = this.getActiveMatrix();
    double globalX = matrix.getTranslationX();
    double globalY = matrix.getTranslationY();

    ScreenRectangle activeRect =
        this.scissorStack.push(new ScreenRectangle(globalX, globalY, width, height));
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

  protected static void fillTrapezoid(EverBufferBuilder builder, EverMatrix4f matrix,
      float x1, float y1, float x2, float y2,
      float x3, float y3, float x4, float y4,
      float z, GradientStyle style) {

    builder.vertex(matrix, x1, y1, z).color(style.r1(), style.g1(), style.b1(), style.a1())
        .endVertex();
    builder.vertex(matrix, x2, y2, z).color(style.r2(), style.g2(), style.b2(), style.a2())
        .endVertex();
    builder.vertex(matrix, x3, y3, z).color(style.r3(), style.g3(), style.b3(), style.a3())
        .endVertex();
    builder.vertex(matrix, x4, y4, z).color(style.r4(), style.g4(), style.b4(), style.a4())
        .endVertex();
  }

  protected static void blitTrapezoid(
      EverBufferBuilder builder, EverMatrix4f matrix,
      float x1, float y1, float x2, float y2,
      float x3, float y3, float x4, float y4,
      float z, float minU, float maxU, float minV, float maxV) {

    builder.vertex(matrix, x1, y1, z).uv(minU, maxV).endVertex();
    builder.vertex(matrix, x2, y2, z).uv(maxU, maxV).endVertex();
    builder.vertex(matrix, x3, y3, z).uv(maxU, minV).endVertex();
    builder.vertex(matrix, x4, y4, z).uv(minU, minV).endVertex();
  }

  public void fill(int x1, int y1, int x2, int y2, GradientStyle style) {
    EverMatrix4f matrix = this.getActiveMatrix();
    this.setupColorShader();

    EverBufferBuilder builder = TESSELATOR.beginPositionColor();

    fillTrapezoid(builder, matrix,
        x1, y2, // Bottom-Left
        x2, y2, // Bottom-Right
        x2, y1, // Top-Right
        x1, y1, // Top-Left
        0.0F, style);

    TESSELATOR.draw();
    this.restoreState();
  }

  public void fillBorder(InnerBounds bounds, Border border, BorderColor borderColor) {
    if (border == null || borderColor == null) {
      return;
    }
    this.fillBorder(bounds, border, GradientStyle.solid(borderColor.top()),
        GradientStyle.solid(borderColor.right()), GradientStyle.solid(borderColor.bottom()),
        GradientStyle.solid(borderColor.left()));
  }

  public void fillBorder(InnerBounds bounds, Border border, GradientStyle borderTop,
      GradientStyle borderRight, GradientStyle borderBottom, GradientStyle borderLeft) {
    if (border == null) {
      return;
    }

    EverMatrix4f matrix = this.getActiveMatrix();
    this.setupColorShader();
    EverBufferBuilder builder = TESSELATOR.beginPositionColor();

    if (border.top() > 0) {
      fillTrapezoid(builder, matrix,
          bounds.innerX(), bounds.innerY(), //  Bottom-Left
          bounds.innerX2(), bounds.innerY(), // Bottom-Right
          bounds.x2(), bounds.y(), //           Top-Right
          bounds.x(), bounds.y(), //            Top-Left
          0.0F, borderTop);
    }
    if (border.right() > 0) {
      fillTrapezoid(builder, matrix,
          bounds.innerX2(), bounds.innerY2(),
          bounds.x2(), bounds.y2(),
          bounds.x2(), bounds.y(),
          bounds.innerX2(), bounds.innerY(),
          0.0F, borderRight);
    }
    if (border.bottom() > 0) {
      fillTrapezoid(builder, matrix,
          bounds.x(), bounds.y2(),
          bounds.x2(), bounds.y2(),
          bounds.innerX2(), bounds.innerY2(),
          bounds.innerX(), bounds.innerY2(),
          0.0F, borderBottom);
    }
    if (border.left() > 0) {
      fillTrapezoid(builder, matrix,
          bounds.x(), bounds.y2(),
          bounds.innerX(), bounds.innerY2(),
          bounds.innerX(), bounds.innerY(),
          bounds.x(), bounds.y(),
          0.0F, borderLeft);
    }

    TESSELATOR.draw();
    this.restoreState();
  }

  public void fillFlatBorder(InnerBounds bounds, Border border, GradientStyle borderStyle) {
    if (border == null) {
      return;
    }
    this.fillFlatBorder(bounds, border, borderStyle, borderStyle, borderStyle,
        borderStyle);
  }

  public void fillFlatBorder(InnerBounds bounds, Border border, GradientStyle borderTop,
      GradientStyle borderRight, GradientStyle borderBottom, GradientStyle borderLeft) {
    if (border == null) {
      return;
    }

    if (border.top() > 0) {
      this.fill(
          bounds.innerX(), bounds.y(), //       Top-Left
          bounds.innerX2(), bounds.innerY(), // Bottom-Right
          borderTop);
    }
    if (border.right() > 0) {
      this.fill(
          bounds.innerX2(), bounds.innerY(),
          bounds.x2(), bounds.innerY2(),
          borderRight);
    }
    if (border.bottom() > 0) {
      this.fill(
          bounds.innerX(), bounds.innerY2(),
          bounds.innerX2(), bounds.y2(),
          borderBottom);
    }
    if (border.left() > 0) {
      this.fill(
          bounds.x(), bounds.innerY(),
          bounds.innerX(), bounds.innerY2(),
          borderLeft);
    }
  }

  public void blit(ResourceLocation texture, float x1, float y1, float x2, float y2,
      float minU, float maxU, float minV, float maxV,
      float red, float green, float blue, float alpha) {

    EverMatrix4f matrix = this.getActiveMatrix();

    this.setupTexShader();
    RenderSystem.setShaderTexture(0, texture);
    this.setColor(red, green, blue, alpha);

    EverBufferBuilder builder = TESSELATOR.beginPositionTex();

    blitTrapezoid(
        builder, matrix,
        x1, y2, // Bottom-Left
        x2, y2, // Bottom-Right
        x2, y1, // Top-Right
        x1, y1, // Top-Left
        0.0F, minU, maxU, minV, maxV);

    TESSELATOR.draw();
    this.restoreState();
  }

  public void drawString(String text, int x, int y, int color, boolean shadow) {
    FONT.drawString(this.poseStack, text, x, y, color, shadow);
  }

  public void drawString(Component component, int x, int y, int color, boolean shadow) {
    FONT.drawString(this.poseStack, component, x, y, color, shadow);
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
