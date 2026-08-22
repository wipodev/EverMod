package net.evermod.client.gui.api;

import net.evermod.client.graphics.EverGraphics;

/**
 * Interface for components capable of displaying popup information or hint text
 * when hovered by the mouse cursor.
 *
 * @author Wipodev
 */
public interface TooltipProvider {

  /**
   * Checks whether the tooltip associated with this element should be rendered
   * during the current frame.
   *
   * @param mouseX Current cursor X position.
   * @param mouseY Current cursor Y position.
   * @return {@code true} if the tooltip is active and ready to draw.
   */
  default boolean isTooltipActive(int mouseX, int mouseY) {
    return false;
  }

  /**
   * Renders the floating tooltip elements. Executed during the top-most layer pass.
   *
   * @param graphics Canvas graphics context.
   * @param mouseX   Current cursor X position.
   * @param mouseY   Current cursor Y position.
   */
  void renderTooltip(EverGraphics graphics, int mouseX, int mouseY);
}
