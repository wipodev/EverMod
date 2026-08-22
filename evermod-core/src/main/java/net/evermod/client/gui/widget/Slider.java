package net.evermod.client.gui.widget;

import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.graphics.font.EverFont;
import net.evermod.client.gui.api.style.TextAlignment;
import net.evermod.client.gui.core.AbstractWidget;
import net.evermod.math.EverMath;
import net.minecraft.network.chat.Component;
import java.util.function.Consumer;

public class Slider extends AbstractWidget<Slider> {

  protected double minValue = 0.0D;
  protected double maxValue = 100.0D;
  protected double value = 0.0D;
  protected double step = 0.0D;
  protected int handleWidth = 8;
  protected boolean dragging = false;
  protected Consumer<Double> onChangeAction;
  protected final Button handle;
  protected final Label label;
  private Component text;
  private int labelOffsetX = 0;
  private int labelOffsetY = 0;

  public Slider(double minValue, double maxValue, double defaultValue) {
    super(0, 0, 0, 0);
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.value = EverMath.clamp(defaultValue, minValue, maxValue);
    this.handle = new Button() {
      @Override
      public boolean isHovered(double pointX, double pointY) {
        return Slider.this.isHovered(pointX, pointY);
      }
    };
    this.label = new Label();
    this.handle.setParent(this);
    this.label.setParent(this);
  }

  public Slider() {
    this(0.0D, 100.0D, 0.0D);
  }

  public Button getHandle() {
    return this.handle;
  }

  public Label getLabel() {
    return this.label;
  }

  public double getStep() {
    return this.step;
  }

  public Slider step(double step) {
    this.step = Math.max(0.0D, step);
    return self();
  }

  public int getHandleWidth() {
    return this.handleWidth;
  }

  public Slider handleWidth(int width) {
    this.handleWidth = width;
    return self();
  }

  public String getText() {
    return this.text != null ? this.text.getString() : "";
  }

  public Component getComponent() {
    return this.text;
  }

  public Slider text(String text) {
    this.text = Component.literal(text);
    return self();
  }

  public Slider text(Component text) {
    this.text = text;
    return self();
  }

  public int getLabelOffsetX() {
    return this.labelOffsetX;
  }

  public int getLabelOffsetY() {
    return this.labelOffsetY;
  }

  public Slider labelOffset(int x, int y) {
    this.labelOffsetX = x;
    this.labelOffsetY = y;
    return self();
  }

  public Slider onChange(Consumer<Double> action) {
    this.onChangeAction = action;
    return self();
  }

  public Slider onChange(Runnable action) {
    this.onChangeAction = slider -> action.run();
    return self();
  }

  public double getValue() {
    return this.value;
  }

  public Slider value(double value) {
    double newValue = EverMath.clamp(value, this.minValue, this.maxValue);
    if (this.step > 0.0D) {
      newValue = Math.round((newValue - this.minValue) / this.step) * this.step + this.minValue;
      newValue = EverMath.clamp(newValue, this.minValue, this.maxValue);
    }
    if (Double.compare(this.value, newValue) != 0) {
      this.value = newValue;
      if (this.onChangeAction != null) {
        this.onChangeAction.accept(this.value);
      }
    }
    return self();
  }

  protected void updateValueFromMouse(double mouseX) {
    int usableWidth = this.width - this.handleWidth;
    if (usableWidth <= 0) {
      return;
    }
    double relativeX =
        EverMath.clamp(mouseX - getGlobalX() - (this.handleWidth / 2.0), 0, usableWidth);
    double ratio = relativeX / usableWidth;
    value(this.minValue + ratio * (this.maxValue - this.minValue));
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      if (button == 0) {
        this.dragging = true;
        this.updateValueFromMouse(mouseX);
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (button == 0 && this.dragging) {
      this.dragging = false;
      return true;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    if (this.canInteract() && this.dragging) {
      this.updateValueFromMouse(mouseX);
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  @Override
  protected void renderContent(
      EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    double ratio = (this.maxValue > this.minValue)
        ? (this.value - this.minValue) / (this.maxValue - this.minValue)
        : 0;

    int handleX = (int) (ratio * (this.width - this.handleWidth));

    this.handle.enabled(this.enabled);
    this.handle.bounds(handleX, 0, this.handleWidth, this.height);

    this.handle.render(graphics, mouseX, mouseY, partialTicks);
    this.renderLabel(graphics, mouseX, mouseY, partialTicks);
  }

  protected void renderLabel(
      EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (this.text == null || this.text.getString().isBlank()) {
      return;
    }

    String formattedText = String.format("%s: %.0f%%", this.text.getString(), this.value);

    EverFont font = this.getFont();
    int textX = 0;
    int textY = 0;

    if (this.labelOffsetX > 0) {
      textX = this.labelOffsetX;
    } else {
      int textWidth = font.width(formattedText);
      int padLeft = this.getContentPaddingLeft();
      int padRight = this.getContentPaddingRight();

      if (this.textAlign == TextAlignment.CENTER) {
        textX = (this.width / 2) - (textWidth / 2);
      } else if (this.textAlign == TextAlignment.RIGHT) {
        textX = this.width - textWidth - padRight;
      } else {
        textX = padLeft;
      }
    }

    if (this.labelOffsetY > 0) {
      textY = this.labelOffsetY;
    } else {
      int fontHeight = font.fontHeight();
      int padTop = this.getContentPaddingTop();
      int padBottom = this.getContentPaddingBottom();

      int availableHeight = this.height - padTop - padBottom;
      textY = padTop + (availableHeight - fontHeight) / 2;
    }

    this.label.enabled(this.enabled);
    this.label.position(textX, textY);
    this.label.fontShadow(true);
    this.label.text(formattedText);

    this.label.render(graphics, mouseX, mouseY, partialTicks);
  }
}
