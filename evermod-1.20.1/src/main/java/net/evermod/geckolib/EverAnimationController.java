package net.evermod.geckolib;

import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class EverAnimationController<T extends EverAnimatable> {
  private final AnimationController<T> internal;

  public EverAnimationController(AnimationController<T> internal) {
    this.internal = internal;
  }

  public void setAnimation(String name, boolean loop) {
    internal.setAnimation(RawAnimation.begin().then(name,
        loop ? software.bernie.geckolib.core.animation.Animation.LoopType.LOOP
            : software.bernie.geckolib.core.animation.Animation.LoopType.PLAY_ONCE));
  }

  public boolean isStopped() {
    return internal.getAnimationState() == AnimationController.State.STOPPED;
  }

  public void markNeedsReload() {
    internal.forceAnimationReset();
  }
}
