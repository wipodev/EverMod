package net.evermod.client.gui.api.style;

import net.evermod.client.gui.layout.LayoutAlignment;

/**
 * Interface defining alignment and spacing rules for container children along the cross-axis.
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface Alignable<T extends Alignable<T>> {

  /**
   * Sets the cross-axis alignment for child components within this container.
   *
   * @param alignment The alignment behavior (START, CENTER, END).
   * @return This element instance for method chaining.
   */
  T align(LayoutAlignment alignment);

  /**
   * Sets the spacing gap between consecutive children along the main axis.
   *
   * @param pixels Spacing gap in pixels.
   * @return This element instance for method chaining.
   */
  T gap(int pixels);

  /**
   * Gets the configured cross-axis alignment.
   *
   * @return The cross-axis layout alignment.
   */
  LayoutAlignment getAlignment();

  /**
   * Gets the main-axis spacing gap.
   *
   * @return The gap in pixels.
   */
  int getGap();
}
