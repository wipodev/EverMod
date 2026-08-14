package net.evermod.client.gui.widget;

import java.util.function.Consumer;
import net.evermod.client.gui.AbstractComponent;
import net.evermod.client.gui.Border;
import net.evermod.client.gui.BorderColor;
import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.layout.LayoutAlignment;
import net.evermod.client.gui.render.IEverFont;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

/**
 * Abstract base class for button widgets handling mouse interactions, hover states,
 * sound effects, and text rendering alignment. Supports multi-colored border configurations.
 *
 * @author Wipodev
 */
public abstract class AbstractButton extends AbstractComponent {

  protected String text = "button";
  protected Component component;
  protected int textColor = 0xFFFFFFFF;
  protected int hoverTextColor = 0xFFFFFFFF;
  protected int disabledTextColor = 0xFFA0A0A0;
  protected boolean shadow = true;
  protected LayoutAlignment alignment = LayoutAlignment.CENTER;
  protected boolean playSound = true;
  protected Border border = null;
  protected BorderColor borderColor = BorderColor.DEFAULT;
  protected BorderColor hoverBorderColor = BorderColor.all(0xFFFFFFFF);
  protected BorderColor disabledBorderColor = BorderColor.all(0xFF555555);
  protected Consumer<AbstractButton> onClickHandler;

