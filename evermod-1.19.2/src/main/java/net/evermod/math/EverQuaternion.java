package net.evermod.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

/**
 * EverMod Quaternion implementation extending Mojang's native class.
 */
public class EverQuaternion extends Quaternion {

  public EverQuaternion(float x, float y, float z, float w) {
    super(x, y, z, w);
  }

  public EverQuaternion(Vector3f axis, float angle, boolean degrees) {
    super(axis, angle, degrees);
  }

  public EverQuaternion(Quaternion other) {
    super(other);
  }
}
