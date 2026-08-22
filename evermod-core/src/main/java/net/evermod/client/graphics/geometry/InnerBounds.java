package net.evermod.client.graphics.geometry;

import net.evermod.client.graphics.style.Border;

/**
 * Holds 2D spatial boundary coordinates for outer and inner bounds of a UI element.
 *
 * @param x Outer top-left X coordinate.
 * @param y Outer top-left Y coordinate.
 * @param x2 Outer bottom-right X coordinate.
 * @param y2 Outer bottom-right Y coordinate.
 * @param innerX Inner top-left X coordinate.
 * @param innerY Inner top-left Y coordinate.
 * @param innerX2 Inner bottom-right X coordinate.
 * @param innerY2 Inner bottom-right Y coordinate.
 *
 * @author Wipodev
 */
public record InnerBounds(int x, int y, int x2, int y2, int innerX, int innerY, int innerX2,
    int innerY2) {

  /**
   * Expands all boundary coordinates outward by a given offset.
   *
   * @param amount pixel offset to expand
   * @return new expanded {@link InnerBounds} instance
   */
  public InnerBounds expand(int amount) {
    return new InnerBounds(
        this.x - amount,
        this.y - amount,
        this.x2 + amount,
        this.y2 + amount,
        this.innerX - amount,
        this.innerY - amount,
        this.innerX2 + amount,
        this.innerY2 + amount);
  }

  /**
   * Computes spatial boundary coordinates based on outer bounds and border specifications.
   *
   * @param border border layout specifications (can be null)
   * @param x top-left X coordinate of the outer boundary
   * @param y top-left Y coordinate of the outer boundary
   * @param width total outer width
   * @param height total outer height
   * @return computed {@link InnerBounds} instance
   */
  public static InnerBounds of(Border border, int x, int y, int width, int height) {
    int left = (border != null) ? border.left() : 0;
    int top = (border != null) ? border.top() : 0;
    int right = (border != null) ? border.right() : 0;
    int bottom = (border != null) ? border.bottom() : 0;

    int x2 = x + width;
    int y2 = y + height;
    int innerX = x + left;
    int innerY = y + top;
    int innerX2 = x2 - right;
    int innerY2 = y2 - bottom;

    return new InnerBounds(x, y, x2, y2, innerX, innerY, innerX2, innerY2);
  }
}
