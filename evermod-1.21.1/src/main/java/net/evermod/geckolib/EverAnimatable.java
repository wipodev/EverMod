package net.evermod.geckolib;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;

public interface EverAnimatable extends GeoEntity {
  EverAnimationFactory getEverFactory();

  void registerEverControllers(EverAnimatableManager data);

  // Adaptamos el método de Geckolib 4 al nuestro
  @Override
  default void registerControllers(AnimatableManager.ControllerRegistrar data) {
    registerEverControllers(new EverAnimatableManager(data, this));
  }

  @Override
  default AnimatableInstanceCache getAnimatableInstanceCache() {
    return getEverFactory().getInternal();
  }
}
