package net.evermod.client.gui.widget;

import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.graphics.font.EverFont;
import net.evermod.client.gui.api.style.TextAlignment;
import net.evermod.client.gui.core.AbstractWidget;
import net.minecraft.network.chat.Component;

/**
 * Base label component providing text measurement, positioning, and rendering logic.
 * Supports generic fluent API inheritance for subclasses like Button.
 *
 * @param <T> self-referencing widget type for fluent interface chaining
 */
public abstract class AbstractLabel<T extends AbstractLabel<T>> extends AbstractWidget<T> {

  private Component text;

  public AbstractLabel(Component text) {
    super(0, 0, 0, 0);
    this.text = text;
  }

  public AbstractLabel(String text) {
    this(Component.literal(text));
  }

  public AbstractLabel() {
    this(Component.empty());
  }

  @Override
  protected int calculateContentWidth() {
    if (this.text == null || this.text.getString().isEmpty()) {
      return 0;
    }
    return this.getFont().width(this.text);
  }

  @Override
  protected int calculateContentHeight() {
    return this.getFont().fontHeight();
  }

  public String getText() {
    return this.text != null ? this.text.getString() : "";
  }

  public Component getComponent() {
    return this.text;
  }

  public T text(String text) {
    this.text = Component.literal(text);
    return self();
  }

  public T text(Component text) {
    this.text = text;
    return self();
  }

  @Override
  protected void renderContent(
      EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (this.text == null || this.text.getString().isBlank()) {
      return;
    }

    EverFont font = this.getFont();
    int fontHeight = font.fontHeight();

    int padTop = this.getContentPaddingTop();
    int padBottom = this.getContentPaddingBottom();
    int padLeft = this.getContentPaddingLeft();
    int padRight = this.getContentPaddingRight();

    int availableHeight = this.height - padTop - padBottom;
    int textY = padTop + (availableHeight - fontHeight) / 2;

    if (this.textAlign == TextAlignment.CENTER) {
      int centerX = this.width / 2;
      graphics.drawCenteredString(this.text, centerX, textY, this.getColor(), this.getTextShadow());
    } else if (this.textAlign == TextAlignment.RIGHT) {
      int textWidth = font.width(this.text);
      int rightX = this.width - textWidth - padRight;
      graphics.drawString(this.text, rightX, textY, this.getColor(), this.getTextShadow());
    } else {
      graphics.drawString(this.text, padLeft, textY, this.getColor(), this.getTextShadow());
    }
  }
}
