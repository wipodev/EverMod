package net.evermod.client.gui.screens;

import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.ParentComponent;
import net.evermod.client.gui.UIComponent;
import net.minecraft.network.chat.Component;

/**
 * Pure version-agnostic interface for EverMod screen implementations.
 * Defines contract for UI lifecycle, component mounting, and input processing.
 *
 * @author Wipodev
 */
public interface IEverScreen {

  /**
   * Returns the root container hosting all mounted components for this screen.
   *
   * @return Root container instance.
   */
  RootContainer getRootContainer();

  /**
   * Gets the screen title component.
   *
   * @return Screen title as a Component.
   */
  Component getTitleComponent();

  /**
   * Lifecycle method to declare and mount autonomous components into the screen.
   */
  void setupUI();

  /**
   * Mounts a UI component directly into the root container.
   *
   * @param component Autonomous UI component or layout to mount.
   */
  default void add(UIComponent component) {
    getRootContainer().addChild(component);
  }

  /**
   * Resets and recalculates root container dimensions matching viewport bounds.
   *
   * @param width Current viewport width.
   * @param height Current viewport height.
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
   * Renders the root container hierarchy via EverGraphics wrapper.
   *
   * @param graphics EverGraphics rendering context.
   * @param mouseX Current mouse cursor X position.
   * @param mouseY Current mouse cursor Y position.
   * @param partialTick Render tick delta time.
   */
  default void renderEverScreen(EverGraphics graphics, int mouseX, int mouseY, float partialTick) {
    getRootContainer().render(graphics, mouseX, mouseY, partialTick);
  }

  // --- INPUT PROPAGATION DELEMITERS ---

  default boolean handleMouseClicked(double mouseX, double mouseY, int button) {
    return getRootContainer().mouseClicked(mouseX, mouseY, button);
  }

  default boolean handleMouseReleased(double mouseX, double mouseY, int button) {
    return getRootContainer().mouseReleased(mouseX, mouseY, button);
  }

  default boolean handleMouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    return getRootContainer().mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  default boolean handleMouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
    return getRootContainer().mouseScrolled(mouseX, mouseY, deltaX, deltaY);
  }

  default boolean handleMouseScrolled(double mouseX, double mouseY, double delta) {
    return handleMouseScrolled(mouseX, mouseY, 0.0D, delta);
  }

  default boolean handleKeyPressed(int keyCode, int scanCode, int modifiers) {
    return getRootContainer().keyPressed(keyCode, scanCode, modifiers);
  }

  default boolean handleCharTyped(char codePoint, int modifiers) {
    return getRootContainer().charTyped(codePoint, modifiers);
  }

  /**
   * Root component container instance delegate for screen instances.
   */
  class RootContainer extends ParentComponent {
    public RootContainer() {
      super();
    }
  }
}
