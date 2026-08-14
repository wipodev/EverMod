package net.evermod.math;

import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Version-specific wrapper for JOML Quaternionf (1.20+).
 */
public class EverQuaternion {

  private final Quaternionf handle;

  public EverQuaternion(float x, float y, float z, float w) {
    this.handle = new Quaternionf(x, y, z, w);
  }

  public EverQuaternion(Vector3f axis, float angle, boolean degrees) {
    if (degrees) {
      angle = (float) Math.toRadians(angle);
    }
    this.handle = new Quaternionf().setAngleAxis(angle, axis.x(), axis.y(), axis.z());
  }

  public EverQuaternion(Quaternionf other) {
    this.handle = new Quaternionf(other);
  }

  public EverQuaternion(Object raw) {
    this.handle = (Quaternionf) raw;
  }

  /**
   * Unwraps the underlying JOML Quaternionf instance.
   */
  public Quaternionf getHandle() {
    return this.handle;
  }
}
