package net.evermod.client.gui.layout;

import net.evermod.client.gui.UIComponent;

/**
 * Static factory utility class for creating layout containers and spatial utilities.
 * Provides a declarative DSL-style API for UI construction.
 *
 * @author Wipodev
 */
public final class Layouts {

  /**
   * Private constructor to strictly prevent instantiation of this utility class.
   */
  private Layouts() {
    // Utility class; instantiation strictly prevented
  }

  // --- COLUMN FACTORIES ---

  /**
   * Creates a default {@link Column} layout with zero gap and zero padding.
   *
   * @return A new {@link Column} instance.
   */
  public static Column column() {
    return new Column();
  }

  /**
   * Creates a {@link Column} layout with a specified vertical gap between children.
   *
   * @param gap Vertical spacing in pixels between child components.
   * @return A new {@link Column} instance.
   */
  public static Column column(int gap) {
    return new Column(gap);
  }

  /**
   * Creates a {@link Column} layout with specified gap and padding at origin (0, 0).
   *
   * @param gap     Vertical spacing in pixels between child components.
   * @param padding Outer inner-padding in pixels.
   * @return A new {@link Column} instance.
   */
  public static Column column(int gap, int padding) {
    return new Column(0, 0, gap, padding);
  }

  /**
   * Creates a {@link Column} layout with explicit screen position, gap, and padding.
   *
   * @param x       Screen X position in pixels.
   * @param y       Screen Y position in pixels.
   * @param gap     Vertical spacing in pixels between child components.
   * @param padding Outer inner-padding in pixels.
   * @return A new {@link Column} instance.
   */
  public static Column column(int x, int y, int gap, int padding) {
    return new Column(x, y, gap, padding);
  }

  // --- ROW FACTORIES ---

  /**
   * Creates a default {@link Row} layout with zero gap and zero padding.
   *
   * @return A new {@link Row} instance.
   */
  public static Row row() {
    return new Row();
  }

  /**
   * Creates a {@link Row} layout with a specified horizontal gap between children.
   *
   * @param gap Horizontal spacing in pixels between child components.
   * @return A new {@link Row} instance.
   */
  public static Row row(int gap) {
    return new Row(gap);
  }

  /**
   * Creates a {@link Row} layout with specified gap and padding at origin (0, 0).
   *
   * @param gap     Horizontal spacing in pixels between child components.
   * @param padding Outer inner-padding in pixels.
   * @return A new {@link Row} instance.
   */
  public static Row row(int gap, int padding) {
    return new Row(0, 0, gap, padding);
  }

  /**
   * Creates a {@link Row} layout with explicit screen position, gap, and padding.
   *
   * @param x       Screen X position in pixels.
   * @param y       Screen Y position in pixels.
   * @param gap     Horizontal spacing in pixels between child components.
   * @param padding Outer inner-padding in pixels.
   * @return A new {@link Row} instance.
   */
  public static Row row(int x, int y, int gap, int padding) {
    return new Row(x, y, gap, padding);
  }

  // --- BOX FACTORIES ---

  /**
   * Creates a default empty {@link Box} layout at origin (0, 0).
   *
   * @return A new {@link Box} instance.
   */
  public static Box box() {
    return new Box();
  }

  /**
   * Creates a {@link Box} layout with specified dimensions at origin (0, 0).
   *
   * @param width  Container width in pixels.
   * @param height Container height in pixels.
   * @return A new {@link Box} instance.
   */
  public static Box box(int width, int height) {
    return new Box(width, height);
  }

  /**
   * Creates a {@link Box} layout with explicit position and dimensions.
   *
   * @param x      Screen X position in pixels.
   * @param y      Screen Y position in pixels.
   * @param width  Container width in pixels.
   * @param height Container height in pixels.
   * @return A new {@link Box} instance.
   */
  public static Box box(int x, int y, int width, int height) {
    return new Box(x, y, width, height);
  }

