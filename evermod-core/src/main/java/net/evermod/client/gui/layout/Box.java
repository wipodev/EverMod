package net.evermod.client.gui.layout;

import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.ParentComponent;
import net.evermod.client.gui.UIComponent;

/**
 * A container layout that stacks child components directly on top of each other
 * within its bounding box, controlling their relative alignment on both axes.
 *
 * @author Wipodev
 */
public class Box extends ParentComponent {

  private LayoutAlignment horizontalAlignment = LayoutAlignment.START;
  private LayoutAlignment verticalAlignment = LayoutAlignment.START;
  private int padding = 0;

  /**
   * Constructs a Box layout with explicit position and bounds.
   *
   * @param x      Screen X position in pixels.
   * @param y      Screen Y position in pixels.
   * @param width  Container width in pixels.
   * @param height Container height in pixels.
   */
  public Box(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  /**
   * Constructs a Box layout with explicit dimensions at default origin (0, 0).
   *
   * @param width  Container width in pixels.
   * @param height Container height in pixels.
   */
  public Box(int width, int height) {
    this(0, 0, width, height);
  }

  /**
   * Constructs an empty Box layout at origin with zero dimensions.
   */
  public Box() {
    super();
  }

  // --- ALIGNMENT & PADDING SETTERS ---

  /**
   * Gets the active horizontal alignment.
   *
   * @return Active {@link LayoutAlignment}.
   */
  public LayoutAlignment getHorizontalAlignment() {
    return this.horizontalAlignment;
  }

  /**
   * Sets the horizontal alignment for child components.
   *
   * @param alignment Desired horizontal alignment.
   * @return This container instance for method chaining.
   */
  public Box setHorizontalAlignment(LayoutAlignment alignment) {
    this.horizontalAlignment = alignment != null ? alignment : this.horizontalAlignment;
    updateLayout();
    return this;
  }

  /**
   * Fluent alias for {@link #setHorizontalAlignment(LayoutAlignment)}.
   *
   * @param alignment Desired horizontal alignment.
   * @return This container instance for method chaining.
   */
  public Box horizontalAlignment(LayoutAlignment alignment) {
    return setHorizontalAlignment(alignment);
  }

  /**
   * Gets the active vertical alignment.
   *
   * @return Active {@link LayoutAlignment}.
   */
  public LayoutAlignment getVerticalAlignment() {
    return this.verticalAlignment;
  }

  /**
   * Sets the vertical alignment for child components.
   *
   * @param alignment Desired vertical alignment.
   * @return This container instance for method chaining.
   */
  public Box setVerticalAlignment(LayoutAlignment alignment) {
    this.verticalAlignment = alignment != null ? alignment : this.verticalAlignment;
    updateLayout();
    return this;
  }

  /**
   * Fluent alias for {@link #setVerticalAlignment(LayoutAlignment)}.
   *
   * @param alignment Desired vertical alignment.
   * @return This container instance for method chaining.
   */
  public Box verticalAlignment(LayoutAlignment alignment) {
    return setVerticalAlignment(alignment);
  }

  /**
   * Sets both horizontal and vertical alignments simultaneously.
   *
   * @param horizontal Alignment along the X axis.
   * @param vertical   Alignment along the Y axis.
   * @return This container instance for method chaining.
   */
  public Box align(LayoutAlignment horizontal, LayoutAlignment vertical) {
    this.horizontalAlignment = horizontal != null ? horizontal : this.horizontalAlignment;
    this.verticalAlignment = vertical != null ? vertical : this.verticalAlignment;
    updateLayout();
    return this;
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
   * @param padding Padding in pixels (clamped to non-negative).
   * @return This container instance for method chaining.
   */
  public Box setPadding(int padding) {
    this.padding = Math.max(0, padding);
    updateLayout();
    return this;
  }

  /**
   * Fluent alias for {@link #setPadding(int)}.
   *
   * @param padding Padding in pixels.
   * @return This container instance for method chaining.
   */
  public Box padding(int padding) {
    return setPadding(padding);
  }

  // --- LAYOUT RECALCULATION ---

  /**
   * Recalculates screen positions for all child components according to alignment and padding.
   */
  public void updateLayout() {
    int availWidth = Math.max(0, this.width - (this.padding * 2));
    int availHeight = Math.max(0, this.height - (this.padding * 2));

    for (UIComponent child : this.children) {
      if (!child.isVisible()) {
        continue;
      }

      int childX = this.x + this.padding;
      int childY = this.y + this.padding;

      // Apply horizontal alignment
      switch (this.horizontalAlignment) {
        case CENTER:
          childX += (availWidth - child.getWidth()) / 2;
          break;
        case END:
          childX += availWidth - child.getWidth();
          break;
        case START:
        default:
          break;
      }

      // Apply vertical alignment
      switch (this.verticalAlignment) {
        case CENTER:
          childY += (availHeight - child.getHeight()) / 2;
          break;
        case END:
          childY += availHeight - child.getHeight();
          break;
        case START:
        default:
          break;
      }

      child.setX(childX);
      child.setY(childY);
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
  public void setWidth(int width) {
    super.setWidth(width);
    updateLayout();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHeight(int height) {
    super.setHeight(height);
    updateLayout();
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
