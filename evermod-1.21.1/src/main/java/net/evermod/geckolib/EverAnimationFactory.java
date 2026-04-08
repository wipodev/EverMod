package net.evermod.geckolib;

import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EverAnimationFactory {

  private final AnimatableInstanceCache internalFactory;

  public EverAnimationFactory(GeoEntity animatable) {
    // En GL4 se llama InstanceCache
    this.internalFactory = GeckoLibUtil.createInstanceCache(animatable);
  }

  public AnimatableInstanceCache getInternal() {
    return this.internalFactory;
  }
}
