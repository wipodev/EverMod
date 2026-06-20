package net.evermod.geckolib;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public abstract class EverGeoModel<T extends EverAnimatable> extends AnimatedGeoModel<T> {

  private static final float RAD_FACTOR = (float) Math.PI / 180F;

  @Override
  public abstract ResourceLocation getModelResource(T entity);

  @Override
  public abstract ResourceLocation getTextureResource(T entity);

  @Override
  public abstract ResourceLocation getAnimationResource(T entity);

  protected EverHeadConfig getHeadConfig() {
    return null;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  @Override
  public void setCustomAnimations(T animatable, int instanceId, AnimationEvent animationEvent) {
    super.setCustomAnimations(animatable, instanceId, animationEvent);

    EverHeadConfig config = getHeadConfig();
    if (config == null || config.boneName() == null) {
      return;
    }

    IBone head = this.getAnimationProcessor().getBone(config.boneName());
    if (head != null) {
      EntityModelData extraData =
          (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
      AnimationData manager = animatable.getFactory().getOrCreateAnimationData(instanceId);

      int unpausedMultiplier =
          !Minecraft.getInstance().isPaused() || manager.shouldPlayWhilePaused ? 1 : 0;

      float limitedPitch =
          Math.max(config.minPitch(), Math.min(config.maxPitch(), extraData.headPitch));
      head.setRotationX(limitedPitch * RAD_FACTOR * unpausedMultiplier);

      float limitedYaw = Math.max(config.minYaw(), Math.min(config.maxYaw(), extraData.netHeadYaw));
      head.setRotationY(limitedYaw * RAD_FACTOR * unpausedMultiplier);
    }
  }
}
