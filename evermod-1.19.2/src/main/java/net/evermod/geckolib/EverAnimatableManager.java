package net.evermod.geckolib;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class EverAnimatableManager {
  private final AnimationData internalData;
  private final EverAnimatable owner;

  public EverAnimatableManager(AnimationData data, EverAnimatable owner) {
    this.internalData = data;
    this.owner = owner;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public <T extends EverAnimatable> void addController(String name, int transitionTicks,
      EverAnimationPredicate<T> predicate) {

    internalData.addAnimationController(
        new AnimationController((IAnimatable) owner, name, transitionTicks, event -> {
          AnimationEvent rawEvent = (AnimationEvent) event;
          EverAnimationEvent<T> everEvent = new EverAnimationEvent<>(rawEvent);

          return predicate.test(everEvent) == EverPlayState.CONTINUE ? PlayState.CONTINUE
              : PlayState.STOP;
        }));
  }
}
