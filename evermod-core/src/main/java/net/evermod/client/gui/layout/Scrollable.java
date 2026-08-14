package net.evermod.client.gui.layout;

import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.ParentComponent;
import net.evermod.client.gui.UIComponent;

/**
 * A layout container that clips its content within specified bounds and enables
 * vertical or horizontal scrolling via the mouse wheel.
 *
 * @author Wipodev
 */
public class Scrollable extends ParentComponent {

  private UIComponent content;
  private int scrollX = 0;
  private int scrollY = 0;
  private int scrollSpeed = 12;
  private boolean allowVerticalScroll = true;
  private boolean allowHorizontalScroll = false;

  /**
   * Constructs a Scrollable container with explicit position and dimensions.
   *
   * @param x      Screen X position in pixels.
   * @param y      Screen Y position in pixels.
   * @param width  Viewport width in pixels.
   * @param height Viewport height in pixels.
   */
  public Scrollable(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  /**
   * Constructs a Scrollable container with dimensions at default origin.
   *
   * @param width  Viewport width in pixels.
   * @param height Viewport height in pixels.
   */
  public Scrollable(int width, int height) {
    this(0, 0, width, height);
  }

  /**
   * Constructs an empty Scrollable container at origin (0, 0).
   */
  public Scrollable() {
    this(0, 0, 0, 0);
  }

  // --- CONTENT MANAGEMENT ---

  /**
   * Sets the target content component or layout to be scrollable within this viewport.
   *
   * @param content Inner component or container.
   * @return This container instance for method chaining.
   */
  public Scrollable setContent(UIComponent content) {
    this.children.clear();
    this.content = content;
    if (content != null) {
      super.addChild(content);
      updateLayout();
    }
    return this;
  }

  /**
   * Fluent alias for {@link #setContent(UIComponent)}.
   *
   * @param content Inner component or container.
   * @return This container instance for method chaining.
   */
  public Scrollable content(UIComponent content) {
    return setContent(content);
  }

  /**
   * Gets the inner component managed by this scrollable viewport.
   *
   * @return The child {@link UIComponent} content.
   */
  public UIComponent getContent() {
    return this.content;
  }

  // --- MODIFIERS & CONFIGURATION ---

  /**
   * Gets the current horizontal scroll offset.
   *
   * @return Scroll X offset in pixels.
   */
  public int getScrollX() {
    return this.scrollX;
  }

  /**
   * Gets the current vertical scroll offset.
   *
   * @return Scroll Y offset in pixels.
   */
  public int getScrollY() {
    return this.scrollY;
  }

  /**
   * Gets the scroll speed factor.
   *
   * @return Scroll speed in pixels per scroll tick.
   */
  public int getScrollSpeed() {
    return this.scrollSpeed;
  }

  /**
   * Sets the scroll speed factor.
   *
   * @param scrollSpeed Desired speed in pixels (clamped to a minimum of 1).
   * @return This container instance for method chaining.
   */
  public Scrollable setScrollSpeed(int scrollSpeed) {
    this.scrollSpeed = Math.max(1, scrollSpeed);
    return this;
  }

  /**
   * Fluent alias for {@link #setScrollSpeed(int)}.
   *
   * @param scrollSpeed Desired speed in pixels.
   * @return This container instance for method chaining.
   */
  public Scrollable scrollSpeed(int scrollSpeed) {
    return setScrollSpeed(scrollSpeed);
  }

  /**
   * Checks whether vertical scrolling is enabled.
   *
   * @return {@code true} if vertical scrolling is allowed, {@code false} otherwise.
   */
  public boolean isVerticalScrollAllowed() {
    return this.allowVerticalScroll;
  }

  /**
   * Enables or disables vertical scrolling.
   *
   * @param enable {@code true} to allow vertical scrolling, {@code false} to disable.
   * @return This container instance for method chaining.
   */
  public Scrollable enableVerticalScroll(boolean enable) {
    this.allowVerticalScroll = enable;
    return this;
  }

  /**
   * Checks whether horizontal scrolling is enabled.
   *
   * @return {@code true} if horizontal scrolling is allowed, {@code false} otherwise.
   */
  public boolean isHorizontalScrollAllowed() {
    return this.allowHorizontalScroll;
  }

  /**
   * Enables or disables horizontal scrolling.
   *
   * @param enable {@code true} to allow horizontal scrolling, {@code false} to disable.
   * @return This container instance for method chaining.
   */
  public Scrollable enableHorizontalScroll(boolean enable) {
    this.allowHorizontalScroll = enable;
    return this;
  }

  // --- LAYOUT & SCROLL CONTROL ---

  /**
   * Adjusts relative inner coordinates and clamps scrolling within maximum bounds.
   */
  public void updateLayout() {
    if (this.content == null) {
      return;
    }

    // Clamp vertical scroll using viewport height vs content height
    int maxScrollY = Math.max(0, this.content.getHeight() - this.height);
    this.scrollY = Math.min(Math.max(this.scrollY, 0), maxScrollY);

    // Clamp horizontal scroll using viewport width vs content width
    int maxScrollX = Math.max(0, this.content.getWidth() - this.width);
    this.scrollX = Math.min(Math.max(this.scrollX, 0), maxScrollX);

    // Reposition child content based on scroll offset
    this.content.setX(this.x - this.scrollX);
    this.content.setY(this.y - this.scrollY);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    if (!isVisible() || !isEnabled() || !isMouseOver(mouseX, mouseY)) {
      return false;
    }

    if (this.allowVerticalScroll) {
      this.scrollY -= (int) (delta * this.scrollSpeed);
      updateLayout();
      return true;
    } else if (this.allowHorizontalScroll) {
      this.scrollX -= (int) (delta * this.scrollSpeed);
      updateLayout();
      return true;
    }

    return super.mouseScrolled(mouseX, mouseY, delta);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    ensureInitialized();
    updateLayout();

    renderBackground(graphics, mouseX, mouseY, partialTicks);

    graphics.enableScissor(this.x, this.y, this.width, this.height);

    for (UIComponent child : this.children) {
      if (child.isVisible()) {
        child.render(graphics, mouseX, mouseY, partialTicks);
      }
    }

    graphics.disableScissor();
  }
}
