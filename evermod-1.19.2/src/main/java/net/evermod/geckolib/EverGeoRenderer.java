package net.evermod.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import javax.annotation.Nullable;

public abstract class EverGeoRenderer<T extends LivingEntity & EverAnimatable>
    extends GeoEntityRenderer<T> {

  protected EverGeoRenderer(EntityRendererProvider.Context context, EverGeoModel<T> modelProvider) {
    super(context, modelProvider);
  }

  // Firma para Common
  public abstract RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack,
      @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
      ResourceLocation texture);

  @Override
  public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer buffer, int light,
      int overlay, float r, float g, float b, float a) {
    this.renderEverRecursively(new EverGeoBone(bone), stack, buffer, light, overlay, r, g, b, a);
  }

  public void renderEverRecursively(EverGeoBone bone, PoseStack stack, VertexConsumer buffer,
      int light, int overlay, float r, float g, float b, float a) {
    super.renderRecursively(bone.internal(), stack, buffer, light, overlay, r, g, b, a);
  }

  @Override
  protected float getDeathMaxRotation(T animatable) {
    return 90f;
  }
}
