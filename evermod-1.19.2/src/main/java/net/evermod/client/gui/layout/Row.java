package net.evermod.client.gui.layout;

import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.ParentComponent;
import net.evermod.client.gui.UIComponent;

/**
 * Horizontal container layout that places child components side-by-side left to right.
 * Computes layout positions based on gap, padding, cross-axis alignment, and sizing flags.
 *
 * @author Wipodev
 */
public class Row extends ParentComponent {

  /** Spacing in pixels between adjacent child components horizontally. */
  private int gap;

  /** Outer inner-padding surrounding child components in pixels. */
  private int padding;

  /** Cross-axis (vertical) alignment for children. Defaults to START. */
  private LayoutAlignment alignment = LayoutAlignment.START;

  /** Flag indicating whether the row stretches/fixes its width rather than auto-wrapping. */
  private boolean fillWidth = false;

  /** Flag indicating whether the row stretches/fixes its height rather than auto-wrapping. */
  private boolean fillHeight = false;

  /**
   * Constructs a Row layout with explicit origin position, gap, and padding.
   *
   * @param x       Screen X position in pixels.
   * @param y       Screen Y position in pixels.
   * @param gap     Horizontal spacing between child components in pixels.
   * @param padding Outer inner-padding in pixels.
   */
  public Row(int x, int y, int gap, int padding) {
    super(x, y, 0, 0);
    this.gap = gap;
    this.padding = padding;
  }

  /**
   * Constructs a Row layout at origin (0, 0) with a specified gap and zero padding.
   *
   * @param gap Horizontal spacing between child components in pixels.
   */
  public Row(int gap) {
    this(0, 0, gap, 0);
  }

  /**
   * Constructs an empty Row layout at origin (0, 0) with zero gap and zero padding.
   */
  public Row() {
    this(0, 0, 0, 0);
  }

  // --- CONFIGURATION GETTERS & SETTERS ---

  /**
   * Gets the horizontal gap between child components.
   *
   * @return Spacing in pixels.
   */
  public int getGap() {
    return this.gap;
  }

  /**
   * Sets the horizontal gap between child components.
   *
   * @param gap Spacing in pixels.
   * @return This container instance for method chaining.
   */
  public Row setGap(int gap) {
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
  public Row gap(int gap) {
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
  public Row setPadding(int padding) {
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
  public Row padding(int padding) {
    return setPadding(padding);
  }

  /**
   * Gets cross-axis (vertical) alignment.
   *
   * @return Active {@link LayoutAlignment}.
   */
  public LayoutAlignment getAlignment() {
    return this.alignment;
  }

  /**
   * Sets cross-axis (vertical) alignment for child components.
   *
   * @param alignment Desired vertical alignment.
   * @return This container instance for method chaining.
   */
  public Row setAlignment(LayoutAlignment alignment) {
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
  public Row fillMaxSize(int targetWidth, int targetHeight) {
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
  public Row fillMaxWidth(int targetWidth) {
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
  public Row fillMaxHeight(int targetHeight) {
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
    int currentX = this.x + this.padding;
    int maxHeight = 0;

    for (UIComponent child : this.children) {
      if (!child.isVisible()) {
        continue;
      }

      // Set X position relative to cumulative horizontal flow
      child.setX(currentX);

      // Compute maximum height across all visible children
      if (child.getHeight() > maxHeight) {
        maxHeight = child.getHeight();
      }

      // Advance cumulative width for next child
      currentX += child.getWidth() + this.gap;
    }

    // Subtract extra trailing spacing if there were children
    if (!this.children.isEmpty()) {
      currentX -= this.gap;
    }

    // If height is not fixed, wrap around the maximum content height
    int availableHeight =
        this.fillHeight ? Math.max(0, this.height - (this.padding * 2)) : maxHeight;

    // Apply cross-axis (Y) alignment
    for (UIComponent child : this.children) {
      if (!child.isVisible()) {
        continue;
      }

      switch (this.alignment) {
        case CENTER:
          child.setY(this.y + this.padding + (availableHeight - child.getHeight()) / 2);
          break;
        case END:
          child.setY(this.y + this.padding + (availableHeight - child.getHeight()));
          break;
        case START:
        default:
          child.setY(this.y + this.padding);
          break;
      }
    }

    // Wrap dimensions if explicit fill flags are disabled
    if (!this.fillWidth) {
      this.width = (currentX - this.x) + this.padding;
    }
    if (!this.fillHeight) {
      this.height = maxHeight + (this.padding * 2);
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
