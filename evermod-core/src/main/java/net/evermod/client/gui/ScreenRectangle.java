package net.evermod.client.gui;

/**
 * Represents a screen rectangle for UI clipping operations using doubles
 * to preserve sub-pixel accuracy before final viewport scaling.
 *
 * @author Wipodev
 */
public record ScreenRectangle(double x, double y, double width, double height) {

  public static final ScreenRectangle EMPTY = new ScreenRectangle(0.0, 0.0, 0.0, 0.0);

  /**
   * Calculates the intersection between this rectangle and another.
   * Returns EMPTY if they do not overlap.
   */
  public ScreenRectangle intersection(ScreenRectangle other) {
    if (other == null) {
      return this;
    }

    double newX = Math.max(this.x, other.x);
    double newY = Math.max(this.y, other.y);
    double newRight = Math.min(this.x + this.width, other.x + other.width);
    double newBottom = Math.min(this.y + this.height, other.y + other.height);

    double newWidth = newRight - newX;
    double newHeight = newBottom - newY;

    if (newWidth <= 0.0 || newHeight <= 0.0) {
      return EMPTY;
    }

    return new ScreenRectangle(newX, newY, newWidth, newHeight);
  }

  public double bottomEdge() {
    return this.y + this.height;
  }
}
