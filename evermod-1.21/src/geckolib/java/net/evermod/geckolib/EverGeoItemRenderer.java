package net.evermod.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.util.Color;
import javax.annotation.Nullable;

public abstract class EverGeoItemRenderer<T extends Item & EverAnimatable>
    extends GeoItemRenderer<T> {

  protected EverGeoItemRenderer(EverGeoModel<T> modelProvider) {
    super(modelProvider);
  }

  public RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack,
      @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
      ResourceLocation texture) {
    return RenderType.entityCutout(texture);
  }

  @Override
  public void renderChildBones(PoseStack poseStack, T animatable, GeoBone bone,
      RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer,
      boolean isReRender, float partialTick, int packedLight, int packedOverlay, int color) {
    float a = ((color >> 24) & 0xFF) / 255f;
    float r = ((color >> 16) & 0xFF) / 255f;
    float g = ((color >> 8) & 0xFF) / 255f;
    float b = (color & 0xFF) / 255f;

    this.renderEverRecursively(new EverGeoBone(bone), poseStack, buffer, packedLight, packedOverlay,
        r, g, b, a);
  }

  public void renderEverRecursively(EverGeoBone bone, PoseStack stack, VertexConsumer buffer,
      int light, int overlay, float r, float g, float b, float a) {
    int argb = Color.ofRGBA(r, g, b, a).getColor();
    super.renderChildBones(stack, this.animatable, bone.internal(), null, null, buffer, false, 0,
        light, overlay, argb);
  }
}
