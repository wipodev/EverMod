package net.evermod.client.gui;

/**
 * Abstract base implementation of {@link UIComponent}.
 * Manages spatial coordinates, dimensions, and state flags for all UI elements.
 *
 * @author Wipodev
 */
public abstract class AbstractComponent implements UIComponent {

  /** The X-coordinate position of the component on screen. */
  protected int x;

  /** The Y-coordinate position of the component on screen. */
  protected int y;

  /** The width of the component in pixels. */
  protected int width;

  /** The height of the component in pixels. */
  protected int height;

  /** Controls whether the component should be rendered and process inputs. */
  protected boolean visible = true;

  /** Controls whether the component is interactable by user input. */
  protected boolean enabled = true;

  /**
   * Constructs a UI component with explicit position and dimensions.
   *
   * @param x      Initial X position in pixels.
   * @param y      Initial Y position in pixels.
   * @param width  Component width in pixels.
   * @param height Component height in pixels.
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

  /**
   * {@inheritDoc}
   */
  @Override
  public int getX() {
    return this.x;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setX(int x) {
    this.x = x;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getY() {
    return this.y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setY(int y) {
    this.y = y;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getWidth() {
    return this.width;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidth(int width) {
    this.width = width;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getHeight() {
    return this.height;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setHeight(int height) {
    this.height = height;
  }

  // --- VISIBILITY AND STATE GETTERS & SETTERS ---

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isVisible() {
    return this.visible;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isEnabled() {
    return this.enabled;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  // --- ABSTRACT RENDERING METHOD ---

  /**
   * {@inheritDoc}
   */
  @Override
  public abstract void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks);
}
