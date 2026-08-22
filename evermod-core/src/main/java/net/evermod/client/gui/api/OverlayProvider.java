package net.evermod.client.gui.api;

import net.evermod.client.graphics.EverGraphics;

/**
 * Interface for UI components that need to render content in an overlay pass
 * on top of other screen elements (e.g., dropdown lists, popups, floating menus).
 *
 * @author Wipodev
 */
public interface OverlayProvider {

  /**
   * Determines if the overlay layer should be rendered in the current frame.
   *
   * @return {@code true} if the overlay is active and needs rendering.
   */
  boolean isOverlayActive();

  /**
   * Renders the floating overlay elements. Executed during the top-layer pass.
   *
   * @param graphics Canvas graphics context.
   * @param mouseX   Current cursor X position.
   * @param mouseY   Current cursor Y position.
   */
  void renderOverlay(EverGraphics graphics, int mouseX, int mouseY);
}
