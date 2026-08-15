package net.evermod.client.graphics.style;

/**
 * Immutable record representing 4-sided border thicknesses in pixels.
 * Provides intuitive factory methods inspired by CSS box-model conventions.
 *
 * @param top Thickness of the top border.
 * @param right Thickness of the right border.
 * @param bottom Thickness of the bottom border.
 * @param left Thickness of the left border.
 *
 * @author Wipodev
 */
public record Border(int top, int right, int bottom, int left) {

  /** No border thickness on any side. */
  public static final Border NONE = new Border(0, 0, 0, 0);

  /** Standard 1px uniform border. */
  public static final Border DEFAULT = new Border(1, 1, 1, 1);

  /**
   * Creates a uniform border where all 4 sides share the same thickness.
   *
   * @param thickness Border size in pixels for all sides.
   * @return Border instance.
   */
  public static Border all(int thickness) {
    return new Border(thickness, thickness, thickness, thickness);
  }

  /**
   * Creates a border with thickness applied only to the top edge.
   *
   * @param thickness Top border size in pixels.
   * @return Border instance.
   */
  public static Border top(int thickness) {
    return new Border(thickness, 0, 0, 0);
  }

  /**
   * Creates a border with thickness applied only to the right edge.
   *
   * @param thickness Right border size in pixels.
   * @return Border instance.
   */
  public static Border right(int thickness) {
    return new Border(0, thickness, 0, 0);
  }

  /**
   * Creates a border with thickness applied only to the bottom edge.
   *
   * @param thickness Bottom border size in pixels.
   * @return Border instance.
   */
  public static Border bottom(int thickness) {
    return new Border(0, 0, thickness, 0);
  }

  /**
   * Creates a border with thickness applied only to the left edge.
   *
   * @param thickness Left border size in pixels.
   * @return Border instance.
   */
  public static Border left(int thickness) {
    return new Border(0, 0, 0, thickness);
  }

  /**
   * Creates a border with symmetric vertical (top/bottom) and horizontal (left/right) values.
   *
   * @param vertical Thickness for top and bottom borders.
   * @param horizontal Thickness for left and right borders.
   * @return Border instance.
   */
  public static Border symmetric(int vertical, int horizontal) {
    return new Border(vertical, horizontal, vertical, horizontal);
  }
}
