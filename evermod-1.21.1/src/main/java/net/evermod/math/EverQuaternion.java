package net.evermod.math;

import org.joml.Quaternionf;


public class EverQuaternion extends Quaternionf {

  public EverQuaternion() {
    super();
  }

  public EverQuaternion(float x, float y, float z, float w) {
    super(x, y, z, w);
  }

  public EverQuaternion(Quaternionf other) {
    super(other);
  }
}
