package net.evermod.geckolib;

import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType.EDefaultLoopTypes;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.AnimationState;

public class EverAnimationController<T extends EverAnimatable> {
  private final AnimationController<T> internal;

  public EverAnimationController(AnimationController<T> internal) {
    this.internal = internal;
  }

  public void setAnimation(String name, boolean loop) {
    internal.setAnimation(new AnimationBuilder().addAnimation(name,
        loop ? EDefaultLoopTypes.LOOP : EDefaultLoopTypes.PLAY_ONCE));
  }

  public boolean isStopped() {
    return internal.getAnimationState() == AnimationState.Stopped;
  }

  public void markNeedsReload() {
    internal.markNeedsReload();
  }
}
