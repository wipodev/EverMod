package net.evermod.client.gui.core;

import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.graphics.font.EverFont;
import net.evermod.client.graphics.geometry.InnerBounds;
import net.evermod.client.graphics.style.Border;
import net.evermod.client.graphics.style.BorderColor;
import net.evermod.client.gui.api.style.ElementStyleable;
import net.minecraft.resources.ResourceLocation;

/**
 * Base class for all renderable and styleable UI elements.
 * Encapsulates the box model, layout rules, and background/border properties.
 *
 * @param <T> Concrete element subtype for fluent method chaining.
 */
public abstract class UIElement<T extends UIElement<T>>
    extends UINode
    implements ElementStyleable<T> {

  // ==========================================
  // Fields: Layout Constraints & Spacing
  // ==========================================
  protected int marginTop;
  protected int marginRight;
  protected int marginBottom;
  protected int marginLeft;

  protected int paddingTop;
  protected int paddingRight;
  protected int paddingBottom;
  protected int paddingLeft;

  protected boolean fillMaxWidth;
  protected boolean fillMaxHeight;

  // ==========================================
  // Fields: Visual & Styling
  // ==========================================
  protected int backgroundColor = 0x00000000;
  protected ResourceLocation backgroundTexture;

  protected Border border = Border.NONE;
  protected BorderColor borderColor = BorderColor.DEFAULT;

  // ==========================================
  // Constructors & Fluent Helper
  // ==========================================
  public UIElement(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public UIElement() {
    super(0, 0, 0, 0);
  }

  @SuppressWarnings("unchecked")
  protected T self() {
    return (T) this;
  }

  /**
   * Calculates the auto-sized content width without borders or padding.
   * Override in child classes to supply intrinsic dimensions.
   */
  protected int calculateContentWidth() {
    return 0;
  }

  /**
   * Calculates the auto-sized content height without borders or padding.
   * Override in child classes to supply intrinsic dimensions.
   */
  protected int calculateContentHeight() {
    return 0;
  }

  /**
   * Recalculates element width and height based on layout bounds and inner content dimensions.
   */
  public void autoSize() {
    if (this.isFillMaxWidth() && this.getParent() != null) {
      this.width = this.getParent().getWidth();
    } else if (this.width == 0) {
      this.width = this.calculateContentWidth() + this.getContentPaddingLeft()
          + this.getContentPaddingRight();
    }

    if (this.isFillMaxHeight() && this.getParent() != null) {
      this.height = this.getParent().getHeight();
    } else if (this.height == 0) {
      this.height = this.calculateContentHeight() + this.getContentPaddingTop()
          + this.getContentPaddingBottom();
    }
  }

  // ==========================================
  // Helpers: Effective Content Padding (Padding + Border Offset)
  // ==========================================

  public int getContentPaddingLeft() {
    int borderOffset = (this.border != null) ? this.border.left() : 0;
    return borderOffset + this.paddingLeft;
  }

  public int getContentPaddingRight() {
    int borderOffset = (this.border != null) ? this.border.right() : 0;
    return borderOffset + this.paddingRight;
  }

  public int getContentPaddingTop() {
    int borderOffset = (this.border != null) ? this.border.top() : 0;
    return borderOffset + this.paddingTop;
  }

  public int getContentPaddingBottom() {
    int borderOffset = (this.border != null) ? this.border.bottom() : 0;
    return borderOffset + this.paddingBottom;
  }

  // ==========================================
  // Fluent API: Position & Bounds
  // ==========================================
  public T position(int x, int y) {
    this.x = x;
    this.y = y;
    return self();
  }

  public T size(int width, int height) {
    this.width = width;
    this.height = height;
    return self();
  }

  public T bounds(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    return self();
  }

  public T visible(boolean visible) {
    this.visible = visible;
    return self();
  }

  public T enabled(boolean enabled) {
    this.enabled = enabled;
    return self();
  }

  // ==========================================
  // Interface: ElementStyleable (Layout Constraints)
  // ==========================================
  @Override
  public T fillMaxSize() {
    this.fillMaxWidth = true;
    this.fillMaxHeight = true;
    return self();
  }

  @Override
  public T fillMaxWidth() {
    this.fillMaxWidth = true;
    return self();
  }

  @Override
  public T fillMaxHeight() {
    this.fillMaxHeight = true;
    return self();
  }

  @Override
  public boolean isFillMaxWidth() {
    return this.fillMaxWidth;
  }

  @Override
  public boolean isFillMaxHeight() {
    return this.fillMaxHeight;
  }

  @Override
  public T margin(int value) {
    return this.margin(value, value, value, value);
  }

  @Override
  public T margin(int horizontal, int vertical) {
    return this.margin(vertical, horizontal, vertical, horizontal);
  }

  @Override
  public T margin(int top, int right, int bottom, int left) {
    this.marginTop = top;
    this.marginRight = right;
    this.marginBottom = bottom;
    this.marginLeft = left;
    return self();
  }

  @Override
  public int getMarginTop() {
    return this.marginTop;
  }

  @Override
  public int getMarginRight() {
    return this.marginRight;
  }

  @Override
  public int getMarginBottom() {
    return this.marginBottom;
  }

  @Override
  public int getMarginLeft() {
    return this.marginLeft;
  }

  @Override
  public T padding(int value) {
    return this.padding(value, value, value, value);
  }

  @Override
  public T padding(int horizontal, int vertical) {
    return this.padding(vertical, horizontal, vertical, horizontal);
  }

  @Override
  public T padding(int top, int right, int bottom, int left) {
    this.paddingTop = top;
    this.paddingRight = right;
    this.paddingBottom = bottom;
    this.paddingLeft = left;
    return self();
  }

  @Override
  public int getPaddingTop() {
    return this.paddingTop;
  }

  @Override
  public int getPaddingRight() {
    return this.paddingRight;
  }

  @Override
  public int getPaddingBottom() {
    return this.paddingBottom;
  }

  @Override
  public int getPaddingLeft() {
    return this.paddingLeft;
  }

  // ==========================================
  // Interface: ElementStyleable (Borders)
  // ==========================================
  @Override
  public T border(Border border, BorderColor color) {
    this.border = border;
    this.borderColor = color;
    return self();
  }

  @Override
  public Border getBorder() {
    return this.border;
  }

  @Override
  public BorderColor getBorderColor() {
    return this.borderColor;
  }

  // ==========================================
  // Interface: ElementStyleable (Backgrounds)
  // ==========================================
  @Override
  public T background(int color) {
    this.backgroundColor = color;
    return self();
  }

  @Override
  public T background(ResourceLocation texture, int tintColor) {
    this.backgroundTexture = texture;
    this.backgroundColor = tintColor;
    return self();
  }

  @Override
  public int getBackgroundColor() {
    return this.backgroundColor;
  }

  @Override
  public ResourceLocation getBackgroundTexture() {
    return this.backgroundTexture;
  }

  // ==========================================
  // Shared Rendering Helpers
  // ==========================================
  protected void renderBackground(
      EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    int currentColor = this.getCurrentBackgroundColor();
    BorderColor currentBorderColor = this.getCurrentBorderColor();
    ResourceLocation currentTexture = this.getCurrentBackgroundTexture();
    boolean hasBorder =
        this.border != null && this.border != Border.NONE && currentBorderColor != null;
    boolean hasSolidColor = ((currentColor >> 24) & 0xFF) > 0;

    if (hasSolidColor) {
      if (hasBorder) {
        graphics.drawRect(
            0, 0, this.width, this.height, currentColor, this.border, currentBorderColor);
      } else {
        graphics.drawRect(0, 0, this.width, this.height, currentColor);
      }
    }

    if (currentTexture != null) {
      if (hasBorder) {
        graphics.drawTexture(currentTexture, 0, 0, this.width, this.height, this.border,
            currentBorderColor);
      } else {
        graphics.drawTexture(currentTexture, 0, 0, this.width, this.height);
      }
    }

    if (hasBorder && !hasSolidColor && currentTexture == null) {
      InnerBounds bounds = InnerBounds.of(this.border, 0, 0, this.width, this.height);
      graphics.fillBorder(bounds, this.border, currentBorderColor);
    }
  }

  protected int getCurrentBackgroundColor() {
    return this.backgroundColor;
  }

  protected ResourceLocation getCurrentBackgroundTexture() {
    return this.backgroundTexture;
  }

  protected BorderColor getCurrentBorderColor() {
    return this.borderColor;
  }

  /**
   * Helper method to obtain the shared font instance across all UI elements.
   *
   * @return active shared EverFont instance
   */
  protected EverFont getFont() {
    return EverGraphics.getSharedFont();
  }
}
