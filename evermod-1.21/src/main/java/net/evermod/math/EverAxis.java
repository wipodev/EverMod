package net.evermod.math;

import com.mojang.math.Axis;
import org.joml.Quaternionf;

public enum EverAxis {
  XP(Axis.XP), YP(Axis.YP), ZP(Axis.ZP);

  private final Axis container;

  EverAxis(Axis container) {
    this.container = container;
  }

  public Quaternionf rotationDegrees(float degrees) {
    return this.container.rotationDegrees(degrees);
  }
}
