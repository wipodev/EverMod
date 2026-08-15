package net.evermod.client.graphics.style;

/**
 * Represents ARGB colors for each individual side of a component border.
 *
 * @param top    ARGB color for the top border edge.
 * @param right  ARGB color for the right border edge.
 * @param bottom ARGB color for the bottom border edge.
 * @param left   ARGB color for the left border edge.
 */
public record BorderColor(int top, int right, int bottom, int left) {

  private static final int DEFAULT_COLOR = 0xFF000000;
  public static final BorderColor DEFAULT =
      new BorderColor(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR);

  /**
   * Creates a BorderColor with the same color applied to all four sides.
   *
   * @param color ARGB color for all sides.
   * @return A new BorderColor instance.
   */
  public static BorderColor all(int color) {
    return new BorderColor(color, color, color, color);
  }

  /**
   * Creates a BorderColor with separate colors for vertical and horizontal edges.
   *
   * @param vertical   ARGB color for top and bottom edges.
   * @param horizontal ARGB color for left and right edges.
   * @return A new BorderColor instance.
   */
  public static BorderColor symmetric(int vertical, int horizontal) {
    return new BorderColor(vertical, horizontal, vertical, horizontal);
  }

  /**
   * Creates a BorderColor with only the top edge set, defaulting others to black.
   *
   * @param color ARGB color for the top edge.
   * @return A new BorderColor instance.
   */
  public static BorderColor top(int color) {
    return new BorderColor(color, DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR);
  }

  /**
   * Creates a BorderColor with only the right edge set, defaulting others to black.
   *
   * @param color ARGB color for the right edge.
   * @return A new BorderColor instance.
   */
  public static BorderColor right(int color) {
    return new BorderColor(DEFAULT_COLOR, color, DEFAULT_COLOR, DEFAULT_COLOR);
  }

  /**
   * Creates a BorderColor with only the bottom edge set, defaulting others to black.
   *
   * @param color ARGB color for the bottom edge.
   * @return A new BorderColor instance.
   */
  public static BorderColor bottom(int color) {
    return new BorderColor(DEFAULT_COLOR, DEFAULT_COLOR, color, DEFAULT_COLOR);
  }

  /**
   * Creates a BorderColor with only the left edge set, defaulting others to black.
   *
   * @param color ARGB color for the left edge.
   * @return A new BorderColor instance.
   */
  public static BorderColor left(int color) {
    return new BorderColor(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR, color);
  }
}
