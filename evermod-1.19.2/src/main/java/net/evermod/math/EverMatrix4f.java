package net.evermod.math;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import com.mojang.math.Matrix4f;

public class EverMatrix4f {

  private static final FloatBuffer BUFFER = BufferUtils.createFloatBuffer(16);
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

  private float getComponent(int index) {
    BUFFER.clear();
    this.handle.store(BUFFER);
    return BUFFER.get(index);
  }

  public double getTranslationX() {
    return getComponent(12);
  }

  public double getTranslationY() {
    return getComponent(13);
  }

  /**
   * Unwraps the underlying Mojang Matrix4f instance.
   */
  public Matrix4f getHandle() {
    return this.handle;
  }
}
