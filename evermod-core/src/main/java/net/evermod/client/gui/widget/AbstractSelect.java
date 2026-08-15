package net.evermod.client.gui.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import net.evermod.client.gui.AbstractComponent;

/**
 * Abstract base class for selectable dropdown components.
 * Manages options list, selection state, and popup expansion behavior.
 *
 * @param <V> The type of the value held by each selectable option.
 * @author Wipodev
 */
public abstract class AbstractSelect<V> extends AbstractComponent {

  protected final List<Option<V>> options = new ArrayList<>();
  protected Option<V> selectedOption = null;
  protected boolean expanded = false;

  protected Consumer<V> onChangeHandler;
  protected Function<V, String> labelProvider = Object::toString;

  /**
   * Represents an individual selectable option within the dropdown.
   *
   * @param <V> The value type.
   */
  public static class Option<V> {
    private final V value;
    private final String label;

    public Option(V value, String label) {
      this.value = value;
      this.label = label;
    }

    public V getValue() {
      return this.value;
    }

    public String getLabel() {
      return this.label;
    }
  }

  /**
   * Constructs an AbstractSelect component with position and dimensions.
   *
   * @param x      Screen X position in pixels.
   * @param y      Screen Y position in pixels.
   * @param width  Select component width in pixels.
   * @param height Select component height in pixels.
   */
  public AbstractSelect(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  /**
   * Constructs an AbstractSelect at origin (0, 0) with default dimensions (120x20).
   */
  public AbstractSelect() {
    this(0, 0, 120, 20);
  }

  // --- OPTION MANAGEMENT ---

  @SuppressWarnings("unchecked")
  public <T extends AbstractSelect<V>> T addOption(V value, String label) {
    Option<V> opt = new Option<>(value, label);
    this.options.add(opt);
    if (this.selectedOption == null) {
      this.selectedOption = opt;
    }
    return (T) this;
  }

  public <T extends AbstractSelect<V>> T addOption(V value) {
    return addOption(value,
        this.labelProvider != null ? this.labelProvider.apply(value) : String.valueOf(value));
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSelect<V>> T clearOptions() {
    this.options.clear();
    this.selectedOption = null;
    this.expanded = false;
    return (T) this;
  }

  public List<Option<V>> getOptions() {
    return Collections.unmodifiableList(this.options);
  }

  // --- SELECTION STATE ---

  public V getSelectedValue() {
    return this.selectedOption != null ? this.selectedOption.getValue() : null;
  }

  public Option<V> getSelectedOption() {
    return this.selectedOption;
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSelect<V>> T setSelectedValue(V value) {
    for (Option<V> opt : this.options) {
      if ((opt.getValue() == null && value == null)
          || (opt.getValue() != null && opt.getValue().equals(value))) {
        selectOption(opt);
        break;
      }
    }
    return (T) this;
  }

  public <T extends AbstractSelect<V>> T selectedValue(V value) {
    return setSelectedValue(value);
  }

  protected void selectOption(Option<V> option) {
    if (this.selectedOption != option) {
      this.selectedOption = option;
      if (this.onChangeHandler != null && option != null) {
        this.onChangeHandler.accept(option.getValue());
      }
    }
    this.expanded = false;
  }

  // --- EXPANSION STATE ---

  public boolean isExpanded() {
    return this.expanded;
  }

  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  public void toggleExpand() {
    this.expanded = !this.expanded;
  }

  // --- FLUENT SETTERS ---

  @SuppressWarnings("unchecked")
  public <T extends AbstractSelect<V>> T setLabelProvider(Function<V, String> provider) {
    this.labelProvider = provider;
    return (T) this;
  }

  public <T extends AbstractSelect<V>> T labelProvider(Function<V, String> provider) {
    return setLabelProvider(provider);
  }

  @SuppressWarnings("unchecked")
  public <T extends AbstractSelect<V>> T setOnChange(Consumer<V> handler) {
    this.onChangeHandler = handler;
    return (T) this;
  }

  public <T extends AbstractSelect<V>> T onChange(Consumer<V> handler) {
    return setOnChange(handler);
  }

  // --- EVENT HANDLING ---

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!this.visible || !this.enabled || button != 0) {
      return false;
    }

    int mx = (int) mouseX;
    int my = (int) mouseY;

    // Header click toggles menu
    if (isMouseOverHeader(mx, my)) {
      toggleExpand();
      return true;
    }

    // Check click on dropdown items if expanded
    if (this.expanded) {
      int clickedIndex = getOptionIndexAt(mx, my);
      if (clickedIndex >= 0 && clickedIndex < this.options.size()) {
        selectOption(this.options.get(clickedIndex));
        return true;
      } else {
        // Clicked outside list, close dropdown
        this.expanded = false;
      }
    }

    return super.mouseClicked(mouseX, mouseY, button);
  }

  protected boolean isMouseOverHeader(int mouseX, int mouseY) {
    return mouseX >= this.x && mouseX < this.x + this.width &&
        mouseY >= this.y && mouseY < this.y + this.height;
  }

  protected abstract int getOptionIndexAt(int mouseX, int mouseY);
}
