package net.evermod.client.graphics.pipeline;

import net.evermod.math.EverMatrix4f;

/**
 * Interface providing version-agnostic vertex building operations using a stateful fluent API.
 *
 * @author Wipodev
 */
public interface EverBufferBuilder {

  /**
   * Sets the position for the current vertex.
   *
   * @param matrix transformation matrix
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   * @return active buffer builder instance for chaining
   */
  EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z);

  /**
   * Sets the position for the current vertex with double precision.
   *
   * @param matrix transformation matrix
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   * @return active buffer builder instance for chaining
   */
  default EverBufferBuilder vertex(EverMatrix4f matrix, double x, double y, double z) {
    return vertex(matrix, (float) x, (float) y, (float) z);
  }

  /**
   * Sets the position for the current vertex without matrix transformation.
   *
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   * @return active buffer builder instance for chaining
   */
  default EverBufferBuilder vertex(float x, float y, float z) {
    return vertex(null, x, y, z);
  }

  /**
   * Sets the position for the current vertex without matrix transformation.
   *
   * @param x X coordinate
   * @param y Y coordinate
   * @param z Z coordinate
   * @return active buffer builder instance for chaining
   */
  default EverBufferBuilder vertex(double x, double y, double z) {
    return vertex(null, x, y, z);
  }

  /**
   * Sets the UV texture coordinates for the current vertex.
   *
   * @param u U texture coordinate
   * @param v V texture coordinate
   * @return active buffer builder instance for chaining
   */
  EverBufferBuilder uv(float u, float v);

  /**
   * Sets the RGBA color for the current vertex.
   *
   * @param r red component (0.0F - 1.0F)
   * @param g green component (0.0F - 1.0F)
   * @param b blue component (0.0F - 1.0F)
   * @param a alpha component (0.0F - 1.0F)
   * @return active buffer builder instance for chaining
   */
  EverBufferBuilder color(float r, float g, float b, float a);

  /**
   * Sets the RGBA color for the current vertex with white RGB by default.
   *
   * @param a alpha component (0.0F - 1.0F)
   * @return active buffer builder instance for chaining
   */
  default EverBufferBuilder color(float a) {
    return color(1.0F, 1.0F, 1.0F, a);
  }

  /**
   * Sets the lightmap coordinates for the current vertex.
   *
   * @param blockLight block light level (0 - 15)
   * @param skyLight sky light level (0 - 15)
   * @return active buffer builder instance for chaining
   */
  EverBufferBuilder uv2(int blockLight, int skyLight);

  /**
   * Sets the lightmap coordinates using a single packed light integer.
   *
   * @param packedLight sky and block light packed into a single int
   * @return active buffer builder instance for chaining
   */
  default EverBufferBuilder uv2(int packedLight) {
    int blockLight = packedLight & 0xFFFF;
    int skyLight = (packedLight >> 16) & 0xFFFF;
    return uv2(blockLight, skyLight);
  }

  /**
   * Finalizes the current vertex definition and writes accumulated state to the buffer.
   */
  void endVertex();
}
