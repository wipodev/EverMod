package net.evermod.geckolib;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public abstract class EverGeoModel<T extends EverAnimatable> extends GeoModel<T> {

  @Override
  public abstract ResourceLocation getModelResource(T entity);

  @Override
  public abstract ResourceLocation getTextureResource(T entity);

  @Override
  public abstract ResourceLocation getAnimationResource(T entity);

  protected EverHeadConfig getHeadConfig() {
    return null;
  }

  @Override
  public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
    super.setCustomAnimations(animatable, instanceId, animationState);

    EverHeadConfig config = getHeadConfig();
    if (config == null || config.boneName() == null) {
      return;
    }

    GeoBone head = this.getAnimationProcessor().getBone(config.boneName());
    if (head != null) {
      EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
      if (entityData == null) {
        return;
      }

      int unpausedMultiplier = !Minecraft.getInstance().isPaused() ? 1 : 0;

      float limitedPitch =
          Math.max(config.minPitch(), Math.min(config.maxPitch(), entityData.headPitch()));
      float limitedYaw =
          Math.max(config.minYaw(), Math.min(config.maxYaw(), entityData.netHeadYaw()));

      head.setRotX(limitedPitch * Mth.DEG_TO_RAD * unpausedMultiplier);
      head.setRotY(limitedYaw * Mth.DEG_TO_RAD * unpausedMultiplier);

      head.markRotationAsChanged();
    }
  }
}
