package net.evermod.client.gui.widget;

import java.util.function.Consumer;
import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.graphics.font.EverFont;
import net.evermod.client.graphics.style.Border;
import net.evermod.client.graphics.style.BorderColor;
import net.evermod.client.gui.core.AbstractWidget;
import net.minecraft.network.chat.Component;

public class Checkbox extends AbstractWidget<Checkbox> {

  public enum TextPosition {
    LEFT, RIGHT
  }

  private int boxSize = 14;
  private int boxColor = 0xFF222222;
  private int boxBorderColor = 0xFF000000;
  private int boxHoverBorderColor = 0xFFFFFFFF;
  private int boxDisabledBorderColor = 0xFF888888;
  private int checkColor = 0xFF55FF55;
  private int gap = 5;
  protected boolean checked;
  protected Consumer<Boolean> onChangeAction;
  protected final Label label;
  private TextPosition textCheckPosition = TextPosition.RIGHT;

  public Checkbox(boolean initialValue) {
    super(0, 0, 0, 0);
    this.checked = initialValue;
    this.label = new Label();
    this.label.setParent(this);
  }

  public Checkbox() {
    this(false);
  }

  @Override
  protected int calculateContentWidth() {
    if (this.label.getText() == null || this.label.getText().isBlank()) {
      return this.boxSize;
    }
    return this.boxSize + this.gap + getFont().width(this.label.getText());
  }

  @Override
  protected int calculateContentHeight() {
    return Math.max(this.boxSize, getFont().fontHeight());
  }

  public int getBoxSize() {
    return this.boxSize;
  }

  public Checkbox boxSize(int size) {
    this.boxSize = size;
    return self();
  }

  private int getCurrentBoxBorderColor() {
    if (!this.enabled) {
      return this.boxDisabledBorderColor;
    }
    if (this.hovered) {
      return this.boxHoverBorderColor;
    }
    return this.boxBorderColor;
  }

  public Checkbox colorBox(int checkColor, int boxColor, int borderBox) {
    this.boxColor = boxColor;
    this.checkColor = checkColor;
    this.boxBorderColor = borderBox;
    return self();
  }

  public Checkbox colorBox(int checkColor, int borderBox) {
    this.checkColor = checkColor;
    this.boxBorderColor = borderBox;
    return self();
  }

  public Checkbox colorBox(int checkColor) {
    this.checkColor = checkColor;
    return self();
  }

  public int getGap() {
    return this.gap;
  }

  public Checkbox gap(int size) {
    this.gap = size;
    return self();
  }

  public Label getLabel() {
    return this.label;
  }

  public Checkbox text(String text) {
    this.label.text(text);
    return self();
  }

  public Checkbox text(Component text) {
    this.label.text(text);
    return self();
  }

  public Checkbox textPosition(TextPosition position) {
    this.textCheckPosition = position;
    return self();
  }

  public void toggle() {
    checked(!this.checked);
  }

  public Checkbox onChange(Consumer<Boolean> action) {
    this.onChangeAction = action;
    return self();
  }

  public Checkbox onChange(Runnable action) {
    this.onChangeAction = check -> action.run();
    return self();
  }

  public boolean isChecked() {
    return this.checked;
  }

  public Checkbox checked(boolean checked) {
    if (this.checked != checked) {
      this.checked = checked;
      if (this.onChangeAction != null) {
        this.onChangeAction.accept(this.checked);
      }
    }
    return self();
  }


  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      if (button == 0) {
        toggle();
        return true;
      }
    }
    return false;
  }

  @Override
  protected void renderContent(
      EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    int padTop = getContentPaddingTop();
    int padBottom = getContentPaddingBottom();
    int padLeft = getContentPaddingLeft();

    String text = this.label.getText();
    boolean hasText = text != null && !text.isBlank();
    int textWidth = 0;
    int fontHeight = 0;
    EverFont font = null;

    if (hasText) {
      font = getFont();
      textWidth = font.width(text);
      fontHeight = font.fontHeight();
    }

    int boxX;
    int textX = 0;

    if (hasText && this.textCheckPosition == TextPosition.LEFT) {
      textX = padLeft;
      boxX = padLeft + textWidth + this.gap;
    } else {
      boxX = padLeft;
      if (hasText) {
        textX = padLeft + this.boxSize + this.gap;
      }
    }

    int availableHeight = this.height - padTop - padBottom;
    int boxY = padTop + (availableHeight - this.boxSize) / 2;

    graphics.drawRect(boxX, boxY, this.boxSize, this.boxSize, this.boxColor,
        getCurrentBoxBorderColor());

    if (this.checked) {
      int pad = 3;
      graphics.drawRect(boxX + pad, boxY + pad, this.boxSize - (pad * 2), this.boxSize - (pad * 2),
          this.checkColor);
    }

    if (hasText) {
      int textY = padTop + (availableHeight - fontHeight) / 2;
      this.label.enabled(this.enabled);
      this.label.position(textX, textY);
      this.label.fontShadow(true);
      this.label.render(graphics, mouseX, mouseY, partialTicks);
    }
  }
}
