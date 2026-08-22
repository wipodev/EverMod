package net.evermod.client.gui.api;

import net.evermod.client.graphics.EverGraphics;

/**
 * Interface for UI elements that are drawn on screen during the main rendering pass.
 *
 * @author Wipodev
 */
public interface Renderable {

  /**
   * Renders the visual representation of this element.
   *
   * @param graphics     The current graphics context for drawing operations.
   * @param mouseX       Current cursor X position in screen coordinates.
   * @param mouseY       Current cursor Y position in screen coordinates.
   * @param partialTicks Fractional tick time for smooth animation rendering.
   */
  void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks);
}
