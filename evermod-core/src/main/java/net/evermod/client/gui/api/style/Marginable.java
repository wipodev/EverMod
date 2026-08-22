package net.evermod.client.gui.api.style;

/**
 * Interface defining outer spacing modifications (margin) around the element.
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface Marginable<T extends Marginable<T>> {

  /**
   * Sets uniform margin for all four sides.
   *
   * @param value Margin in pixels.
   * @return This element instance for method chaining.
   */
  T margin(int value);

  /**
   * Sets horizontal and vertical margin.
   *
   * @param horizontal Left and right margin in pixels.
   * @param vertical   Top and bottom margin in pixels.
   * @return This element instance for method chaining.
   */
  T margin(int horizontal, int vertical);

  /**
   * Sets individual margin for all four sides in CSS order (top, right, bottom, left).
   *
   * @param top    Top margin in pixels.
   * @param right  Right margin in pixels.
   * @param bottom Bottom margin in pixels.
   * @param left   Left margin in pixels.
   * @return This element instance for method chaining.
   */
  T margin(int top, int right, int bottom, int left);

  /**
   * Gets the top margin.
   *
   * @return Top margin in pixels.
   */
  int getMarginTop();

  /**
   * Gets the right margin.
   *
   * @return Right margin in pixels.
   */
  int getMarginRight();

  /**
   * Gets the bottom margin.
   *
   * @return Bottom margin in pixels.
   */
  int getMarginBottom();

  /**
   * Gets the left margin.
   *
   * @return Left margin in pixels.
   */
  int getMarginLeft();
}
