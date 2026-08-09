package net.evermod.client.gui;

/**
 * Base contract for all user interface elements in EverUI.
 * Defines dimensions, state flags, rendering methods, and input event handlers.
 *
 * @author Wipodev
 */
public interface UIComponent {

  // --- POSITION AND DIMENSIONS ---

  /**
   * Gets the relative or absolute X position.
   *
   * @return X coordinate in pixels.
   */
  int getX();

  /**
   * Sets the X position of the component.
   *
   * @param x Target X coordinate.
   */
  void setX(int x);

  /**
   * Gets the relative or absolute Y position.
   *
   * @return Y coordinate in pixels.
   */
  int getY();

  /**
   * Sets the Y position of the component.
   *
   * @param y Target Y coordinate.
   */
  void setY(int y);

  /**
   * Gets the current width of the component.
   *
   * @return Component width in pixels.
   */
  int getWidth();

  /**
   * Sets the width of the component.
   *
   * @param width Target width in pixels.
   */
  void setWidth(int width);

  /**
   * Gets the current height of the component.
   *
   * @return Component height in pixels.
   */
  int getHeight();

  /**
   * Sets the height of the component.
   *
   * @param height Target height in pixels.
   */
  void setHeight(int height);

  // --- VISIBILITY AND STATE ---

  /**
   * Checks if the component is visible and should be rendered.
   *
   * @return True if visible.
   */
  boolean isVisible();

  /**
   * Sets the visibility state of the component.
   *
   * @param visible True to show, false to hide.
   */
  void setVisible(boolean visible);

  /**
   * Checks if the component is enabled and can process user interactions.
   *
   * @return True if enabled.
   */
  boolean isEnabled();

  /**
   * Sets the enabled state of the component.
   *
   * @param enabled True to enable, false to disable.
   */
  void setEnabled(boolean enabled);

  /**
   * Checks if the mouse coordinates hover inside the bounds of this component.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @return True if mouse is hovering over the component bounds.
   */
  default boolean isMouseOver(double mouseX, double mouseY) {
    return isVisible() &&
        mouseX >= getX() && mouseX < getX() + getWidth() &&
        mouseY >= getY() && mouseY < getY() + getHeight();
  }

  // --- RENDERING CYCLE ---

  /**
   * Renders the component on screen using EverGraphics.
   *
   * @param graphics The active EverGraphics context.
   * @param mouseX   Current mouse X position.
   * @param mouseY   Current mouse Y position.
   * @param partialTicks Frame rendering delta time.
   */
  void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks);

  // --- MOUSE INPUT EVENTS ---

  /**
   * Handles mouse button press events.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @param button Pressed mouse button ID (0: Left, 1: Right, 2: Middle).
   * @return True if the event was consumed by this component.
   */
  default boolean mouseClicked(double mouseX, double mouseY, int button) {
    return false;
  }

  /**
   * Handles mouse button release events.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @param button Released mouse button ID.
   * @return True if the event was consumed by this component.
   */
  default boolean mouseReleased(double mouseX, double mouseY, int button) {
    return false;
  }

  /**
   * Handles mouse drag events when a button is held down.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @param button Active mouse button ID.
   * @param dragX  Horizontal displacement delta.
   * @param dragY  Vertical displacement delta.
   * @return True if the event was consumed.
   */
  default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    return false;
  }

  /**
   * Handles mouse scroll wheel movements.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @param delta  Scroll wheel movement magnitude and direction.
   * @return True if the event was consumed.
   */
  default boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    return false;
  }

  // --- KEYBOARD INPUT EVENTS ---

  /**
   * Handles keyboard key press events.
   *
   * @param keyCode   NATIVE GLFW key code.
   * @param scanCode  Hardware scan code.
   * @param modifiers Key modifier flags (e.g., Shift, Ctrl, Alt).
   * @return True if the event was consumed.
   */
  default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return false;
  }

  /**
   * Handles typed character input (useful for text fields).
   *
   * @param codePoint Unicode character code.
   * @param modifiers Key modifier flags.
   * @return True if the event was consumed.
   */
  default boolean charTyped(char codePoint, int modifiers) {
    return false;
  }
}
