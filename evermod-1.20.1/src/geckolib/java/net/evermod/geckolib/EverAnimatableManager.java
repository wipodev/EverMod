package net.evermod.geckolib;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animation.AnimationState;

public class EverAnimatableManager {
  private final AnimatableManager.ControllerRegistrar internalRegistrar;
  private final EverAnimatable owner;

  public EverAnimatableManager(AnimatableManager.ControllerRegistrar registrar,
      EverAnimatable owner) {
    this.internalRegistrar = registrar;
    this.owner = owner;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T extends EverAnimatable> void addController(String name, int transitionTicks,
      EverAnimationPredicate<T> predicate) {

    internalRegistrar
        .add(new AnimationController((GeoEntity) owner, name, transitionTicks, state -> {
          AnimationState rawState = (AnimationState) state;
          EverAnimationEvent<T> everEvent = new EverAnimationEvent<>(rawState);

          return predicate.test(everEvent) == EverPlayState.CONTINUE ? PlayState.CONTINUE
              : PlayState.STOP;
        }));
  }
}
