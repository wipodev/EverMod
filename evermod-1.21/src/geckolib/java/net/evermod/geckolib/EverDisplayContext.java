package net.evermod.geckolib;

import net.minecraft.world.item.ItemDisplayContext;

public enum EverDisplayContext {
  NONE(ItemDisplayContext.NONE), THIRD_PERSON_LEFT_HAND(
      ItemDisplayContext.THIRD_PERSON_LEFT_HAND), THIRD_PERSON_RIGHT_HAND(
          ItemDisplayContext.THIRD_PERSON_RIGHT_HAND), FIRST_PERSON_LEFT_HAND(
              ItemDisplayContext.FIRST_PERSON_LEFT_HAND), FIRST_PERSON_RIGHT_HAND(
                  ItemDisplayContext.FIRST_PERSON_RIGHT_HAND), HEAD(ItemDisplayContext.HEAD), GUI(
                      ItemDisplayContext.GUI), GROUND(
                          ItemDisplayContext.GROUND), FIXED(ItemDisplayContext.FIXED);

  private final ItemDisplayContext vanillaContext;

  EverDisplayContext(ItemDisplayContext vanillaContext) {
    this.vanillaContext = vanillaContext;
  }

  public ItemDisplayContext getVanilla() {
    return this.vanillaContext;
  }
}
