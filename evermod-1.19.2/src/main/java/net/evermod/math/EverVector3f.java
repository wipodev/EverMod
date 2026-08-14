package net.evermod.math;

import com.mojang.math.Vector3f;

/**
 * Version-specific wrapper for Mojang Vector3f (1.19.2 and below).
 */
public class EverVector3f {

  private final Vector3f handle;

  public EverVector3f() {
    this.handle = new Vector3f();
  }

  public EverVector3f(float x, float y, float z) {
    this.handle = new Vector3f(x, y, z);
  }

  public EverVector3f(Vector3f other) {
    this.handle = new Vector3f(other.x(), other.y(), other.z());
  }

  public EverVector3f(Object raw) {
    this.handle = (Vector3f) raw;
  }

  public float x() {
    return this.handle.x();
  }

  public float y() {
    return this.handle.y();
  }

  public float z() {
    return this.handle.z();
  }

  /**
   * Unwraps the underlying Mojang Vector3f instance.
   */
  public Vector3f getHandle() {
    return this.handle;
  }
}
