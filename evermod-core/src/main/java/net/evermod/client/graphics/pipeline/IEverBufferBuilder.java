package net.evermod.client.graphics.pipeline;

import net.evermod.math.EverMatrix4f;

/**
 * Interface providing version-agnostic vertex building operations.
 *
 * @author Wipodev
 */
public interface IEverBufferBuilder {

  /**
   * Adds a vertex with position and RGBA color.
   *
   * @param matrix transformation matrix
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   * @param r red component (0.0F - 1.0F)
   * @param g green component (0.0F - 1.0F)
   * @param b blue component (0.0F - 1.0F)
   * @param a alpha component (0.0F - 1.0F)
   * @return active buffer builder instance for chaining
   */
  IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float r, float g,
      float b, float a);

  /**
   * Adds a vertex with position, UV texture coordinates, and RGBA color.
   *
   * @param matrix transformation matrix
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   * @param u U texture coordinate
   * @param v V texture coordinate
   * @param r red component (0.0F - 1.0F)
   * @param g green component (0.0F - 1.0F)
   * @param b blue component (0.0F - 1.0F)
   * @param a alpha component (0.0F - 1.0F)
   * @return active buffer builder instance for chaining
   */
  IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a);

  /**
   * Adds a vertex with position and UV texture coordinates, delegating color to active shader state.
   *
   * @param matrix transformation matrix
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   * @param u U texture coordinate
   * @param v V texture coordinate
   * @return active buffer builder instance for chaining
   */
  IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v);
}
