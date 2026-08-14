package net.evermod.client.gui.layout;

import net.evermod.client.gui.AbstractComponent;
import net.evermod.client.gui.EverGraphics;

/**
 * A decorative layout divider component used to draw thin horizontal or vertical
 * lines between UI sections.
 *
 * @author Wipodev
 */
public class Divider extends AbstractComponent {

  /**
   * Orientation modes available for line rendering.
   */
  public enum Orientation {
    HORIZONTAL,
    VERTICAL
  }

  private Orientation orientation;
  private int thickness;
  private int color;

  /**
   * Constructs a Divider with explicit orientation, thickness, and color.
   *
   * @param orientation Line orientation (HORIZONTAL or VERTICAL).
   * @param thickness   Line thickness in pixels (minimum 1).
   * @param color       ARGB hex color code.
   */
  public Divider(Orientation orientation, int thickness, int color) {
    super(0, 0, 0, 0);
    this.orientation = orientation;
    this.thickness = Math.max(1, thickness);
    this.color = color;
    applyDimensions();
  }

  /**
   * Constructs a horizontal Divider with 1px thickness and a default dark gray color (0xFF444444).
   */
  public Divider() {
    this(Orientation.HORIZONTAL, 1, 0xFF444444);
  }

  // --- FACTORY METHODS ---

  /**
   * Creates a horizontal divider with custom thickness and ARGB color.
   *
   * @param thickness Line thickness in pixels.
   * @param color     ARGB hex color code.
   * @return A new {@link Divider} instance.
   */
  public static Divider horizontal(int thickness, int color) {
    return new Divider(Orientation.HORIZONTAL, thickness, color);
  }

  /**
   * Creates a horizontal divider with custom thickness and default dark color.
   *
   * @param thickness Line thickness in pixels.
   * @return A new {@link Divider} instance.
   */
  public static Divider horizontal(int thickness) {
    return new Divider(Orientation.HORIZONTAL, thickness, 0xFF444444);
  }

  /**
   * Creates a default 1px horizontal divider.
   *
   * @return A new {@link Divider} instance.
   */
  public static Divider horizontal() {
    return new Divider(Orientation.HORIZONTAL, 1, 0xFF444444);
  }

  /**
   * Creates a vertical divider with custom thickness and ARGB color.
   *
   * @param thickness Line thickness in pixels.
   * @param color     ARGB hex color code.
   * @return A new {@link Divider} instance.
   */
  public static Divider vertical(int thickness, int color) {
    return new Divider(Orientation.VERTICAL, thickness, color);
  }

  /**
   * Creates a vertical divider with custom thickness and default dark color.
   *
   * @param thickness Line thickness in pixels.
   * @return A new {@link Divider} instance.
   */
  public static Divider vertical(int thickness) {
    return new Divider(Orientation.VERTICAL, thickness, 0xFF444444);
  }

  /**
   * Creates a default 1px vertical divider.
   *
   * @return A new {@link Divider} instance.
   */
  public static Divider vertical() {
    return new Divider(Orientation.VERTICAL, 1, 0xFF444444);
  }

  // --- GETTERS & SETTERS ---

  /**
   * Gets current orientation.
   *
   * @return Active {@link Orientation}.
   */
  public Orientation getOrientation() {
    return this.orientation;
  }

  /**
   * Sets line orientation and recalculates dimensions.
   *
   * @param orientation New orientation.
   * @return This divider instance for method chaining.
   */
  public Divider setOrientation(Orientation orientation) {
    this.orientation = orientation != null ? orientation : this.orientation;
    applyDimensions();
    return this;
  }

  /**
   * Gets current line thickness.
   *
   * @return Thickness in pixels.
   */
  public int getThickness() {
    return this.thickness;
  }

  /**
   * Sets line thickness and recalculates dimensions.
   *
   * @param thickness Line thickness in pixels (clamped to at least 1).
   * @return This divider instance for method chaining.
   */
  public Divider setThickness(int thickness) {
    this.thickness = Math.max(1, thickness);
    applyDimensions();
    return this;
  }

  /**
   * Gets line color.
   *
   * @return ARGB hex color code.
   */
  public int getColor() {
    return this.color;
  }

  /**
   * Sets line rendering color.
   *
   * @param color ARGB hex color code.
   * @return This divider instance for method chaining.
   */
  public Divider setColor(int color) {
    this.color = color;
    return this;
  }

  /**
   * Adjusts component dimensions based on orientation and thickness.
   */
  private void applyDimensions() {
    if (this.orientation == Orientation.HORIZONTAL) {
      this.height = this.thickness;
    } else {
      this.width = this.thickness;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    graphics.drawRect(this.x, this.y, this.width, this.height, this.color);
  }
}
