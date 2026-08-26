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
public class MCEverTesselator implements EverTesselator, EverBufferBuilder {

  private final Tesselator tesselator = Tesselator.getInstance();
  private BufferBuilder builder;

  @Override
  public EverBufferBuilder beginPositionColor() {
    this.builder = this.tesselator.getBuilder();
    this.builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    return this;
  }

  @Override
  public EverBufferBuilder beginPositionTex() {
    this.builder = this.tesselator.getBuilder();
    this.builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    return this;
  }

  @Override
  public EverBufferBuilder beginParticle() {
    this.builder = this.tesselator.getBuilder();
    this.builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float r, float g,
      float b, float a) {
    this.builder.vertex(matrix.getHandle(), x, y, z).color(r, g, b, a).endVertex();
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a, int blockLight, int skyLight) {
    int packedLight = (skyLight << 16) | blockLight;
    this.builder.vertex(matrix.getHandle(), x, y, z).uv(u, v).color(r, g, b, a).uv2(packedLight)
        .endVertex();
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a) {
    this.builder.vertex(matrix.getHandle(), x, y, z).uv(u, v).color(r, g, b, a).endVertex();
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u,
      float v) {
    this.builder.vertex(matrix.getHandle(), x, y, z).uv(u, v).endVertex();
    return this;
  }

  @Override
  public void draw() {
    BufferUploader.drawWithShader(this.builder.end());
  }
}
