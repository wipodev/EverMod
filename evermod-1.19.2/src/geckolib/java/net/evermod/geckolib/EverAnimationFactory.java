package net.evermod.geckolib;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class EverAnimationFactory {

  private final AnimationFactory internalFactory;

  public EverAnimationFactory(IAnimatable animatable) {
    // En GL3 se usa GeckoLibUtil para crear la factoría
    this.internalFactory = GeckoLibUtil.createFactory(animatable);
  }

  public AnimationFactory getInternal() {
    return this.internalFactory;
  }
}
