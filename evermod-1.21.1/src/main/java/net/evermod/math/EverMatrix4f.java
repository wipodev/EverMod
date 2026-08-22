package net.evermod.math;

import org.joml.Matrix4f;

public class EverMatrix4f {

  private final Matrix4f handle;

  public EverMatrix4f() {
    this.handle = new Matrix4f();
  }

  public EverMatrix4f(Matrix4f other) {
    this.handle = new Matrix4f(other);
  }

  public EverMatrix4f(Object raw) {
    this.handle = (Matrix4f) raw;
  }

  public double getTranslationX() {
    return this.handle.m30();
  }

  public double getTranslationY() {
    return this.handle.m31();
  }

  /**
   * Unwraps the underlying JOML Matrix4f instance.
   */
  public Matrix4f getHandle() {
    return this.handle;
  }
}
