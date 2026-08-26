package net.evermod.client.graphics.pipeline;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
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
    this.builder =
        this.tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    return this;
  }

  @Override
  public EverBufferBuilder beginPositionTex() {
    this.builder = this.tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    return this;
  }

  @Override
  public EverBufferBuilder beginParticle() {
    this.builder = this.tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float r, float g,
      float b, float a) {
    this.builder.addVertex(matrix.getHandle(), x, y, z).setColor(r, g, b, a);
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a, int blockLight, int skyLight) {
    this.builder.addVertex(matrix.getHandle(), x, y, z).setUv(u, v).setColor(r, g, b, a)
        .setUv2(blockLight, skyLight);
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a) {
    this.builder.addVertex(matrix.getHandle(), x, y, z).setUv(u, v).setColor(r, g, b, a);
    return this;
  }

  @Override
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u,
      float v) {
    this.builder.addVertex(matrix.getHandle(), x, y, z).setUv(u, v);
    return this;
  }

  @Override
  public void draw() {
    MeshData meshData = this.builder.build();
    if (meshData != null) {
      BufferUploader.drawWithShader(meshData);
    }
  }
}
