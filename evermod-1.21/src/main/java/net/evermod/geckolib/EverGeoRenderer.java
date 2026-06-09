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
import software.bernie.geckolib.cache.object.GeoBone;
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
        ItemDisplayContext context = (everContext != null) ? everContext.getVanilla() : ItemDisplayContext.NONE;
        
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

  public abstract RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack,
      @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight,
      ResourceLocation texture);

  // --- MÉTODOS HOOK ABSTRAÍDOS (Tu lógica de negocio pura) ---

  protected ItemStack getCustomItemForBone(EverGeoBone bone, T animatable) {
    return ItemStack.EMPTY;
  }

  // Ahora la firma de la API pública está unificada con tu propio enum
  protected EverDisplayContext getTransformContextForBone(EverGeoBone bone, T animatable) {
    return EverDisplayContext.NONE;
  }

  protected void applyItemTransforms(PoseStack poseStack, EverGeoBone bone, T animatable) {}

  protected float getDeathMaxRotation(T animatable) {
    return 90f;
  }
}