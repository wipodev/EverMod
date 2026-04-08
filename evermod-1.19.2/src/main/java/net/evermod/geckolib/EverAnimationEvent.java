package net.evermod.geckolib;

import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class EverAnimationEvent<T extends EverAnimatable> {
  private final AnimationEvent<T> internal;

  public EverAnimationEvent(AnimationEvent<T> internal) {
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

  // Aquí devolvemos nuestro controlador envuelto
  public EverAnimationController<T> getController() {
    return new EverAnimationController<>(internal.getController());
  }
}
