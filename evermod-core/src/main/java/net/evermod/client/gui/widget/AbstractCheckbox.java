package net.evermod.client.gui.widget;

import java.util.function.Consumer;
import net.evermod.client.gui.AbstractComponent;

/**
 * Abstract base class for checkbox UI components with click toggle state management.
 *
 * @author Wipodev
 */
public abstract class AbstractCheckbox extends AbstractComponent {

  protected boolean checked;
  protected Consumer<Boolean> onChangeHandler;

  /**
   * Constructs an AbstractCheckbox with explicit position, bounds, and initial state.
   *
   * @param x Initial X position in pixels.
   * @param y Initial Y position in pixels.
   * @param width Component width in pixels.
   * @param height Component height in pixels.
   * @param initialValue Initial checked state.
   */
  public AbstractCheckbox(int x, int y, int width, int height, boolean initialValue) {
    super(x, y, width, height);
    this.checked = initialValue;
  }

  /**
   * Constructs an AbstractCheckbox at origin (0, 0) with default dimensions (14x14).
   *
   * @param initialValue Initial checked state.
   */
  public AbstractCheckbox(boolean initialValue) {
    this(0, 0, 14, 14, initialValue);
  }

  /**
   * Constructs an AbstractCheckbox at origin (0, 0) with unchecked state.
   */
  public AbstractCheckbox() {
    this(0, 0, 14, 14, false);
  }

  /**
   * Toggles the current checked state and invokes listeners.
   */
  public void toggle() {
    setChecked(!this.checked);
  }

  public boolean isChecked() {
    return this.checked;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractCheckbox> T setChecked(boolean checked) {
    if (this.checked != checked) {
      this.checked = checked;
      if (this.onChangeHandler != null) {
        this.onChangeHandler.accept(this.checked);
      }
    }
    return (T) this;
  }

  public <T extends AbstractCheckbox> T checked(boolean checked) {
    return setChecked(checked);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractCheckbox> T setOnChange(Consumer<Boolean> handler) {
    this.onChangeHandler = handler;
    return (T) this;
  }

  public <T extends AbstractCheckbox> T onChange(Consumer<Boolean> handler) {
    return setOnChange(handler);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.visible && this.enabled && button == 0 && isMouseOver((int) mouseX, (int) mouseY)) {
      toggle();
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }
}
