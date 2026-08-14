package net.evermod.client.gui.widget;

import net.evermod.client.gui.Border;
import net.evermod.client.gui.BorderColor;
import net.evermod.client.gui.EverGraphics;
import net.minecraft.network.chat.Component;

/**
 * Solid color implementation of {@link AbstractButton} supporting ARGB background states and custom borders with {@link BorderColor}.
 *
 * @author Wipodev
 */
public class SolidButton extends AbstractButton {

  private int backgroundColor = 0x80000000;
  private int hoverBackgroundColor = 0x80555555;
  private int disabledBackgroundColor = 0x40333333;

  /**
   * Constructs a SolidButton with default properties at origin (0, 0) and default size (150x20).
   */
  public SolidButton() {
    super();
  }

  /**
   * Constructs a SolidButton with a custom primary ARGB background color.
   *
   * @param color ARGB background color code.
   */
  public SolidButton(int color) {
    super();
    this.backgroundColor = color;
  }

  /**
   * Constructs a SolidButton with a plain text label.
   *
   * @param text Text string to display on the button label.
   */
  public SolidButton(String text) {
    super();
    setText(text);
  }

  /**
   * Constructs a SolidButton with a plain text label and a custom primary background color.
   *
   * @param text  Text string to display on the button label.
   * @param color ARGB background color code.
   */
  public SolidButton(String text, int color) {
    super();
    setText(text);
    this.backgroundColor = color;
  }

  /**
   * Constructs a SolidButton with a styled Component label.
   *
   * @param component Text Component to display on the button label.
   */
  public SolidButton(Component component) {
    super();
    setComponent(component);
  }

  /**
   * Constructs a SolidButton with a styled Component label and a custom primary background color.
   *
   * @param component Text Component to display on the button label.
   * @param color     ARGB background color code.
   */
  public SolidButton(Component component, int color) {
    super();
    setComponent(component);
    this.backgroundColor = color;
  }

  /**
   * Sets the primary ARGB background color.
   *
   * @param color ARGB color code.
   * @return This button instance for method chaining.
   */
  public SolidButton setBackgroundColor(int color) {
    this.backgroundColor = color;
    return this;
  }

  /**
   * Fluent API alias for {@link #setBackgroundColor(int)}.
   *
   * @param color ARGB color code.
   * @return This button instance for method chaining.
   */
  public SolidButton backgroundColor(int color) {
    return setBackgroundColor(color);
  }

  /**
   * Sets background colors for normal, hover, and disabled button states.
   *
   * @param normal   ARGB color code for normal state.
   * @param hover    ARGB color code for hover state.
   * @param disabled ARGB color code for disabled state.
   * @return This button instance for method chaining.
   */
  public SolidButton setBackgroundColors(int normal, int hover, int disabled) {
    this.backgroundColor = normal;
    this.hoverBackgroundColor = hover;
    this.disabledBackgroundColor = disabled;
    return this;
  }

  /**
   * Fluent API alias for {@link #setBackgroundColors(int, int, int)}.
   *
   * @param normal   ARGB color code for normal state.
   * @param hover    ARGB color code for hover state.
   * @param disabled ARGB color code for disabled state.
   * @return This button instance for method chaining.
   */
  public SolidButton backgroundColors(int normal, int hover, int disabled) {
    return setBackgroundColors(normal, hover, disabled);
  }

  @Override
  protected void renderBackground(EverGraphics graphics, int mouseX, int mouseY, boolean hovered) {
    int activeColor = !this.enabled ? this.disabledBackgroundColor
        : (hovered ? this.hoverBackgroundColor : this.backgroundColor);

    BorderColor activeBorderColor = getActiveBorderColor(hovered);

    if (this.border != null && this.border != Border.NONE && activeBorderColor != null) {
      graphics.drawRect(this.x, this.y, this.width, this.height, activeColor, this.border,
          activeBorderColor);
    } else {
      graphics.drawRect(this.x, this.y, this.width, this.height, activeColor);
    }
  }
}
