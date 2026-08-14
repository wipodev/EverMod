package net.evermod.client.gui.widget;

import net.evermod.client.gui.Border;
import net.evermod.client.gui.BorderColor;
import net.evermod.client.gui.EverGraphics;

/**
 * Customizable UI Checkbox component.
 * Renders a box with an optional check indicator and adjacent text label.
 *
 * @author Wipodev
 */
public class Checkbox extends AbstractCheckbox {

  private String label = "";
  private int boxSize = 14;
  private int boxColor = 0xFF222222;
  private int checkColor = 0xFF55FF55;
  private int textColor = 0xFFFFFFFF;
  private int borderColor = 0xFF888888;

  /**
   * Constructs a Checkbox with position, dimensions, label, and initial state.
   *
   * @param x Initial X position.
   * @param y Initial Y position.
   * @param width Total component width.
   * @param height Total component height.
   * @param label Text displayed next to the checkbox.
   * @param initialValue Initial checked status.
   */
  public Checkbox(int x, int y, int width, int height, String label, boolean initialValue) {
    super(x, y, width, height, initialValue);
    this.label = label;
  }

  /**
   * Constructs a Checkbox at origin (0, 0) with zero dimensions.
   */
  public Checkbox() {
    super(0, 0, 0, 0, false);
  }

  // --- FLUENT SETTERS ---

  public Checkbox label(String label) {
    this.label = label;
    return this;
  }

  public Checkbox boxSize(int size) {
    this.boxSize = size;
    return this;
  }

  public Checkbox colors(int boxColor, int checkColor, int textColor) {
    this.boxColor = boxColor;
    this.checkColor = checkColor;
    this.textColor = textColor;
    return this;
  }

  // --- RENDERING ---

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    // Centering the box vertically relative to component height
    int boxY = this.y + (this.height - this.boxSize) / 2;

    // Outer border & background box
    graphics.drawRect(this.x, boxY, this.boxSize, this.boxSize, this.boxColor, Border.all(1),
        BorderColor.all(this.borderColor));

    // Inner check indicator if enabled
    if (this.checked) {
      int pad = 3;
      graphics.drawRect(
          this.x + pad,
          boxY + pad,
          this.boxSize - (pad * 2),
          this.boxSize - (pad * 2),
          this.checkColor);
    }

    // Render optional text label
    if (this.label != null && !this.label.isEmpty()) {
      int textX = this.x + this.boxSize + 6;
      int textY = this.y + (this.height - 8) / 2; // Center 8px default Minecraft font height
      graphics.drawString(this.label, textX, textY, this.textColor, true);
    }
  }
}