  /**
   * Constructs an AbstractButton with explicit origin position and dimensions.
   *
   * @param x      Screen X position in pixels.
   * @param y      Screen Y position in pixels.
   * @param width  Button width in pixels.
   * @param height Button height in pixels.
   */
  public AbstractButton(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  /**
   * Constructs an AbstractButton at origin (0, 0) with default dimensions (150x20).
   */
  public AbstractButton() {
    this(0, 0, 150, 20);
  }

  // --- AUTO-SIZING UTILITY ---

  /**
   * Automatically calculates and updates button dimensions based on text metrics.
   *
   * @param <T>                Concrete button type for method chaining.
   * @param horizontalPadding Horizontal padding in pixels to apply on both sides.
   * @return This button instance for method chaining.
   */
  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T autoSize(int horizontalPadding) {
    Font font = Minecraft.getInstance().font;
    int textWidth = 0;

    if (this.component != null) {
      textWidth = font.width(this.component);
    } else if (this.text != null) {
      textWidth = font.width(this.text);
    }

    int borderWidthPadding = (this.border != null) ? (this.border.left() + this.border.right()) : 0;
    int borderHeightPadding =
        (this.border != null) ? (this.border.top() + this.border.bottom()) : 0;

    this.width = textWidth + (horizontalPadding * 2) + borderWidthPadding;
    this.height = font.lineHeight + 8 + borderHeightPadding;
    return (T) this;
  }

  // --- GETTERS & SETTERS (FLUENT API) ---

  public String getText() {
    if (this.component != null) {
      return this.component.getString();
    }
    return this.text != null ? this.text : "";
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setText(String text) {
    this.text = text != null ? text : "";
    this.component = null;
    return (T) this;
  }

  public <T extends AbstractButton> T text(String text) {
    return setText(text);
  }

  public <T extends AbstractButton> T label(String label) {
    return setText(label);
  }

  public Component getComponent() {
    return this.component;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setComponent(Component component) {
    this.component = component;
    this.text = null;
    return (T) this;
  }

  public <T extends AbstractButton> T text(Component component) {
    return setComponent(component);
  }

  public <T extends AbstractButton> T label(Component component) {
    return setComponent(component);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setTextColor(int color) {
    this.textColor = color;
    return (T) this;
  }

  public <T extends AbstractButton> T textColor(int color) {
    return setTextColor(color);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setTextColors(int normal, int hover, int disabled) {
    this.textColor = normal;
    this.hoverTextColor = hover;
    this.disabledTextColor = disabled;
    return (T) this;
  }

  public <T extends AbstractButton> T textColors(int normal, int hover, int disabled) {
    return setTextColors(normal, hover, disabled);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setShadow(boolean shadow) {
    this.shadow = shadow;
    return (T) this;
  }

  public <T extends AbstractButton> T shadow(boolean shadow) {
    return setShadow(shadow);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setAlignment(LayoutAlignment alignment) {
    this.alignment = alignment != null ? alignment : this.alignment;
    return (T) this;
  }

  public <T extends AbstractButton> T alignment(LayoutAlignment alignment) {
    return setAlignment(alignment);
  }

  public Border getBorder() {
    return this.border;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setBorder(Border border, BorderColor borderColor) {
    this.border = border;
    this.borderColor = borderColor;
    return (T) this;
  }

  public <T extends AbstractButton> T border(Border border, BorderColor borderColor) {
    return setBorder(border, borderColor);
  }

  public <T extends AbstractButton> T border(Border border, int borderColor) {
    return setBorder(border, BorderColor.all(borderColor));
  }

  public <T extends AbstractButton> T border(int border, BorderColor borderColor) {
    return setBorder(new Border(border, border, border, border), borderColor);
  }

  public <T extends AbstractButton> T border(int border, int borderColor) {
    return setBorder(new Border(border, border, border, border), BorderColor.all(borderColor));
  }

  public <T extends AbstractButton> T border(int border) {
    return setBorder(new Border(border, border, border, border), this.borderColor);
  }

  public <T extends AbstractButton> T border(BorderColor borderColor) {
    return setBorder(Border.DEFAULT, borderColor);
  }

  public <T extends AbstractButton> T borderColor(int borderColor) {
    return border(BorderColor.all(borderColor));
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setBorderColors(BorderColor normal, BorderColor hover,
      BorderColor disabled) {
    this.borderColor = normal;
    this.hoverBorderColor = hover;
    this.disabledBorderColor = disabled;
    return (T) this;
  }

  public <T extends AbstractButton> T borderColors(BorderColor normal, BorderColor hover,
      BorderColor disabled) {
    return setBorderColors(normal, hover, disabled);
  }

  public <T extends AbstractButton> T borderColors(int normal, int hover, int disabled) {
    return setBorderColors(BorderColor.all(normal), BorderColor.all(hover),
        BorderColor.all(disabled));
  }

  /**
   * Helper method to evaluate active border color configuration based on component state.
   *
   * @param hovered Whether the button is currently hovered.
   * @return The active {@link BorderColor} configuration.
   */
  protected BorderColor getActiveBorderColor(boolean hovered) {
    if (!this.enabled) {
      return this.disabledBorderColor;
    }
    return hovered ? this.hoverBorderColor : this.borderColor;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setPlaySound(boolean playSound) {
    this.playSound = playSound;
    return (T) this;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T setOnClick(Consumer<AbstractButton> handler) {
    this.onClickHandler = handler;
    return (T) this;
  }

  public <T extends AbstractButton> T onClick(Consumer<AbstractButton> handler) {
    return setOnClick(handler);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractButton> T enabled(boolean enabled) {
    this.setEnabled(enabled);
    return (T) this;
  }

  // --- INPUT INTERACTION METHODS ---

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.visible && this.enabled && isMouseOver((int) mouseX, (int) mouseY) && button == 0) {
      if (this.playSound) {
        Minecraft.getInstance().getSoundManager().play(
            SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
      }
      if (this.onClickHandler != null) {
        this.onClickHandler.accept(this);
      }
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  // --- RENDERING TEMPLATE ---

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    boolean hovered = isMouseOver(mouseX, mouseY) && this.enabled;

    renderBackground(graphics, mouseX, mouseY, hovered);
    renderLabel(graphics, hovered);
  }

  /**
   * Abstract method implemented by concrete subclasses to draw custom backgrounds (colors, textures, etc.).
   *
   * @param graphics Custom graphics context wrapper.
   * @param mouseX   Current cursor X position.
   * @param mouseY   Current cursor Y position.
   * @param hovered  Whether the mouse cursor is hovering over the button.
   */
  protected abstract void renderBackground(EverGraphics graphics, int mouseX, int mouseY,
      boolean hovered);

  /**
   * Renders the button text label positioned according to the active {@link LayoutAlignment}.
   *
   * @param graphics Custom graphics context wrapper.
   * @param hovered  Whether the mouse cursor is hovering over the button.
   */
  protected void renderLabel(EverGraphics graphics, boolean hovered) {
    String labelText = getText();
    IEverFont font = graphics.getFont();

    int borderLeft = (this.border != null) ? this.border.left() : 0;
    int borderRight = (this.border != null) ? this.border.right() : 0;
    int borderTop = (this.border != null) ? this.border.top() : 0;
    int borderBottom = (this.border != null) ? this.border.bottom() : 0;

    int usableWidth = this.width - borderLeft - borderRight;
    int usableHeight = this.height - borderTop - borderBottom;

    int textWidth = (this.component != null) ? font.width(this.component) : font.width(labelText);
    int renderX = this.x + borderLeft;

    switch (this.alignment) {
      case CENTER:
        renderX += (usableWidth - textWidth) / 2;
        break;
      case END:
        renderX += (usableWidth - textWidth) - 4;
        break;
      case START:
      default:
        renderX += 4;
        break;
    }

    int renderY = this.y + borderTop + (usableHeight - font.fontHeight()) / 2;
    int activeTextColor = !this.enabled ? this.disabledTextColor
        : (hovered ? this.hoverTextColor : this.textColor);

    if (this.component != null) {
      graphics.drawString(this.component, renderX, renderY, activeTextColor, this.shadow);
    } else if (!labelText.isEmpty()) {
      graphics.drawString(labelText, renderX, renderY, activeTextColor, this.shadow);
    }
  }
}
