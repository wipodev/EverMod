package net.evermod.client.gui.widget;

import net.evermod.client.gui.Border;
import net.evermod.client.gui.BorderColor;
import net.evermod.client.gui.EverGraphics;

/**
 * Concrete implementation of AbstractSlider rendering vector/flat style tracks and handles.
 * Uses normalized values for rendering calculations aligned with Minecraft GUI mechanics.
 *
 * @author Wipodev
 */
public class Slider extends AbstractSlider {

  private int trackHeight = 4;
  private int backgroundColor = 0xFF222222;
  private int filledColor = 0xFF3B82F6;
  private int handleColor = 0xFFFFFFFF;
  private int handleHoverColor = 0xFFE0E0E0;

  private Border border = Border.DEFAULT;
  private BorderColor borderColor = BorderColor.all(0xFF555555);

  /**
   * Constructs a Slider widget with explicit position, dimensions, and numerical bounds.
   *
   * @param x            Screen X position in pixels.
   * @param y            Screen Y position in pixels.
   * @param width        Slider width in pixels.
   * @param height       Slider height in pixels.
   * @param minValue     Minimum numeric bound.
   * @param maxValue     Maximum numeric bound.
   * @param defaultValue Initial starting value.
   */
  public Slider(int x, int y, int width, int height, double minValue, double maxValue,
      double defaultValue) {
    super(x, y, width, height, minValue, maxValue, defaultValue);
  }

  /**
   * Constructs a Slider widget at origin (0, 0) with default dimensions (120x20).
   */
  public Slider(double minValue, double maxValue, double defaultValue) {
    super(minValue, maxValue, defaultValue);
  }

  // --- SUBCLASS SPECIFIC FLUENT BUILDERS ---

  public Slider setTrackHeight(int trackHeight) {
    this.trackHeight = trackHeight;
    return this;
  }

  public Slider trackHeight(int trackHeight) {
    return setTrackHeight(trackHeight);
  }

  public Slider setColors(int background, int filled, int handle, int handleHover) {
    this.backgroundColor = background;
    this.filledColor = filled;
    this.handleColor = handle;
    this.handleHoverColor = handleHover;
    return this;
  }

  public Slider colors(int background, int filled, int handle, int handleHover) {
    return setColors(background, filled, handle, handleHover);
  }

  public Slider setBorder(Border border, BorderColor borderColor) {
    this.border = border;
    this.borderColor = borderColor;
    return this;
  }

  public Slider border(Border border, BorderColor borderColor) {
    return setBorder(border, borderColor);
  }

  // --- RENDERING TEMPLATE ---

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    // 1. Calculate Track Boundaries
    int trackY = this.y + (this.height - this.trackHeight) / 2;

    // 2. Render Track Background
    graphics.drawRect(this.x, trackY, this.width, this.trackHeight, this.backgroundColor,
        this.border, this.borderColor);

    // 3. Render Track Active Fill (uses normalized value)
    int handleX = this.x + getHandleXOffset();
    int filledWidth = handleX - this.x + (this.handleWidth / 2);
    if (filledWidth > 0) {
      graphics.drawRect(this.x, trackY, filledWidth, this.trackHeight, this.filledColor);
    }

    // 4. Calculate Handle Coordinates & Hover State
    int handleY = this.y + (this.height - this.handleHeight) / 2;
    boolean handleHovered = mouseX >= handleX && mouseX <= handleX + this.handleWidth
        && mouseY >= handleY && mouseY <= handleY + this.handleHeight;

    int currentHandleColor = (handleHovered || (isMouseOver(mouseX, mouseY) && this.enabled))
        ? this.handleHoverColor
        : this.handleColor;

    // 5. Render Handle
    graphics.drawRect(handleX, handleY, this.handleWidth, this.handleHeight, currentHandleColor,
        this.border, this.borderColor);
  }
}
