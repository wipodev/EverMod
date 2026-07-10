package net.evermod.geckolib;

import software.bernie.geckolib.animation.AnimationState;

public class EverAnimationEvent<T extends EverAnimatable> {
  private final AnimationState<T> internal;

  public EverAnimationEvent(AnimationState<T> internal) {
    this.internal = internal;
  }

  public T getAnimatable() {
    return internal.getAnimatable();
  }

  public boolean isMoving() {
    return internal.isMoving();
  }

  public float getLimbSwingAmount() {
    return internal.getLimbSwingAmount();
  }

  public EverAnimationController<T> getController() {
    return new EverAnimationController<>(internal.getController());
  }
}
