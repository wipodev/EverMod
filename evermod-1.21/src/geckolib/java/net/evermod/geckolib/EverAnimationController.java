package net.evermod.geckolib;

import software.bernie.geckolib.animation.Animation.LoopType;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;

public class EverAnimationController<T extends EverAnimatable> {
  private final AnimationController<T> internal;

  public EverAnimationController(AnimationController<T> internal) {
    this.internal = internal;
  }

  public void setAnimation(String name, boolean loop) {
    internal
        .setAnimation(RawAnimation.begin().then(name, loop ? LoopType.LOOP : LoopType.PLAY_ONCE));
  }

  public String getCurrentAnimation() {
    if (this.internal.getCurrentAnimation() != null) {
      return this.internal.getCurrentAnimation().animation().name();
    }
    return "";
  }

  public void setAnimationSpeed(double speed) {
    this.internal.setAnimationSpeed(speed);
  }

  public boolean isStopped() {
    return internal.getAnimationState() == AnimationController.State.STOPPED;
  }

  public void markNeedsReload() {
    internal.forceAnimationReset();
  }
}
