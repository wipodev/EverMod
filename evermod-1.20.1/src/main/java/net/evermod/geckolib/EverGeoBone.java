package net.evermod.geckolib;

import software.bernie.geckolib.cache.object.GeoBone;

public record EverGeoBone(GeoBone internal) {

  public String getName() {
    if (this.internal != null && this.internal.getClass().getName().contains("GeoBone")) {
      return ((GeoBone) this.internal).getName();
    }
    return "";
  }
}
