package net.evermod.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import javax.annotation.Nullable;

public abstract class EverGeoItemRenderer<T extends Item & EverAnimatable>
    extends GeoItemRenderer<T> {

  protected EverGeoItemRenderer(EverGeoModel<T> modelProvider) {
    super(modelProvider);
  }

  @Override
  public RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack,
      @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
      ResourceLocation texture) {
    return RenderType.entityCutout(texture);
  }

  @Override
  public void renderRecursively(GeoBone bone, PoseStack stack, VertexConsumer buffer, int light,
      int overlay, float r, float g, float b, float a) {
    this.renderEverRecursively(new EverGeoBone(bone), stack, buffer, light, overlay, r, g, b, a);
  }

  public void renderEverRecursively(EverGeoBone bone, PoseStack stack, VertexConsumer buffer,
      int light, int overlay, float r, float g, float b, float a) {
    super.renderRecursively(bone.internal(), stack, buffer, light, overlay, r, g, b, a);
  }
}
