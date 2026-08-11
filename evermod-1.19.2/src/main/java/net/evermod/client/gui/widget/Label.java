package net.evermod.client.gui.widget;

import net.evermod.client.gui.AbstractComponent;
import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.layout.LayoutAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

/**
 * Primitive text widget for displaying plain strings or rich Minecraft Components.
 * Supports auto-sizing, custom ARGB colors, drop shadows, and horizontal text alignment.
 *
 * @author Wipodev
 */
public class Label extends AbstractComponent {

  private String text;
  private Component component;
  private int color = 0xFFFFFFFF;
  private boolean shadow = true;
  private LayoutAlignment alignment = LayoutAlignment.START;

  /**
   * Constructs a Label displaying a formatted Minecraft Component text.
   * Automatically calculates initial dimensions based on the component's width and font height.
   *
   * @param component Formatted Minecraft Component text.
   */
  public Label(Component component) {
    super(0, 0, 0, 0);
    this.component = component;
    this.text = null;
    autoSize();
  }

  /**
   * Constructs a Label displaying a plain string text.
   * Automatically calculates initial dimensions based on the text string's width and font height.
   *
   * @param text Plain string text.
   */
  public Label(String text) {
    super(0, 0, 0, 0);
    this.text = text != null ? text : "";
    this.component = null;
    autoSize();
  }

  // --- AUTO-SIZING UTILITY ---

  /**
   * Automatically calculates and updates the component width and height
   * using Minecraft's active font metrics.
   */
  public void autoSize() {
    Font font = Minecraft.getInstance().font;
    if (this.component != null) {
      this.width = font.width(this.component);
    } else if (this.text != null) {
      this.width = font.width(this.text);
    } else {
      this.width = 0;
    }
    this.height = font.lineHeight; // Default font height (9 pixels)
  }

  // --- GETTERS & SETTERS (FLUENT API) ---

  /**
   * Gets the current plain text string, or its visual text string representation if a Component is used.
   *
   * @return Text string representation.
   */
  public String getText() {
    if (this.component != null) {
      return this.component.getString();
    }
    return this.text != null ? this.text : "";
  }

  /**
   * Sets new plain string text and recalculates text bounding dimensions.
   *
   * @param text Plain text string.
   * @return This label instance for method chaining.
   */
  public Label setText(String text) {
    this.text = text != null ? text : "";
    this.component = null;
    autoSize();
    return this;
  }

  /**
   * Fluent alias for {@link #setText(String)}.
   *
   * @param text Plain text string.
   * @return This label instance for method chaining.
   */
  public Label text(String text) {
    return setText(text);
  }

  /**
   * Gets the current rich Minecraft Component text.
   *
   * @return Active Minecraft Component, or {@code null} if plain text is used.
   */
  public Component getComponent() {
    return this.component;
  }

  /**
   * Sets new rich Minecraft Component text and recalculates text bounding dimensions.
   *
   * @param component Formatted Minecraft Component text.
   * @return This label instance for method chaining.
   */
  public Label setComponent(Component component) {
    this.component = component;
    this.text = null;
    autoSize();
    return this;
  }

  /**
   * Fluent alias for {@link #setComponent(Component)}.
   *
   * @param component Formatted Minecraft Component text.
   * @return This label instance for method chaining.
   */
  public Label component(Component component) {
    return setComponent(component);
  }

  /**
   * Gets the current ARGB rendering color code.
   *
   * @return ARGB hex color code.
   */
  public int getColor() {
    return this.color;
  }

  /**
   * Sets the ARGB text rendering color.
   *
   * @param color ARGB hex color code (e.g., {@code 0xFFFFFFFF}).
   * @return This label instance for method chaining.
   */
  public Label setColor(int color) {
    this.color = color;
    return this;
  }

  /**
   * Fluent alias for {@link #setColor(int)}.
   *
   * @param color ARGB hex color code.
   * @return This label instance for method chaining.
   */
  public Label color(int color) {
    return setColor(color);
  }

  /**
   * Checks whether the text drop shadow rendering is enabled.
   *
   * @return {@code true} if drop shadow is rendered, {@code false} otherwise.
   */
  public boolean isShadow() {
    return this.shadow;
  }

  /**
   * Sets whether to render a drop shadow behind the text.
   *
   * @param shadow {@code true} to enable drop shadow, {@code false} to disable it.
   * @return This label instance for method chaining.
   */
  public Label setShadow(boolean shadow) {
    this.shadow = shadow;
    return this;
  }

  /**
   * Fluent alias for {@link #setShadow(boolean)}.
   *
   * @param shadow {@code true} to enable drop shadow, {@code false} to disable it.
   * @return This label instance for method chaining.
   */
  public Label shadow(boolean shadow) {
    return setShadow(shadow);
  }

  /**
   * Gets the horizontal text alignment relative to the widget bounding box.
   *
   * @return Active {@link LayoutAlignment}.
   */
  public LayoutAlignment getAlignment() {
    return this.alignment;
  }

  /**
   * Sets the horizontal text alignment within the component bounds.
   *
   * @param alignment Desired horizontal {@link LayoutAlignment}.
   * @return This label instance for method chaining.
   */
  public Label setAlignment(LayoutAlignment alignment) {
    this.alignment = alignment != null ? alignment : this.alignment;
    return this;
  }

  /**
   * Fluent alias for {@link #setAlignment(LayoutAlignment)}.
   *
   * @param alignment Desired horizontal {@link LayoutAlignment}.
   * @return This label instance for method chaining.
   */
  public Label alignment(LayoutAlignment alignment) {
    return setAlignment(alignment);
  }

  // --- RENDERING METHOD ---

  /**
   * Renders the label text onto the screen using {@link EverGraphics}, taking into account
   * visibility, bounding box alignment, color, and drop shadow settings.
   *
   * @param graphics The graphics pipeline context wrapper.
   * @param mouseX Current mouse cursor X coordinate.
   * @param mouseY Current mouse cursor Y coordinate.
   * @param partialTicks Partial tick time for animation frame interpolation.
   */
  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    int renderX = this.x;
    int textWidth = (this.component != null) ? graphics.getFont().width(this.component)
        : graphics.getFont().width(this.text != null ? this.text : "");

    // Apply internal horizontal alignment according to bounding box width
    switch (this.alignment) {
      case CENTER:
        renderX += (this.width - textWidth) / 2;
        break;
      case END:
        renderX += (this.width - textWidth);
        break;
      case START:
      default:
        break;
    }

    // Delegate rendering directly to EverGraphics
    if (this.component != null) {
      graphics.drawString(this.component, renderX, this.y, this.color, this.shadow);
    } else if (this.text != null) {
      graphics.drawString(this.text, renderX, this.y, this.color, this.shadow);
    }
  }
}
