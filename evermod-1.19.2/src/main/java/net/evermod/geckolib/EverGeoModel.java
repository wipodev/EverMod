package net.evermod.geckolib;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public abstract class EverGeoModel<T extends EverAnimatable> extends AnimatedGeoModel<T> {

  @Override
  public abstract ResourceLocation getModelResource(T entity);

  @Override
  public abstract ResourceLocation getTextureResource(T entity);

  @Override
  public abstract ResourceLocation getAnimationResource(T entity);
}
