package net.evermod.client.gui.layout;

import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.ParentComponent;
import net.evermod.client.gui.UIComponent;

/**
 * Vertical container layout that stacks child components top to bottom.
 * Computes layout positions based on gap, padding, cross-axis alignment, and sizing flags.
 *
 * @author Wipodev
 */
public class Column extends ParentComponent {

  private int gap;
  private int padding;
  private LayoutAlignment alignment = LayoutAlignment.START;
  private boolean fillWidth = false;
  private boolean fillHeight = false;

  /**
   * Constructs a Column layout with explicit origin position, gap, and padding.
   *
   * @param x       Screen X position in pixels.
   * @param y       Screen Y position in pixels.
   * @param gap     Vertical spacing between child components in pixels.
   * @param padding Outer inner-padding in pixels.
   */
  public Column(int x, int y, int gap, int padding) {
    super(x, y, 0, 0);
    this.gap = gap;
    this.padding = padding;
  }

  /**
   * Constructs a Column layout at origin (0, 0) with a specified gap and zero padding.
   *
   * @param gap Vertical spacing between child components in pixels.
   */
  public Column(int gap) {
    this(0, 0, gap, 0);
  }

  /**
   * Constructs an empty Column layout at origin (0, 0) with zero gap and zero padding.
   */
  public Column() {
    this(0, 0, 0, 0);
  }

  // --- CONFIGURATION GETTERS & SETTERS ---

  /**
   * Gets the vertical gap between child components.
   *
   * @return Spacing in pixels.
   */
  public int getGap() {
    return this.gap;
  }

  /**
   * Sets the vertical gap between child components.
   *
   * @param gap Spacing in pixels.
   * @return This container instance for method chaining.
   */
  public Column setGap(int gap) {
    this.gap = gap;
    updateLayout();
    return this;
  }

  /**
   * Fluent alias for {@link #setGap(int)}.
   *
   * @param gap Spacing in pixels.
   * @return This container instance for method chaining.
   */
  public Column gap(int gap) {
    return setGap(gap);
  }

  /**
   * Gets current inner padding.
   *
   * @return Padding size in pixels.
   */
  public int getPadding() {
    return this.padding;
  }

  /**
   * Sets container inner padding.
   *
   * @param padding Padding size in pixels.
   * @return This container instance for method chaining.
   */
  public Column setPadding(int padding) {
    this.padding = padding;
    updateLayout();
    return this;
  }

  /**
   * Fluent alias for {@link #setPadding(int)}.
   *
   * @param padding Padding size in pixels.
   * @return This container instance for method chaining.
   */
  public Column padding(int padding) {
    return setPadding(padding);
  }

  /**
   * Gets cross-axis (horizontal) alignment.
   *
   * @return Active {@link LayoutAlignment}.
   */
  public LayoutAlignment getAlignment() {
    return this.alignment;
  }

  /**
   * Sets cross-axis (horizontal) alignment for child components.
   *
   * @param alignment Desired horizontal alignment.
   * @return This container instance for method chaining.
   */
  public Column setAlignment(LayoutAlignment alignment) {
    this.alignment = alignment != null ? alignment : this.alignment;
    updateLayout();
    return this;
  }

  // --- SIZING UTILITIES ---

  /**
   * Forces this layout to fill the specified width and height bounds.
   *
   * @param targetWidth  Maximum width to occupy in pixels.
   * @param targetHeight Maximum height to occupy in pixels.
   * @return This container instance for method chaining.
   */
  public Column fillMaxSize(int targetWidth, int targetHeight) {
    this.width = targetWidth;
    this.height = targetHeight;
    this.fillWidth = true;
    this.fillHeight = true;
    updateLayout();
    return this;
  }

  /**
   * Forces this layout to fill a specified width.
   *
   * @param targetWidth Maximum width to occupy in pixels.
   * @return This container instance for method chaining.
   */
  public Column fillMaxWidth(int targetWidth) {
    this.width = targetWidth;
    this.fillWidth = true;
    updateLayout();
    return this;
  }

  /**
   * Forces this layout to fill a specified height.
   *
   * @param targetHeight Maximum height to occupy in pixels.
   * @return This container instance for method chaining.
   */
  public Column fillMaxHeight(int targetHeight) {
    this.height = targetHeight;
    this.fillHeight = true;
    updateLayout();
    return this;
  }

  // --- LAYOUT RECALCULATION ---

  /**
   * Recalculates relative positions and dimensions for all child components.
   */
  public void updateLayout() {
    int currentY = this.y + this.padding;
    int maxWidth = 0;

    for (UIComponent child : this.children) {
      if (!child.isVisible()) {
        continue;
      }

      // Set Y position relative to cumulative vertical flow
      child.setY(currentY);

      // Compute maximum width across all visible children
      if (child.getWidth() > maxWidth) {
        maxWidth = child.getWidth();
      }

      // Advance cumulative height for next child
      currentY += child.getHeight() + this.gap;
    }

    // Subtract extra trailing spacing if there were children
    if (!this.children.isEmpty()) {
      currentY -= this.gap;
    }

    // If width is not fixed, wrap around the maximum content width
    int availableWidth = this.fillWidth ? Math.max(0, this.width - (this.padding * 2)) : maxWidth;

    // Apply cross-axis (X) alignment
    for (UIComponent child : this.children) {
      if (!child.isVisible()) {
        continue;
      }

      switch (this.alignment) {
        case CENTER:
          child.setX(this.x + this.padding + (availableWidth - child.getWidth()) / 2);
          break;
        case END:
          child.setX(this.x + this.padding + (availableWidth - child.getWidth()));
          break;
        case START:
        default:
          child.setX(this.x + this.padding);
          break;
      }
    }

    // Wrap dimensions if explicit fill flags are disabled
    if (!this.fillWidth) {
      this.width = maxWidth + (this.padding * 2);
    }
    if (!this.fillHeight) {
      this.height = (currentY - this.y) + this.padding;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ParentComponent addChild(UIComponent child) {
    super.addChild(child);
    updateLayout();
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    updateLayout();
    super.render(graphics, mouseX, mouseY, partialTicks);
  }
}
