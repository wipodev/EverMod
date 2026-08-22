package net.evermod.client.gui.widget;

import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.graphics.style.GradientStyle;
import net.evermod.client.gui.api.OverlayProvider;
import net.minecraft.network.chat.Component;

/**
 * Overlay component responsible for rendering context tooltips at cursor positions.
 */
public class Tooltip implements OverlayProvider {
  private Component text;
  private boolean active = false;
  private int width = -1;
  private int height = -1;
  private int backgroundColor = 0xF0100010;
  private int borderColorTop = 0xAA5000FF;
  private int borderColorBottom = 0xAA28007F;
  private int textColor = 0xFFFFFFFF;
  private int padding = 3;

  /**
   * Constructs a Tooltip with text provided as a Minecraft {@link Component}.
   *
   * @param text The text component to display.
   */
  public Tooltip(Component text) {
    this.text = text;
  }

  /**
   * Constructs a Tooltip with literal string content.
   *
   * @param text The string message to display.
   */
  public Tooltip(String text) {
    this(Component.literal(text));
  }

  /**
   * Updates the tooltip content using a {@link Component}.
   *
   * @param text The new text component.
   * @return This tooltip instance for chaining.
   */
  public Tooltip text(Component text) {
    this.text = text;
    return this;
  }

  /**
   * Updates the tooltip content using a literal string.
   *
   * @param text The new text string.
   * @return This tooltip instance for chaining.
   */
  public Tooltip text(String text) {
    return text(Component.literal(text));
  }

  /**
   * Sets custom fixed dimensions for the tooltip overlay.
   *
   * @param width The width in pixels (-1 for auto-calculation).
   * @param height The height in pixels (-1 for auto-calculation).
   * @return This tooltip instance for chaining.
   */
  public Tooltip size(int width, int height) {
    this.width = width;
    this.height = height;
    return this;
  }

  /**
   * Configures visual color attributes for the tooltip.
   *
   * @param background ARGB background color.
   * @param border ARGB from color.
   * @param border ARGB to color.
   * @param text ARGB text color.
   * @return This tooltip instance for chaining.
   */
  public Tooltip colors(int background, int borderFrom, int borderTo, int text) {
    this.backgroundColor = background;
    this.borderColorTop = borderFrom;
    this.borderColorBottom = borderTo;
    this.textColor = text;
    return this;
  }

  public Tooltip padding(int padding) {
    this.padding = padding;
    return this;
  }

  /**
   * Sets whether the tooltip is active and ready to render.
   *
   * @param active True to show, false to hide.
   */
  public void setActive(boolean active) {
    this.active = active;
  }

  @Override
  public boolean isOverlayActive() {
    return this.active && this.text != null && !this.text.getString().isEmpty();
  }

  @Override
  public void renderOverlay(EverGraphics graphics, int mouseX, int mouseY) {
    if (!this.isOverlayActive()) {
      return;
    }

    var font = graphics.getFont();
    String content = this.text.getString();
    int renderWidth = (this.width > 0) ? this.width : font.width(content) + (this.padding * 2);
    int renderHeight = (this.height > 0) ? this.height : font.fontHeight() + (this.padding * 2);
    int offsetX = 12;
    int offsetY = -12;
    int renderX = mouseX + offsetX;
    int renderY = mouseY + offsetY;

    GradientStyle bgStyle = GradientStyle.solid(this.backgroundColor);
    GradientStyle topStyle = GradientStyle.solid(this.borderColorTop);
    GradientStyle bottomStyle = GradientStyle.solid(this.borderColorBottom);
    GradientStyle sideGradient =
        GradientStyle.gradient(this.borderColorTop, this.borderColorBottom,
            GradientStyle.Direction.VERTICAL);

    graphics.drawFramedGradientRect(renderX, renderY, renderWidth, renderHeight, bgStyle, topStyle,
        sideGradient, bottomStyle, sideGradient);

    int textX = renderX + this.padding;
    int textY = renderY + (renderHeight - font.fontHeight()) / 2;
    graphics.drawString(content, textX, textY, this.textColor, true);
  }
}
