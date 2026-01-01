package net.evermod.client.render;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.world.entity.LivingEntity;

public class ArmorLayerHelpers {

  public static <T extends LivingEntity, M extends HumanoidModel<T>> HumanoidArmorLayer<T, M, HumanoidModel<T>> createHumanoidArmorLayer(
      RenderLayerParent<T, M> parent, EntityRendererProvider.Context context) {
    return new HumanoidArmorLayer<>(parent,
        new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR)),
        new HumanoidModel<>(context.bakeLayer(ModelLayers.PLAYER_OUTER_ARMOR)),
        context.getModelManager());
  }
}
