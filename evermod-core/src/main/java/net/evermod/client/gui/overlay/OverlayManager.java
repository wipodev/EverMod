package net.evermod.client.gui.overlay;

import java.util.ArrayList;
import java.util.List;
import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.gui.api.OverlayProvider;

/**
 * Manages the deferred rendering queue for active overlay elements.
 *
 * @author Wipodev
 */
public class OverlayManager {

  private final List<OverlayProvider> overlays = new ArrayList<>();

  /**
   * Enqueues an overlay provider to be drawn on top of the base UI.
   */
  public void enqueue(OverlayProvider provider) {
    if (provider != null && provider.isOverlayActive()) {
      this.overlays.add(provider);
    }
  }

  /**
   * Executes renderOverlay for all queued elements and clears the queue.
   */
  public void flush(EverGraphics graphics, int mouseX, int mouseY) {
    if (this.overlays.isEmpty()) {
      return;
    }

    for (int i = 0; i < this.overlays.size(); i++) {
      this.overlays.get(i).renderOverlay(graphics, mouseX, mouseY);
    }

    this.overlays.clear();
  }

  public boolean hasPending() {
    return !this.overlays.isEmpty();
  }
}
