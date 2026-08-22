package net.evermod.client.gui.api.style;

/**
 * Interface for elements that can dynamically expand within their parent layout constraints.
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface Sizable<T extends Sizable<T>> {

  /**
   * Instructs the element to expand and occupy all available width and height in its parent.
   *
   * @return This element instance for method chaining.
   */
  T fillMaxSize();

  /**
   * Instructs the element to expand and occupy all available width in its parent.
   *
   * @return This element instance for method chaining.
   */
  T fillMaxWidth();

  /**
   * Instructs the element to expand and occupy all available height in its parent.
   *
   * @return This element instance for method chaining.
   */
  T fillMaxHeight();

  /**
   * Checks if this element is set to fill available width.
   *
   * @return {@code true} if filling max width.
   */
  boolean isFillMaxWidth();

  /**
   * Checks if this element is set to fill available height.
   *
   * @return {@code true} if filling max height.
   */
  boolean isFillMaxHeight();
}
