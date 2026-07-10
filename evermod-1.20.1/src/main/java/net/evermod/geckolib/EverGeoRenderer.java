package net.evermod.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;
import org.jetbrains.annotations.Nullable;

public abstract class EverGeoRenderer<T extends LivingEntity & EverAnimatable>
    extends GeoEntityRenderer<T> {

  protected EverGeoRenderer(EntityRendererProvider.Context context, EverGeoModel<T> modelProvider) {
    super(context, modelProvider);

    // DE ACUERDO A LA DOCUMENTACIÓN: Añadimos la capa oficial en el constructor
    this.addRenderLayer(new BlockAndItemGeoLayer<T>(this) {

      @Override
      @Nullable
      protected ItemStack getStackForBone(GeoBone bone, T animatable) {
        // Abstraemos el GeoBone envolviéndolo en EverGeoBone
        ItemStack stack =
            EverGeoRenderer.this.getCustomItemForBone(new EverGeoBone(bone), animatable);
        return (stack != null && !stack.isEmpty()) ? stack
            : super.getStackForBone(bone, animatable);
      }

      @Override
      protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack,
          T animatable) {
        // 1. Buscamos el contexto gráfico abstracto del mod hijo
        EverDisplayContext everContext =
            EverGeoRenderer.this.getTransformContextForBone(new EverGeoBone(bone), animatable);

        // 2. Extraemos el enum real de Minecraft de forma interna
        ItemDisplayContext context =
            (everContext != null) ? everContext.getVanilla() : ItemDisplayContext.NONE;

        return (context != ItemDisplayContext.NONE) ? context
            : super.getTransformTypeForStack(bone, stack, animatable);
      }

      @Override
      protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack,
          T animatable, MultiBufferSource bufferSource, float partialTick, int packedLight,
          int packedOverlay) {
        poseStack.pushPose();

        // Permite al mod hijo aplicar offsets de forma agnóstica
        EverGeoRenderer.this.applyItemTransforms(poseStack, new EverGeoBone(bone), animatable);

        // Invocamos el comportamiento nativo de GeckoLib 4 para renderizar el ítem de forma segura
        super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick,
            packedLight, packedOverlay);

        poseStack.popPose();
      }
    });
  }

  @Override
  public RenderType getRenderType(T animatable, ResourceLocation texture,
      MultiBufferSource bufferSource, float partialTick) {
    return this.getEverRenderType(animatable, texture);
  }

  @Override
  public Color getRenderColor(T animatable, float partialTick, int packedLight) {
    float alpha = this.getEverAlpha(animatable);

    if (alpha < 1.0F) {
      int alphaInt = Math.max(0, Math.min(255, (int) (alpha * 255.0F)));
      return Color.ofRGBA(255, 255, 255, alphaInt);
    }
    return super.getRenderColor(animatable, partialTick, packedLight);
  }

  @Override
  public void preRender(PoseStack poseStack, T animatable, BakedGeoModel model,
      MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick,
      int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

    EverScale scale = this.getEverScale(animatable);
    this.scaleWidth = scale.width();
    this.scaleHeight = scale.height();

    super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick,
        packedLight, packedOverlay, red, green, blue, alpha);
  }

  // --- MÉTODOS HOOK ABSTRAÍDOS ---

  protected RenderType getEverRenderType(T animatable, ResourceLocation texture) {
    return RenderType.entityCutoutNoCull(texture);
  };

  protected float getEverAlpha(T animatable) {
    return 1.0F;
  }

  protected EverScale getEverScale(T animatable) {
    return EverScale.DEFAULT;
  }

  protected ItemStack getCustomItemForBone(EverGeoBone bone, T animatable) {
    return ItemStack.EMPTY;
  }

  protected EverDisplayContext getTransformContextForBone(EverGeoBone bone, T animatable) {
    return EverDisplayContext.NONE;
  }

  protected void applyItemTransforms(PoseStack poseStack, EverGeoBone bone, T animatable) {}

  protected float getDeathMaxRotation(T animatable) {
    return 90f;
  }
}
