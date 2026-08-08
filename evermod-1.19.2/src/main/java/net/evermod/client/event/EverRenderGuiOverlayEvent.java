package net.evermod.client.event;

import net.evermod.client.gui.EverGraphics;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Event fired during GUI overlay rendering in EverMod.
 * Wraps the native render context into an EverGraphics wrapper.
 *
 * @author Wipodev
 */
public abstract class EverRenderGuiOverlayEvent extends Event {
  private final EverGraphics graphics;
  private final float partialTick;
  private final String overlayId;
  private final int width;
  private final int height;

  protected EverRenderGuiOverlayEvent(Object nativeGraphics, float partialTick, String overlayId,
      int width, int height) {
    this.graphics = new EverGraphics(nativeGraphics);
    this.partialTick = partialTick;
    this.overlayId = overlayId;
    this.width = width;
    this.height = height;
  }

  /**
   * Gets the abstracted EverGraphics context.
   *
   * @return EverGraphics wrapper instance.
   */
  public EverGraphics getGraphics() {
    return graphics;
  }

  public float getPartialTick() {
    return partialTick;
  }

  public String getOverlayId() {
    return overlayId;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  @Cancelable
  public static class Pre extends EverRenderGuiOverlayEvent {
    public Pre(Object graphics, float partialTick, String overlayId, int width, int height) {
      super(graphics, partialTick, overlayId, width, height);
    }
  }

  public static class Post extends EverRenderGuiOverlayEvent {
    public Post(Object graphics, float partialTick, String overlayId, int width, int height) {
      super(graphics, partialTick, overlayId, width, height);
    }
  }
}
