package net.evermod.client.gui.layout;

import net.evermod.client.gui.ParentComponent;
import net.evermod.client.gui.UIComponent;
import net.evermod.client.gui.EverGraphics;

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
   * @param x      Screen X position.
   * @param y      Screen Y position.
   * @param width  Container width.
   * @param height Container height.
   */
  public Box(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public Box(int width, int height) {
    this(0, 0, width, height);
  }

  /**
   * Constructs a Box layout at default origin.
   */
  public Box() {
    super();
  }

  // --- ALIGNMENT & PADDING SETTERS ---

  public LayoutAlignment getHorizontalAlignment() {
    return this.horizontalAlignment;
  }

  public Box setHorizontalAlignment(LayoutAlignment alignment) {
    this.horizontalAlignment = alignment != null ? alignment : this.horizontalAlignment;
    updateLayout();
    return this;
  }

  public Box horizontalAlignment(LayoutAlignment alignment) {
    return setHorizontalAlignment(alignment);
  }

  public LayoutAlignment getVerticalAlignment() {
    return this.verticalAlignment;
  }

  public Box setVerticalAlignment(LayoutAlignment alignment) {
    this.verticalAlignment = alignment != null ? alignment : this.verticalAlignment;
    updateLayout();
    return this;
  }

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

  public int getPadding() {
    return this.padding;
  }

  public Box setPadding(int padding) {
    this.padding = Math.max(0, padding);
    updateLayout();
    return this;
  }

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

  @Override
  public ParentComponent addChild(UIComponent child) {
    super.addChild(child);
    updateLayout();
    return this;
  }

  @Override
  public void setWidth(int width) {
    super.setWidth(width);
    updateLayout();
  }

  @Override
  public void setHeight(int height) {
    super.setHeight(height);
    updateLayout();
  }

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    updateLayout();
    super.render(graphics, mouseX, mouseY, partialTicks);
  }
}
