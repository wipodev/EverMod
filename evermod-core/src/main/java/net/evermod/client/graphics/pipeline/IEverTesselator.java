package net.evermod.client.graphics.pipeline;

/**
 * Interface providing version-agnostic vertex buffering operations.
 *
 * @author Wipodev
 */
public interface IEverTesselator {

  /**
   * Begins building vertices for position-color primitives.
   *
   * @return active buffer builder instance
   */
  IEverBufferBuilder beginPositionColor();

  /**
   * Begins building vertices for position-texture primitives.
   *
   * @return active buffer builder instance
   */
  IEverBufferBuilder beginPositionTex();

  /**
   * Flushes and renders accumulated buffer data to active shader.
   */
  void draw();
}
