package net.evermod.client.gui;

/**
 * Abstract base implementation of {@link UIComponent}.
 * Manages spatial coordinates, dimensions, and state flags for all UI elements.
 *
 * @author Wipodev
 */
public abstract class AbstractComponent implements UIComponent {

  protected int x;
  protected int y;
  protected int width;
  protected int height;
  protected boolean visible = true;
  protected boolean enabled = true;

  /**
   * Constructs a UI component with explicit position and dimensions.
   *
   * @param x      Initial X position.
   * @param y      Initial Y position.
   * @param width  Component width.
   * @param height Component height.
   */
  public AbstractComponent(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  /**
   * Constructs a UI component at the origin (0, 0) with zero dimensions.
   * Useful for dynamically calculated layouts.
   */
  public AbstractComponent() {
    this(0, 0, 0, 0);
  }

  // --- POSITION AND DIMENSION GETTERS & SETTERS ---

  @Override
  public int getX() {
    return this.x;
  }

  @Override
  public void setX(int x) {
    this.x = x;
  }

  @Override
  public int getY() {
    return this.y;
  }

  @Override
  public void setY(int y) {
    this.y = y;
  }

  @Override
  public int getWidth() {
    return this.width;
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  @Override
  public int getHeight() {
    return this.height;
  }

  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  // --- VISIBILITY AND STATE GETTERS & SETTERS ---

  @Override
  public boolean isVisible() {
    return this.visible;
  }

  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  // --- ABSTRACT RENDERING METHOD ---

  @Override
  public abstract void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks);
}
