package net.evermod.client.gui.api;

/**
 * Interface for components that handle user input events such as mouse actions,
 * keyboard presses, and focus state management.
 *
 * @author Wipodev
 */
public interface Interactive {

  /**
     * Convenience check when the interactive target is this current element.
     * 
     * @param pointX Screen X coordinate
     * @param pointY Screen Y coordinate
     * @return True if point lies inside this element's global bounds
     */
  boolean isHovered(double pointX, double pointY);

  /**
   * Called when the mouse cursor is moved within or across the component boundaries.
   *
   * @param mouseX Current cursor X position.
   * @param mouseY Current cursor Y position.
   */
  default void mouseMoved(double mouseX, double mouseY) {}

  /**
   * Called when a mouse button is pressed over or targeted at this component.
   *
   * @param mouseX Current cursor X position.
   * @param mouseY Current cursor Y position.
   * @param button The mouse button pressed (0 for Left, 1 for Right, 2 for Middle).
   * @return {@code true} if the event was consumed and should not propagate further.
   */
  default boolean mouseClicked(double mouseX, double mouseY, int button) {
    return false;
  }

  /**
   * Called when a mouse button is released.
   *
   * @param mouseX Current cursor X position.
   * @param mouseY Current cursor Y position.
   * @param button The mouse button released.
   * @return {@code true} if the event was consumed.
   */
  default boolean mouseReleased(double mouseX, double mouseY, int button) {
    return false;
  }

  /**
   * Called when the mouse cursor is dragged across the component.
   *
   * @param mouseX Current cursor X position.
   * @param mouseY Current cursor Y position.
   * @param button The mouse button being held down.
   * @param dragX  Horizontal drag delta.
   * @param dragY  Vertical drag delta.
   * @return {@code true} if the event was consumed.
   */
  default boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    return false;
  }

  /**
   * Called when the mouse scroll wheel is moved vertically.
   *
   * @param mouseX Current cursor X position.
   * @param mouseY Current cursor Y position.
   * @param delta  Vertical scroll amount.
   * @return {@code true} if the event was consumed.
   */
  default boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    return false;
  }

  /**
   * Handles dual-axis scroll wheel input.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @param deltaX Horizontal scroll delta.
   * @param deltaY Vertical scroll delta.
   * @return {@code true} if consumed by this component.
   */
  default boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
    return this.mouseScrolled(mouseX, mouseY, deltaY);
  }

  /**
   * Called when a key is pressed while this component has input focus.
   *
   * @param keyCode   The key code pressed.
   * @param scanCode  The platform-dependent scan code.
   * @param modifiers Bitfield for modifier keys (Shift, Ctrl, Alt).
   * @return {@code true} if the event was consumed.
   */
  default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    return false;
  }

  /**
   * Called when a key is released while this component has input focus.
   *
   * @param keyCode   The key code released.
   * @param scanCode  The platform-dependent scan code.
   * @param modifiers Bitfield for modifier keys.
   * @return {@code true} if the event was consumed.
   */
  default boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    return false;
  }

  /**
   * Called when a character is typed while this component has input focus.
   *
   * @param codePoint The Unicode code point typed.
   * @param modifiers Bitfield for modifier keys.
   * @return {@code true} if the event was consumed.
   */
  default boolean charTyped(char codePoint, int modifiers) {
    return false;
  }

  /**
   * Checks if this component is currently focused for keyboard input.
   *
   * @return {@code true} if focused.
   */
  default boolean isFocused() {
    return false;
  }

  /**
   * Sets the input focus state of this component.
   *
   * @param focused {@code true} to focus, {@code false} to unfocus.
   */
  default void setFocused(boolean focused) {}
}
