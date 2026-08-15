package net.evermod.client.graphics.geometry;

import net.evermod.client.graphics.style.Border;

/**
 * Calculates and holds inner content boundaries and border metrics for UI elements,
 * eliminating redundant margin and padding math across renderers.
 *
 * @author Wipodev
 */
public record InnerBounds(
    int left,
    int top,
    int right,
    int bottom,
    int innerX,
    int innerY,
    int innerWidth,
    int innerHeight,
    int x2,
    int y2,
    int innerX2,
    int innerY2) {

  /**
   * Computes the inner boundary coordinates and metrics based on outer bounds and border widths.
   *
   * @param border the border layout specifications (can be null)
   * @param x top-left X coordinate of the outer boundary
   * @param y top-left Y coordinate of the outer boundary
   * @param width total outer width
   * @param height total outer height
   * @return computed {@link InnerBounds} instance
   */
  public static InnerBounds of(Border border, int x, int y, int width, int height) {
    int l = (border != null) ? border.left() : 0;
    int t = (border != null) ? border.top() : 0;
    int r = (border != null) ? border.right() : 0;
    int b = (border != null) ? border.bottom() : 0;

    int iX = x + l;
    int iY = y + t;
    int iWidth = Math.max(0, width - l - r);
    int iHeight = Math.max(0, height - t - b);

    int outerX2 = x + width;
    int outerY2 = y + height;
    int iX2 = outerX2 - r;
    int iY2 = outerY2 - b;

    return new InnerBounds(l, t, r, b, iX, iY, iWidth, iHeight, outerX2, outerY2, iX2, iY2);
  }
}
