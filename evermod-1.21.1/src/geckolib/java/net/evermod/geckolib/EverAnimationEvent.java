package net.evermod.geckolib;

import software.bernie.geckolib.animation.AnimationState;

public class EverAnimationEvent<T extends EverAnimatable> {
  private final AnimationState<T> internal;
  private final EverAnimationController<T> controller;

  public EverAnimationEvent(AnimationState<T> internal) {
    this.internal = internal;
    this.controller = new EverAnimationController<>(internal.getController());
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
    return this.controller;
  }
}
