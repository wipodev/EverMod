package net.evermod.geckolib;

import net.minecraft.client.renderer.block.model.ItemTransforms.TransformType;

public enum EverDisplayContext {
  NONE(TransformType.NONE), THIRD_PERSON_LEFT_HAND(
      TransformType.THIRD_PERSON_LEFT_HAND), THIRD_PERSON_RIGHT_HAND(
          TransformType.THIRD_PERSON_RIGHT_HAND), FIRST_PERSON_LEFT_HAND(
              TransformType.FIRST_PERSON_LEFT_HAND), FIRST_PERSON_RIGHT_HAND(
                  TransformType.FIRST_PERSON_RIGHT_HAND), HEAD(TransformType.HEAD), GUI(
                      TransformType.GUI), GROUND(TransformType.GROUND), FIXED(TransformType.FIXED);

  private final TransformType vanillaTransform;

  EverDisplayContext(TransformType vanillaTransform) {
    this.vanillaTransform = vanillaTransform;
  }

  public TransformType getVanilla() {
    return this.vanillaTransform;
  }
}
