package net.evermod.math;

import com.mojang.math.Vector3f;


public class EverVector3f extends Vector3f {

  public EverVector3f() {
    super();
  }

  public EverVector3f(float x, float y, float z) {
    super(x, y, z);
  }

  public EverVector3f(Vector3f other) {
    super(other.x(), other.y(), other.z());
  }
}
