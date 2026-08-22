package net.evermod.math;

public final class EverMath {

  private EverMath() {}

  public static double clamp(double val, double min, double max) {
    return Math.max(min, Math.min(val, max));
  }
}
