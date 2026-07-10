package net.evermod.geckolib;

public final class EverScale {
  public static final EverScale DEFAULT = new EverScale(1.0F, 1.0F);

  private final float width;
  private final float height;

  private EverScale(float width, float height) {
    this.width = width;
    this.height = height;
  }

  public static EverScale of(float width, float height) {
    return new EverScale(width, height);
  }

  public static EverScale uniform(float scale) {
    return new EverScale(scale, scale);
  }

  public float width() {
    return this.width;
  }

  public float height() {
    return this.height;
  }
}
