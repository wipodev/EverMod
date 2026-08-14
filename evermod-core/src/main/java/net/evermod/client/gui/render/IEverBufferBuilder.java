package net.evermod.client.gui.render;

import net.evermod.math.EverMatrix4f;

/**
 * Interface providing version-agnostic vertex building operations.
 *
 * @author Wipodev
 */
public interface IEverBufferBuilder {

  /**
   * Adds a vertex with position and RGBA color.
   */
  IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float r, float g,
      float b, float a);

  /**
   * Adds a vertex with position, UV texture coordinates, and RGBA color.
   */
  IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a);

  /**
     * Adds a vertex with position and UV texture coordinates, delegating color to active shader state.
     */
  IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v);
}
