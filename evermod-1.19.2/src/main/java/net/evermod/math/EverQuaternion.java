package net.evermod.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

/**
 * Version-specific wrapper for Mojang Quaternion (1.19.2 and below).
 */
public class EverQuaternion {

  private final Quaternion handle;

  public EverQuaternion(float x, float y, float z, float w) {
    this.handle = new Quaternion(x, y, z, w);
  }

  public EverQuaternion(Vector3f axis, float angle, boolean degrees) {
    this.handle = new Quaternion(axis, angle, degrees);
  }

  public EverQuaternion(Quaternion other) {
    this.handle = new Quaternion(other);
  }

  public EverQuaternion(Object raw) {
    this.handle = (Quaternion) raw;
  }

  /**
   * Unwraps the underlying Mojang Quaternion instance.
   */
  public Quaternion getHandle() {
    return this.handle;
  }
}
