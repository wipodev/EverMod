package net.evermod.client.gui.api.style;

/**
 * Interface defining inner spacing modifications (padding) around element contents.
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface Paddable<T extends Paddable<T>> {

  /**
   * Sets uniform padding for all four sides.
   *
   * @param value Padding in pixels.
   * @return This element instance for method chaining.
   */
  T padding(int value);

  /**
   * Sets horizontal and vertical padding.
   *
   * @param horizontal Left and right padding in pixels.
   * @param vertical   Top and bottom padding in pixels.
   * @return This element instance for method chaining.
   */
  T padding(int horizontal, int vertical);

  /**
   * Sets individual padding for all four sides in CSS order (top, right, bottom, left).
   *
   * @param top    Top padding in pixels.
   * @param right  Right padding in pixels.
   * @param bottom Bottom padding in pixels.
   * @param left   Left padding in pixels.
   * @return This element instance for method chaining.
   */
  T padding(int top, int right, int bottom, int left);

  /**
   * Gets the top padding.
   *
   * @return Top padding in pixels.
   */
  int getPaddingTop();

  /**
   * Gets the right padding.
   *
   * @return Right padding in pixels.
   */
  int getPaddingRight();

  /**
   * Gets the bottom padding.
   *
   * @return Bottom padding in pixels.
   */
  int getPaddingBottom();

  /**
   * Gets the left padding.
   *
   * @return Left padding in pixels.
   */
  int getPaddingLeft();
}
