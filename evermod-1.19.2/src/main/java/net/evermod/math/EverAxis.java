package net.evermod.math;

import com.mojang.math.Vector3f;
import com.mojang.math.Quaternion;

public enum EverAxis {
  XP(Vector3f.XP), YP(Vector3f.YP), ZP(Vector3f.ZP);

  private final Vector3f container;

  EverAxis(Vector3f container) {
    this.container = container;
  }

  public Quaternion rotationDegrees(float degrees) {
    return this.container.rotationDegrees(degrees);
  }
}
