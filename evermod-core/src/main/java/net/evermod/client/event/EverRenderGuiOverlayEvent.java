package net.evermod.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.evermod.client.graphics.EverGraphics;
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

  protected EverRenderGuiOverlayEvent(EverGraphics graphics, float partialTick, String overlayId,
      int width, int height) {
    this.graphics = graphics;
    this.partialTick = partialTick;
    this.overlayId = overlayId;
    this.width = width;
    this.height = height;
  }

  protected EverRenderGuiOverlayEvent(PoseStack poseStack, float partialTick, String overlayId,
      int width, int height) {
    this(EverGraphics.of(poseStack), partialTick, overlayId, width, height);
  }

  /**
   * Gets the abstracted EverGraphics context.
   *
   * @return EverGraphics wrapper instance.
   */
  public EverGraphics getGraphics() {
    return this.graphics;
  }

  public float getPartialTick() {
    return this.partialTick;
  }

  public String getOverlayId() {
    return this.overlayId;
  }

  public int getWidth() {
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  @Cancelable
  public static class Pre extends EverRenderGuiOverlayEvent {

    public Pre(EverGraphics graphics, float partialTick, String overlayId, int width, int height) {
      super(graphics, partialTick, overlayId, width, height);
    }

    public Pre(PoseStack poseStack, float partialTick, String overlayId, int width, int height) {
      super(poseStack, partialTick, overlayId, width, height);
    }
  }

  public static class Post extends EverRenderGuiOverlayEvent {

    public Post(EverGraphics graphics, float partialTick, String overlayId, int width, int height) {
      super(graphics, partialTick, overlayId, width, height);
    }

    public Post(PoseStack poseStack, float partialTick, String overlayId, int width, int height) {
      super(poseStack, partialTick, overlayId, width, height);
    }
  }
}
