package net.evermod.client.graphics.pipeline;

/**
 * Interface providing version-agnostic vertex buffering operations.
 *
 * @author Wipodev
 */
public interface EverTesselator {

  /**
   * Begins building vertices for position-color primitives.
   *
   * @return active buffer builder instance
   */
  EverBufferBuilder beginPositionColor();

  /**
   * Begins building vertices for position-texture primitives.
   *
   * @return active buffer builder instance
   */
  EverBufferBuilder beginPositionTex();

  /**
   * Begins building vertices for particle primitives.
   *
   * @return active buffer builder instance
   */
  EverBufferBuilder beginParticle();

  /**
   * Flushes and renders accumulated buffer data to active shader.
   */
  void draw();
}
