package net.evermod.client.graphics.pipeline;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.evermod.math.EverMatrix4f;

/**
 * Version-specific implementation of the vertex batching pipeline.
 *
 * @author Wipodev
 */
public class EverTesselator implements IEverTesselator, IEverBufferBuilder {

  private final Tesselator tesselator = Tesselator.getInstance();
  private BufferBuilder builder;

  @Override
  public IEverBufferBuilder beginPositionColor() {
    this.builder = this.tesselator.getBuilder();
    this.builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    return this;
  }

  @Override
  public IEverBufferBuilder beginPositionTex() {
    this.builder = this.tesselator.getBuilder();
    this.builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    return this;
  }

  @Override
  public IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float r, float g,
      float b, float a) {
    this.builder.vertex(matrix.getHandle(), x, y, z).color(r, g, b, a).endVertex();
    return this;
  }

  @Override
  public IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a) {
    this.builder.vertex(matrix.getHandle(), x, y, z).uv(u, v).color(r, g, b, a).endVertex();
    return this;
  }

  @Override
  public IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u,
      float v) {
    this.builder.vertex(matrix.getHandle(), x, y, z).uv(u, v).endVertex();
    return this;
  }

  @Override
  public void draw() {
    BufferUploader.drawWithShader(this.builder.end());
  }
}
