package net.evermod.client.gui.screens;

import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.gui.core.AbstractContainer;
import net.evermod.client.gui.core.UINode;
import net.minecraft.network.chat.Component;

/**
 * Interface representing a custom screen backed by a root container layout.
 */
public interface IEverScreen {

  /**
   * Gets the root container for this screen.
   *
   * @return The root container.
   */
  RootContainer getRootContainer();

  /**
   * Gets the title component of the screen.
   *
   * @return The title component.
   */
  Component getTitleComponent();

  /**
   * Sets up and builds the screen UI layout.
   */
  void setupUI();

  /**
   * Adds a child component to the root container.
   *
   * @param component The spatially bound component to add.
   * @param <C>       The type of the component.
   * @return The added component.
   */
  default <C extends UINode> C add(C component) {
    return getRootContainer().addChild(component);
  }

  /**
   * Initializes the screen dimensions and rebuilds the UI.
   *
   * @param width  The new width of the screen.
   * @param height The new height of the screen.
   */
  default void initEverScreen(int width, int height) {
    RootContainer root = getRootContainer();
    root.setX(0);
    root.setY(0);
    root.setWidth(width);
    root.setHeight(height);
    root.clearChildren();
    setupUI();
  }

  /**
   * Renders the screen and its root container.
   *
   * @param graphics    The graphics context.
   * @param mouseX      The X coordinate of the mouse.
   * @param mouseY      The Y coordinate of the mouse.
   * @param partialTick The partial tick time.
   */
  default void renderEverScreen(EverGraphics graphics, int mouseX, int mouseY, float partialTick) {
    getRootContainer().render(graphics, mouseX, mouseY, partialTick);
  }

  /**
   * Handles mouse movement events.
   *
   * @param mouseX The X coordinate of the mouse.
   * @param mouseY The Y coordinate of the mouse.
   */
  default void handleMouseMoved(double mouseX, double mouseY) {
    getRootContainer().mouseMoved(mouseX, mouseY);
  }

  /**
   * Handles mouse click events.
   *
   * @param mouseX The X coordinate of the mouse.
   * @param mouseY The Y coordinate of the mouse.
   * @param button The button pressed.
   * @return True if handled, false otherwise.
   */
  default boolean handleMouseClicked(double mouseX, double mouseY, int button) {
    return getRootContainer().mouseClicked(mouseX, mouseY, button);
  }

  /**
   * Handles mouse release events.
   *
   * @param mouseX The X coordinate of the mouse.
   * @param mouseY The Y coordinate of the mouse.
   * @param button The button released.
   * @return True if handled, false otherwise.
   */
  default boolean handleMouseReleased(double mouseX, double mouseY, int button) {
    return getRootContainer().mouseReleased(mouseX, mouseY, button);
  }

  /**
   * Handles mouse drag events.
   *
   * @param mouseX The X coordinate of the mouse.
   * @param mouseY The Y coordinate of the mouse.
   * @param button The button being dragged with.
   * @param dragX  The delta X movement.
   * @param dragY  The delta Y movement.
   * @return True if handled, false otherwise.
   */
  default boolean handleMouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    return getRootContainer().mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  /**
   * Handles mouse scroll events.
   *
   * @param mouseX The X coordinate of the mouse.
   * @param mouseY The Y coordinate of the mouse.
   * @param deltaX The horizontal scroll delta.
   * @param deltaY The vertical scroll delta.
   * @return True if handled, false otherwise.
   */
  default boolean handleMouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
    return getRootContainer().mouseScrolled(mouseX, mouseY, deltaX, deltaY);
  }

  /**
   * Handles mouse scroll events (vertical only).
   *
   * @param mouseX The X coordinate of the mouse.
   * @param mouseY The Y coordinate of the mouse.
   * @param delta  The vertical scroll delta.
   * @return True if handled, false otherwise.
   */
  default boolean handleMouseScrolled(double mouseX, double mouseY, double delta) {
    return handleMouseScrolled(mouseX, mouseY, 0.0D, delta);
  }

  /**
   * Handles key press events.
   *
   * @param keyCode   The key code pressed.
   * @param scanCode  The key scan code.
   * @param modifiers The active modifiers.
   * @return True if handled, false otherwise.
   */
  default boolean handleKeyPressed(int keyCode, int scanCode, int modifiers) {
    return getRootContainer().keyPressed(keyCode, scanCode, modifiers);
  }

  /**
   * Handles key release events.
   *
   * @param keyCode   The key code released.
   * @param scanCode  The key scan code.
   * @param modifiers The active modifiers.
   * @return True if handled, false otherwise.
   */
  default boolean handleKeyReleased(int keyCode, int scanCode, int modifiers) {
    return getRootContainer().keyReleased(keyCode, scanCode, modifiers);
  }

  /**
   * Handles typed characters.
   *
   * @param codePoint The typed character code point.
   * @param modifiers The active modifiers.
   * @return True if handled, false otherwise.
   */
  default boolean handleCharTyped(char codePoint, int modifiers) {
    return getRootContainer().charTyped(codePoint, modifiers);
  }

  /**
   * Root container implementation used as the top-level container for an {@link IEverScreen}.
   */
  class RootContainer extends AbstractContainer<RootContainer> {

    public RootContainer() {
      super();
    }

    public RootContainer(int x, int y, int width, int height) {
      super(x, y, width, height);
    }

    @Override
    public boolean isHovered(double pointX, double pointY) {
      return this.canInteract() && this.containsPoint(pointX, pointY);
    }
  }
}