  // --- SCROLLABLE FACTORIES ---

  /**
   * Creates a default {@link Scrollable} container at origin (0, 0) with zero dimensions.
   *
   * @return A new {@link Scrollable} instance.
   */
  public static Scrollable scrollable() {
    return new Scrollable();
  }

  /**
   * Creates a {@link Scrollable} container with explicit viewport dimensions at origin (0, 0).
   *
   * @param width  Viewport width in pixels.
   * @param height Viewport height in pixels.
   * @return A new {@link Scrollable} instance.
   */
  public static Scrollable scrollable(int width, int height) {
    return new Scrollable(width, height);
  }

  /**
   * Creates a {@link Scrollable} container with explicit position and viewport dimensions.
   *
   * @param x      Screen X position in pixels.
   * @param y      Screen Y position in pixels.
   * @param width  Viewport width in pixels.
   * @param height Viewport height in pixels.
   * @return A new {@link Scrollable} instance.
   */
  public static Scrollable scrollable(int x, int y, int width, int height) {
    return new Scrollable(x, y, width, height);
  }

  /**
   * Creates a {@link Scrollable} container with predefined content and viewport dimensions.
   *
   * @param content Inner component to be made scrollable.
   * @param width   Viewport width in pixels.
   * @param height  Viewport height in pixels.
   * @return A new {@link Scrollable} instance containing the content.
   */
  public static Scrollable scrollable(UIComponent content, int width, int height) {
    return new Scrollable(width, height).setContent(content);
  }

  // --- SPACE FACTORIES ---

  /**
   * Creates a {@link Space} component with explicit width and height.
   *
   * @param width  Spacer width in pixels.
   * @param height Spacer height in pixels.
   * @return A new {@link Space} instance.
   */
  public static Space space(int width, int height) {
    return new Space(width, height);
  }

  /**
   * Creates a square {@link Space} component with equal width and height.
   *
   * @param size Square size in pixels.
   * @return A new {@link Space} instance.
   */
  public static Space space(int size) {
    return Space.of(size);
  }

  /**
   * Creates a horizontal {@link Space} component with zero height.
   *
   * @param width Spacer width in pixels.
   * @return A new {@link Space} instance.
   */
  public static Space spaceWidth(int width) {
    return Space.width(width);
  }

  /**
   * Creates a vertical {@link Space} component with zero width.
   *
   * @param height Spacer height in pixels.
   * @return A new {@link Space} instance.
   */
  public static Space spaceHeight(int height) {
    return Space.height(height);
  }

  // --- DIVIDER FACTORIES ---

  /**
   * Creates a default 1px horizontal {@link Divider}.
   *
   * @return A new horizontal {@link Divider} instance.
   */
  public static Divider divider() {
    return Divider.horizontal();
  }

  /**
   * Creates a default 1px horizontal {@link Divider}.
   *
   * @return A new horizontal {@link Divider} instance.
   */
  public static Divider horizontalDivider() {
    return Divider.horizontal();
  }

  /**
   * Creates a horizontal {@link Divider} with custom thickness and color.
   *
   * @param thickness Line thickness in pixels.
   * @param color     ARGB hex color code.
   * @return A new horizontal {@link Divider} instance.
   */
  public static Divider horizontalDivider(int thickness, int color) {
    return Divider.horizontal(thickness, color);
  }

  /**
   * Creates a default 1px vertical {@link Divider}.
   *
   * @return A new vertical {@link Divider} instance.
   */
  public static Divider verticalDivider() {
    return Divider.vertical();
  }

  /**
   * Creates a vertical {@link Divider} with custom thickness and color.
   *
   * @param thickness Line thickness in pixels.
   * @param color     ARGB hex color code.
   * @return A new vertical {@link Divider} instance.
   */
  public static Divider verticalDivider(int thickness, int color) {
    return Divider.vertical(thickness, color);
  }
}
