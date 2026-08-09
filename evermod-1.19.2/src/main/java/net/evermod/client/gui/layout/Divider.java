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

  public enum Orientation {
    HORIZONTAL, VERTICAL
  }

  private Orientation orientation;
  private int thickness;
  private int color;

  /**
   * Constructs a Divider with explicit orientation, thickness, and color.
   *
   * @param orientation Line orientation (HORIZONTAL or VERTICAL).
   * @param thickness   Line thickness in pixels.
   * @param color       ARGB hex color.
   */
  public Divider(Orientation orientation, int thickness, int color) {
    super(0, 0, 0, 0);
    this.orientation = orientation;
    this.thickness = Math.max(1, thickness);
    this.color = color;
    applyDimensions();
  }

  /**
   * Constructs a horizontal Divider with 1px thickness and a default dark color.
   */
  public Divider() {
    this(Orientation.HORIZONTAL, 1, 0xFF444444);
  }

  // --- FACTORY METHODS ---

  public static Divider horizontal(int thickness, int color) {
    return new Divider(Orientation.HORIZONTAL, thickness, color);
  }

  public static Divider horizontal(int thickness) {
    return new Divider(Orientation.HORIZONTAL, thickness, 0xFF444444);
  }

  public static Divider horizontal() {
    return new Divider(Orientation.HORIZONTAL, 1, 0xFF444444);
  }

  public static Divider vertical(int thickness, int color) {
    return new Divider(Orientation.VERTICAL, thickness, color);
  }

  public static Divider vertical(int thickness) {
    return new Divider(Orientation.VERTICAL, thickness, 0xFF444444);
  }

  public static Divider vertical() {
    return new Divider(Orientation.VERTICAL, 1, 0xFF444444);
  }

  // --- GETTERS & SETTERS ---

  public Orientation getOrientation() {
    return this.orientation;
  }

  public Divider setOrientation(Orientation orientation) {
    this.orientation = orientation != null ? orientation : this.orientation;
    applyDimensions();
    return this;
  }

  public int getThickness() {
    return this.thickness;
  }

  public Divider setThickness(int thickness) {
    this.thickness = Math.max(1, thickness);
    applyDimensions();
    return this;
  }

  public int getColor() {
    return this.color;
  }

  public Divider setColor(int color) {
    this.color = color;
    return this;
  }

  private void applyDimensions() {
    if (this.orientation == Orientation.HORIZONTAL) {
      this.height = this.thickness;
    } else {
      this.width = this.thickness;
    }
  }

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    graphics.fill(this.x, this.y, this.width, this.height, this.color);
  }
}
