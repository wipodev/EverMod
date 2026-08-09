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

  private int gap;
  private int padding;
  private LayoutAlignment alignment = LayoutAlignment.START;
  private boolean fillWidth = false;
  private boolean fillHeight = false;

  public Row(int x, int y, int gap, int padding) {
    super(x, y, 0, 0);
    this.gap = gap;
    this.padding = padding;
  }

  public Row(int gap) {
    this(0, 0, gap, 0);
  }

  public Row() {
    this(0, 0, 0, 0);
  }

  // --- CONFIGURATION GETTERS & SETTERS ---

  public int getGap() {
    return this.gap;
  }

  public Row setGap(int gap) {
    this.gap = gap;
    updateLayout();
    return this;
  }

  public Row gap(int gap) {
    return setGap(gap);
  }

  public int getPadding() {
    return this.padding;
  }

  public Row setPadding(int padding) {
    this.padding = padding;
    updateLayout();
    return this;
  }

  public Row padding(int padding) {
    return setPadding(padding);
  }

  public LayoutAlignment getAlignment() {
    return this.alignment;
  }

  public Row setAlignment(LayoutAlignment alignment) {
    this.alignment = alignment != null ? alignment : this.alignment;
    updateLayout();
    return this;
  }

  // --- SIZING UTILITIES ---

  /**
   * Forces this layout to fill the specified width and height bounds.
   *
   * @param targetWidth  Maximum width to occupy.
   * @param targetHeight Maximum height to occupy.
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
   * @param targetWidth Maximum width to occupy.
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
   * @param targetHeight Maximum height to occupy.
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

  @Override
  public ParentComponent addChild(UIComponent child) {
    super.addChild(child);
    updateLayout();
    return this;
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
