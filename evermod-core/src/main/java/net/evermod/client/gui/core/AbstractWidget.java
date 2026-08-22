package net.evermod.client.gui.core;

import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.graphics.style.Border;
import net.evermod.client.graphics.style.BorderColor;
import net.evermod.client.gui.api.Interactive;
import net.evermod.client.gui.api.Renderable;
import net.evermod.client.gui.api.TooltipProvider;
import net.evermod.client.gui.api.style.TextAlignment;
import net.evermod.client.gui.api.style.TextStyleable;
import net.evermod.client.gui.widget.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Abstract base class for UI widgets.
 * Combines positioning, rendering pipeline, style properties, state management, and tooltips.
 *
 * @param <T> Concrete widget subtype for fluent method chaining.
 */
public abstract class AbstractWidget<T extends AbstractWidget<T>>
    extends UIElement<T>
    implements Renderable, Interactive, TextStyleable<T>, TooltipProvider {

  // ==========================================
  // Fields: Visual & Styling
  // ==========================================
  protected int hoverBackgroundColor = 0x00000000;
  protected int disabledBackgroundColor = 0x00000000;
  protected ResourceLocation hoverBackgroundTexture;
  protected ResourceLocation disabledBackgroundTexture;

  protected BorderColor hoverBorderColor;
  protected BorderColor disabledBorderColor;

  protected int textColor = 0xFFFFFFFF;
  protected boolean textShadow = false;
  protected float fontSize = 1.0F;
  protected ResourceLocation fontFamily;
  protected TextAlignment textAlign = TextAlignment.LEFT;

  // ==========================================
  // Fields: State & Extras
  // ==========================================
  protected boolean hovered;
  protected boolean focused;
  protected Tooltip tooltip;

  public AbstractWidget(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public AbstractWidget() {
    super(0, 0, 0, 0);
  }

  @SuppressWarnings("unchecked")
  protected T self() {
    return (T) this;
  }

  // ==========================================
  // Rendering Pipeline
  // ==========================================
  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!this.visible) {
      return;
    }

    autoSize();
    this.hovered = this.isHovered(mouseX, mouseY);

    graphics.push();
    graphics.translate(this.x, this.y, 0.0F);

    this.renderBackground(graphics, mouseX, mouseY, partialTicks);
    this.renderContent(graphics, mouseX, mouseY, partialTicks);

    graphics.pop();
  }

  /**
   * Abstract method to render widget-specific content using local space (0,0).
   */
  protected abstract void renderContent(
      EverGraphics graphics, int mouseX, int mouseY, float partialTicks);

  // ==========================================
  // Helpers: Dynamic State Resolvers
  // ==========================================
  @Override
  protected int getCurrentBackgroundColor() {
    if (!this.enabled) {
      return this.disabledBackgroundColor;
    }
    if (this.hovered) {
      return this.hoverBackgroundColor;
    }
    return this.backgroundColor;
  }

  /**
  * Retrieves the texture based on the current component state.
  *
  * @return The active ResourceLocation for the background texture.
  */
  @Override
  protected ResourceLocation getCurrentBackgroundTexture() {
    if (!this.enabled && this.disabledBackgroundTexture != null) {
      return this.disabledBackgroundTexture;
    }
    if (this.hovered && this.hoverBackgroundTexture != null) {
      return this.hoverBackgroundTexture;
    }
    return this.backgroundTexture;
  }

  @Override
  protected BorderColor getCurrentBorderColor() {
    if (!this.enabled && this.disabledBorderColor != null) {
      return this.disabledBorderColor;
    }
    if (this.hovered && this.hoverBorderColor != null) {
      return this.hoverBorderColor;
    }
    return this.borderColor;
  }

  // ==========================================
  // Interface: Interactive
  // ==========================================
  @Override
  public boolean isHovered(double pointX, double pointY) {
    return this.canInteract() && this.containsPoint(pointX, pointY);
  }

  public boolean isHovered() {
    return this.hovered;
  }

  @Override
  public boolean isFocused() {
    return this.focused;
  }

  @Override
  public void setFocused(boolean focused) {
    if (this.enabled) {
      this.focused = focused;
    }
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    this.hovered = this.isHovered(mouseX, mouseY);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!this.canInteract()) {
      return false;
    }
    boolean clicked = this.containsPoint(mouseX, mouseY);
    if (clicked) {
      this.setFocused(true);
    }
    return clicked;
  }

  // ==========================================
  // Interface: TooltipProvider & Tooltip API
  // ==========================================
  public T tooltip(Tooltip tooltip) {
    this.tooltip = tooltip;
    return self();
  }

  public T tooltip(Component text) {
    this.tooltip = new Tooltip(text);
    return self();
  }

  public T tooltip(String text) {
    this.tooltip = new Tooltip(text);
    return self();
  }

  public Tooltip getTooltip() {
    return this.tooltip;
  }

  @Override
  public boolean isTooltipActive(int mouseX, int mouseY) {
    return this.visible && this.hovered && this.tooltip != null;
  }

  @Override
  public void renderTooltip(EverGraphics graphics, int mouseX, int mouseY) {
    if (this.tooltip != null) {
      this.tooltip.setActive(true);
      this.tooltip.renderOverlay(graphics, mouseX, mouseY);
    }
  }

  // ==========================================
  // Interface: WidgetStyleable (Borders)
  // ==========================================

  public T border(Border border, BorderColor color, BorderColor hoverColor) {
    this.border = border;
    this.borderColor = color;
    this.hoverBorderColor = hoverColor;
    return self();
  }

  public T border(
      Border border, BorderColor color, BorderColor hoverColor, BorderColor disabledColor) {
    this.border = border;
    this.borderColor = color;
    this.hoverBorderColor = hoverColor;
    this.disabledBorderColor = disabledColor;
    return self();
  }

  public BorderColor getHoverBorderColor() {
    return this.hoverBorderColor;
  }

  public BorderColor getDisabledBorderColor() {
    return this.disabledBorderColor;
  }

  // ==========================================
  // Interface: ElementStyleable (Backgrounds)
  // ==========================================

  public T background(int color, int hoverColor, int disableColor) {
    this.backgroundColor = color;
    this.hoverBackgroundColor = hoverColor;
    this.disabledBackgroundColor = disableColor;
    return self();
  }

  public T background(ResourceLocation texture, ResourceLocation hoverTexture,
      ResourceLocation disabledTexture, int tintColor) {
    this.backgroundTexture = texture;
    this.hoverBackgroundTexture = hoverTexture;
    this.disabledBackgroundTexture = disabledTexture;
    this.backgroundColor = tintColor;
    return self();
  }

  public T background(ResourceLocation texture, ResourceLocation hoverTexture,
      ResourceLocation disabledTexture) {
    return this.background(texture, hoverTexture, disabledTexture, 0xFFFFFFFF);
  }

  public int getHoverBackgroundColor() {
    return this.hoverBackgroundColor;
  }

  public int getDisabledBackgroundColor() {
    return this.disabledBackgroundColor;
  }

  // ==========================================
  // Interface: WidgetStyleable (Text Formatting)
  // ==========================================
  @Override
  public T color(int argbColor) {
    this.textColor = argbColor;
    return self();
  }

  @Override
  public T fontShadow(boolean shadow) {
    this.textShadow = shadow;
    return self();
  }

  @Override
  public T fontSize(float size) {
    this.fontSize = size;
    return self();
  }

  @Override
  public T fontFamily(ResourceLocation fontId) {
    this.fontFamily = fontId;
    return self();
  }

  @Override
  public T textAlign(TextAlignment alignment) {
    this.textAlign = alignment;
    return self();
  }

  @Override
  public int getColor() {
    return this.textColor;
  }

  @Override
  public boolean getTextShadow() {
    return this.textShadow;
  }

  @Override
  public float getFontSize() {
    return this.fontSize;
  }

  @Override
  public ResourceLocation getFontFamily() {
    return this.fontFamily;
  }

  @Override
  public TextAlignment getTextAlign() {
    return this.textAlign;
  }
}
