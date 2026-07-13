package net.evermod.config;

public class EverProperty<T> {
  private final T defaultValue;
  private T value;
  private String id = "";
  private String name = "";
  private String comment = "";
  private Object min = null;
  private Object max = null;

  public enum DisplayType {
    DEFAULT, PERCENTAGE, SECONDS_FROM_TICKS
  }

  private EverProperty(T defaultValue) {
    this.defaultValue = defaultValue;
    this.value = defaultValue;
  }

  private DisplayType displayType = DisplayType.DEFAULT;

  public static <V> EverProperty<V> of(V defaultValue) {
    return new EverProperty<>(defaultValue);
  }

  public EverProperty<T> name(String name) {
    this.name = name;
    return this;
  }

  public EverProperty<T> comment(String comment) {
    this.comment = comment;
    return this;
  }

  public EverProperty<T> range(T min, T max) {
    this.min = min;
    this.max = max;
    return this;
  }

  public EverProperty<T> displayAs(DisplayType type) {
    this.displayType = type;
    return this;
  }

  public DisplayType getDisplayType() {
    return this.displayType;
  }

  public T get() {
    return value;
  }

  public void set(T newValue) {
    this.value = newValue;
  }

  // Getters internos para el framework
  protected T getDefaultValue() {
    return defaultValue;
  }

  protected String getComment() {
    return comment;
  }

  protected Object getMin() {
    return min;
  }

  protected Object getMax() {
    return max;
  }

  protected void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return this.id;
  }

  protected void setName(String name) {
    this.name = name;
  }

  public String getName() {
    if (this.name != null && !this.name.isEmpty()) {
      return this.name;
    }
    return (this.id == null || this.id.isEmpty()) ? "Unnamed Property" : this.id;
  }

  public boolean hasCustomName() {
    return this.name != null && !this.name.isEmpty();
  }
}
