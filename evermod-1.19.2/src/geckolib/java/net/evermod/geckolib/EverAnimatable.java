package net.evermod.geckolib;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public interface EverAnimatable extends IAnimatable {
  EverAnimationFactory getEverFactory();

  void registerEverControllers(EverAnimatableManager data);

  // Adaptamos el método original de Geckolib 3 al nuestro
  @Override
  default void registerControllers(AnimationData data) {
    registerEverControllers(new EverAnimatableManager(data, this));
  }

  @Override
  default AnimationFactory getFactory() {
    return getEverFactory().getInternal();
  }
}
