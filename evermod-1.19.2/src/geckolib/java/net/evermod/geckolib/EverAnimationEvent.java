package net.evermod.geckolib;

import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class EverAnimationEvent<T extends EverAnimatable> {
  private final AnimationEvent<T> internal;
  private final EverAnimationController<T> controller;

  public EverAnimationEvent(AnimationEvent<T> internal) {
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
