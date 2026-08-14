package net.evermod.client.gui.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.evermod.math.EverMatrix4f;

/**
 * Minecraft 1.21+ implementation of vertex batching pipeline.
 *
 * @author Wipodev
 */
public class EverTesselator implements IEverTesselator, IEverBufferBuilder {

  private final Tesselator tesselator = Tesselator.getInstance();
  private BufferBuilder builder;

  @Override
  public IEverBufferBuilder beginPositionColor() {
    this.builder =
        this.tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
    return this;
  }

  @Override
  public IEverBufferBuilder beginPositionTex() {
    this.builder = this.tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
    return this;
  }

  @Override
  public IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float r, float g,
      float b, float a) {
    this.builder.addVertex(matrix.getHandle(), x, y, z).setColor(r, g, b, a);
    return this;
  }

  @Override
  public IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u, float v,
      float r, float g, float b, float a) {
    this.builder.addVertex(matrix.getHandle(), x, y, z).setUv(u, v).setColor(r, g, b, a);
    return this;
  }

  @Override
  public IEverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z, float u,
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
