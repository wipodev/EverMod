package net.evermod.math;

import org.joml.Vector3f;


public class EverVector3f extends Vector3f {

  public EverVector3f() {
    super();
  }

  public EverVector3f(float x, float y, float z) {
    super(x, y, z);
  }

  public EverVector3f(Vector3f other) {
    super(other);
  }

  /**
   * Helper method to preserve rotationDegrees syntax from Mojang math.
   *
   * @param degrees Rotation angle in degrees.
   * @return A native EverQuaternion instance.
   */
  public EverQuaternion rotationDegrees(float degrees) {
    float radians = (float) Math.toRadians(degrees);
    return new EverQuaternion(
        new org.joml.Quaternionf().setAngleAxis(radians, this.x, this.y, this.z));
  }
}
