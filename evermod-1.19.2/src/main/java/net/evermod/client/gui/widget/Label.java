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

  /** Plain text representation. Null if rich Component is provided. */
  private String text;

  /** Rich formatted Minecraft component text. Null if plain text is provided. */
  private Component component;

  /** Text ARGB color code. Defaults to solid white (0xFFFFFFFF). */
  private int color = 0xFFFFFFFF;

  /** Flag determining whether a drop shadow should be rendered behind the text. */
  private boolean shadow = true;

  /** Text alignment relative to the component bounds. Defaults to START. */
  private LayoutAlignment alignment = LayoutAlignment.START;

  // --- CONSTRUCTORS ---

  /**
   * Constructs a Label with a rich Minecraft Component.
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
   * Constructs a Label with plain string text.
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
   * Automatically calculates and updates component width and height
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
   * Gets the current plain text string, or its visual text representation if Component is used.
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
   * Sets new plain string text and recalculates text bounds.
   *
   * @param text Plain text.
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
   * @param text Plain text.
   * @return This label instance for method chaining.
   */
  public Label text(String text) {
    return setText(text);
  }

  /**
   * Gets the current rich Minecraft Component text.
   *
   * @return Minecraft Component or null if plain text is used.
   */
  public Component getComponent() {
    return this.component;
  }

  /**
   * Sets new rich Minecraft Component text and recalculates text bounds.
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
   * Gets the current ARGB color.
   *
   * @return ARGB color code.
   */
  public int getColor() {
    return this.color;
  }

  /**
   * Sets the ARGB text rendering color.
   *
   * @param color ARGB hex color code.
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
   * Checks if drop shadow is enabled.
   *
   * @return True if drop shadow is enabled.
   */
  public boolean isShadow() {
    return this.shadow;
  }

  /**
   * Sets whether to render a drop shadow behind text.
   *
   * @param shadow True to draw shadow.
   * @return This label instance for method chaining.
   */
  public Label setShadow(boolean shadow) {
    this.shadow = shadow;
    return this;
  }

  /**
   * Fluent alias for {@link #setShadow(boolean)}.
   *
   * @param shadow True to draw shadow.
   * @return This label instance for method chaining.
   */
  public Label shadow(boolean shadow) {
    return setShadow(shadow);
  }

  /**
   * Gets horizontal text alignment.
   *
   * @return Active {@link LayoutAlignment}.
   */
  public LayoutAlignment getAlignment() {
    return this.alignment;
  }

  /**
   * Sets horizontal text alignment within the component bounds.
   *
   * @param alignment Desired text alignment.
   * @return This label instance for method chaining.
   */
  public Label setAlignment(LayoutAlignment alignment) {
    this.alignment = alignment != null ? alignment : this.alignment;
    return this;
  }

  /**
   * Fluent alias for {@link #setAlignment(LayoutAlignment)}.
   *
   * @param alignment Desired text alignment.
   * @return This label instance for method chaining.
   */
  public Label alignment(LayoutAlignment alignment) {
    return setAlignment(alignment);
  }

  // --- RENDERING METHOD ---

  /**
   * {@inheritDoc}
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
