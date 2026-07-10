package net.evermod.geckolib;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoBone;

public abstract class EverGeoRenderer<T extends LivingEntity & EverAnimatable>
    extends GeoEntityRenderer<T> {

  protected EverGeoRenderer(EntityRendererProvider.Context context, EverGeoModel<T> modelProvider) {
    super(context, modelProvider);
  }

  @Override
  public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer,
      int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

    // 1. Envolvemos el GeoBone nativo en la abstracción de EverMod
    EverGeoBone everBone = new EverGeoBone(bone);

    // 2. Comprobamos si el mod hijo quiere renderizar un ítem en este hueso (ej: "right_hand")
    ItemStack stack = this.getCustomItemForBone(everBone, this.animatable);

    if (stack != null && !stack.isEmpty()) {
      poseStack.pushPose();

      // 3. Sistema de posicionamiento ultra-preciso de GeckoLib 3 usando pivotes (Tu fórmula
      // matemática exacta)
      poseStack.translate(bone.getPivotX() / 16.0F, bone.getPivotY() / 16.0F,
          bone.getPivotZ() / 16.0F);

      // Aplicamos rotaciones dinámicas de la animación
      poseStack.mulPose(Vector3f.ZP.rotation(bone.getRotationZ()));
      poseStack.mulPose(Vector3f.YP.rotation(bone.getRotationY()));
      poseStack.mulPose(Vector3f.XP.rotation(bone.getRotationX()));

      // Aplicamos escala del hueso
      poseStack.scale(bone.getScaleX(), bone.getScaleY(), bone.getScaleZ());

      // 4. Hook para que el mod hijo aplique offsets finos y rotaciones personalizadas
      // (poseStack.translate / mulPose)
      this.applyItemTransforms(poseStack, everBone, this.animatable);

      // 5. Obtenemos el contexto gráfico (TransformType)
      EverDisplayContext everContext = this.getTransformContextForBone(everBone, this.animatable);
      TransformType context = (everContext != null) ? everContext.getVanilla() : TransformType.NONE;

      if (context == TransformType.NONE) {
        context = TransformType.THIRD_PERSON_RIGHT_HAND;
      }

      // 6. Renderizado estático usando la variable interna de búferes gráficos de GeckoLib 3
      // (this.rtb)
      Minecraft.getInstance().getItemRenderer().renderStatic(this.animatable, stack, context, false,
          poseStack, this.rtb, // Usamos la rtb nativa del renderizador
          this.animatable.level, packedLight, OverlayTexture.NO_OVERLAY, this.animatable.getId());

      poseStack.popPose();

      // Re-asigmamos el buffer para prevenir corrupciones visuales en texturas translúcidas de
      // sub-huesos
      buffer =
          this.rtb.getBuffer(RenderType.entityTranslucent(getTextureLocation(this.animatable)));
    }

    // Continuamos el ciclo recursivo nativo de GeckoLib para renderizar el resto del cuerpo y sus
    // huesos hijos
    super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue,
        alpha);
  }

  @Override
  public RenderType getRenderType(T animatable, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight,
      ResourceLocation texture) {
    return this.getEverRenderType(animatable, texture);
  }

  @Override
  public Color getRenderColor(T animatable, float partialTick, PoseStack poseStack,
      MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight) {
    float alpha = this.getEverAlpha(animatable);

    if (alpha < 1.0F) {
      int alphaInt = Math.max(0, Math.min(255, (int) (alpha * 255.0F)));
      return Color.ofRGBA(255, 255, 255, alphaInt);
    }
    return super.getRenderColor(animatable, partialTick, poseStack, bufferSource, buffer,
        packedLight);
  }

  @Override
  public float getWidthScale(T animatable) {
    return this.getEverScale(animatable).width();
  }

  @Override
  public float getHeightScale(T animatable) {
    return this.getEverScale(animatable).height();
  }

  // --- API PÚBLICA / HOOKS ---

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
