package net.evermod.client.gui.overlay;

import java.util.ArrayList;
import java.util.List;
import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.gui.api.TooltipProvider;

/**
 * Manages the deferred rendering queue for active tooltip elements.
 *
 * @author Wipodev
 */
public class ToolTipManager {

  private final List<TooltipProvider> toolTips = new ArrayList<>();

  /**
   * Enqueues an toolTip provider to be drawn on top of the base UI.
   */
  public void enqueue(TooltipProvider provider, int mouseX, int mouseY) {
    if (provider != null && provider.isTooltipActive(mouseX, mouseY)) {
      this.toolTips.add(provider);
    }
  }

  /**
   * Executes renderTooltip for all queued elements and clears the queue.
   */
  public void flush(EverGraphics graphics, int mouseX, int mouseY) {
    if (this.toolTips.isEmpty()) {
      return;
    }

    for (int i = 0; i < this.toolTips.size(); i++) {
      this.toolTips.get(i).renderTooltip(graphics, mouseX, mouseY);
    }

    this.toolTips.clear();
  }

  public boolean hasPending() {
    return !this.toolTips.isEmpty();
  }
}
