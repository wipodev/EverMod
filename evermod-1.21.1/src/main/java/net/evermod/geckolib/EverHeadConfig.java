package net.evermod.geckolib;

public record EverHeadConfig(String boneName, float minPitch, float maxPitch, float minYaw,
    float maxYaw) {

  public EverHeadConfig(String boneName) {
    this(boneName, -180f, 180f, -180f, 180f);
  }
}
