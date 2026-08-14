package net.evermod.client.gui.render;

/**
 * Interface providing version-agnostic vertex buffering operations.
 *
 * @author Wipodev
 */
public interface IEverTesselator {

  /**
   * Begins building vertices for position-color primitives.
   */
  IEverBufferBuilder beginPositionColor();

  /**
   * Begins building vertices for position-texture primitives.
   */
  IEverBufferBuilder beginPositionTex();

  /**
   * Flushes and renders accumulated buffer data to active shader.
   */
  void draw();
}
