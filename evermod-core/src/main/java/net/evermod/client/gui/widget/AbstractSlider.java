package net.evermod.client.gui.widget;

import java.util.function.Consumer;
import net.evermod.client.gui.AbstractComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

/**
 * Abstract base class for slider components with explicit mouse drag state tracking
 * and step snapping mechanics compatible with Minecraft event dispatchers.
 *
 * @author Wipodev
 */
public abstract class AbstractSlider extends AbstractComponent {

  protected double value;
  protected double minValue = 0.0D;
  protected double maxValue = 1.0D;
  protected double step = 0.0D;

  protected int handleWidth = 8;
  protected int handleHeight = 14;

  protected boolean isDragging = false;

  protected Consumer<Double> onChangeHandler;

  /**
   * Constructs an AbstractSlider with explicit bounds and initial default value.
   *
   * @param x            Screen X position in pixels.
   * @param y            Screen Y position in pixels.
   * @param width        Slider width in pixels.
   * @param height       Slider height in pixels.
   * @param minValue     Minimum numeric bound.
   * @param maxValue     Maximum numeric bound.
   * @param defaultValue Initial starting value within bounds.
   */
  public AbstractSlider(int x, int y, int width, int height, double minValue, double maxValue,
      double defaultValue) {
    super(x, y, width, height);
    this.minValue = minValue;
    this.maxValue = maxValue;
    this.value = normalizeValue(defaultValue);
  }

  /**
   * Constructs an AbstractSlider at origin (0, 0) with default dimensions (120x20).
   */
  public AbstractSlider(double minValue, double maxValue, double defaultValue) {
    this(0, 0, 120, 20, minValue, maxValue, defaultValue);
  }

  // --- INTERNAL VALUE CONVERSION HELPERS ---

  /**
   * Converts a value within range [minValue, maxValue] into normalized range [0.0, 1.0].
   */
  protected double normalizeValue(double val) {
    if (this.maxValue <= this.minValue) {
      return 0.0D;
    }
    double clamped = Mth.clamp(val, this.minValue, this.maxValue);
    return (clamped - this.minValue) / (this.maxValue - this.minValue);
  }

  /**
   * Converts normalized internal value [0.0, 1.0] back to user domain value.
   */
  public double getDenormalizedValue() {
    double denormalized = this.minValue + (this.value * (this.maxValue - this.minValue));

    if (this.step > 0.0D) {
      denormalized =
          Math.round((denormalized - this.minValue) / this.step) * this.step + this.minValue;
      denormalized = Mth.clamp(denormalized, this.minValue, this.maxValue);
    }

    return denormalized;
  }

  /**
   * Calculates current value based on mouse X cursor position relative to slider width.
   */
  protected void setValueFromMouse(double mouseX) {
    double usableWidth = this.width - this.handleWidth;

    if (usableWidth <= 0) {
      return;
    }

    // Align cursor relative to center of handle
    double relativeX = mouseX - (this.x + (this.handleWidth / 2.0D));
    double normalized = relativeX / usableWidth;
    setValueNormalized(normalized);
  }

  /**
   * Updates normalized value, applies step constraints, and executes listeners.
   */
  public void setValueNormalized(double newValue) {
    double clamped = Mth.clamp(newValue, 0.0D, 1.0D);

    // Snap normalized value to step increment if configured
    if (this.step > 0.0D && this.maxValue > this.minValue) {
      double denormalized = this.minValue + (clamped * (this.maxValue - this.minValue));
      denormalized =
          Math.round((denormalized - this.minValue) / this.step) * this.step + this.minValue;
      denormalized = Mth.clamp(denormalized, this.minValue, this.maxValue);
      clamped = (denormalized - this.minValue) / (this.maxValue - this.minValue);
    }

    if (this.value != clamped) {
      this.value = clamped;
      applyValue();
      if (this.onChangeHandler != null) {
        this.onChangeHandler.accept(getDenormalizedValue());
      }
    }
  }

  /**
   * Internal hook called when the normalized value changes.
   */
  protected void applyValue() {}

  // --- MINECRAFT EVENT HOOKS ---

  public void onClick(double mouseX, double mouseY) {
    setValueFromMouse(mouseX);
  }

  public void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
    setValueFromMouse(mouseX);
  }

  public void onRelease(double mouseX, double mouseY) {
    Minecraft.getInstance().getSoundManager().play(
        SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.visible && this.enabled && button == 0 && isMouseOver((int) mouseX, (int) mouseY)) {
      this.isDragging = true;
      onClick(mouseX, mouseY);
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    if (this.visible && this.enabled && button == 0 && this.isDragging) {
      onDrag(mouseX, mouseY, dragX, dragY);
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (this.enabled && button == 0 && this.isDragging) {
      this.isDragging = false;
      onRelease(mouseX, mouseY);
      return true;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  // --- GETTERS & SETTERS (FLUENT API) ---

  public double getValue() {
    return getDenormalizedValue();
  }

  public double getNormalizedValue() {
    return this.value;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSlider> T setValue(double value) {
    setValueNormalized(normalizeValue(value));
    return (T) this;
  }

  public <T extends AbstractSlider> T value(double value) {
    return setValue(value);
  }

  public double getMinValue() {
    return this.minValue;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSlider> T setMinValue(double minValue) {
    this.minValue = minValue;
    setValueNormalized(this.value);
    return (T) this;
  }

  public <T extends AbstractSlider> T minValue(double minValue) {
    return setMinValue(minValue);
  }

  public double getMaxValue() {
    return this.maxValue;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSlider> T setMaxValue(double maxValue) {
    this.maxValue = maxValue;
    setValueNormalized(this.value);
    return (T) this;
  }

  public <T extends AbstractSlider> T maxValue(double maxValue) {
    return setMaxValue(maxValue);
  }

  public double getStep() {
    return this.step;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSlider> T setStep(double step) {
    this.step = Math.max(0.0D, step);
    setValueNormalized(this.value);
    return (T) this;
  }

  public <T extends AbstractSlider> T step(double step) {
    return setStep(step);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSlider> T setOnChange(Consumer<Double> handler) {
    this.onChangeHandler = handler;
    return (T) this;
  }

  public <T extends AbstractSlider> T onChange(Consumer<Double> handler) {
    return setOnChange(handler);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSlider> T setHandleSize(int width, int height) {
    this.handleWidth = width;
    this.handleHeight = height;
    return (T) this;
  }

  public boolean isDragging() {
    return this.isDragging;
  }

  protected int getHandleXOffset() {
    int usableWidth = this.width - this.handleWidth;
    return (int) (this.value * usableWidth);
  }
}
