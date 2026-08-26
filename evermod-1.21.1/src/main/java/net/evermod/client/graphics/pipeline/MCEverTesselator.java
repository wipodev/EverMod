package net.evermod.client.graphics.pipeline;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.evermod.math.EverMatrix4f;

/**
 * Version-specific implementation of the vertex batching pipeline for Minecraft 1.21+.
 *
 * @author Wipodev
 */
public class MCEverTesselator implements EverTesselator, EverBufferBuilder {

  private final Tesselator tesselator = Tesselator.getInstance();
  private BufferBuilder builder;
  private EverMatrix4f matrix;
  private float x, y, z;
  private float u = 0.0F, v = 0.0F;
  private float r = 1.0F, g = 1.0F, b = 1.0F, a = 1.0F;
  private int blockLight = 15, skyLight = 15;

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
  public EverBufferBuilder vertex(EverMatrix4f matrix, float x, float y, float z) {
    this.matrix = matrix;
    this.x = x;
    this.y = y;
    this.z = z;
    return this;
  }

  @Override
  public EverBufferBuilder uv(float u, float v) {
    this.u = u;
    this.v = v;
    return this;
  }

  @Override
  public EverBufferBuilder color(float r, float g, float b, float a) {
    this.r = r;
    this.g = g;
    this.b = b;
    this.a = a;
    return this;
  }

  @Override
  public EverBufferBuilder uv2(int blockLight, int skyLight) {
    this.blockLight = blockLight;
    this.skyLight = skyLight;
    return this;
  }

  @Override
  public void endVertex() {
    if (this.matrix != null) {
      this.builder.addVertex(this.matrix.getHandle(), x, y, z);
    } else {
      this.builder.addVertex(x, y, z);
    }

    this.builder.setUv(u, v)
        .setColor(r, g, b, a)
        .setUv2(blockLight, skyLight);

    this.resetVertexState();
  }

  @Override
  public void draw() {
    MeshData meshData = this.builder.build();
    if (meshData != null) {
      BufferUploader.drawWithShader(meshData);
    }
  }

  private void resetVertexState() {
    this.matrix = null;
    this.x = 0.0F;
    this.y = 0.0F;
    this.z = 0.0F;
    this.u = 0.0F;
    this.v = 0.0F;
    this.r = 1.0F;
    this.g = 1.0F;
    this.b = 1.0F;
    this.a = 1.0F;
    this.blockLight = 15;
    this.skyLight = 15;
  }
}
