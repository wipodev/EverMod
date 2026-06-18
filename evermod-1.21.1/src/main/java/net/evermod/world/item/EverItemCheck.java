package net.evermod.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public final class EverItemCheck {

  private EverItemCheck() {}

  /**
   * Comprueba si el ItemStack es un alimento comestible.
   */
  public static boolean isEdible(ItemStack stack) {
    if (stack == null || stack.isEmpty()) {
      return false;
    }
    return stack.has(DataComponents.FOOD);
  }

  /**
   * Comprueba si el ItemStack corresponde a cualquier semilla de cultivo vanilla.
   */
  public static boolean isSeed(ItemStack stack) {
    if (stack == null || stack.isEmpty()) {
      return false;
    }

    Item item = stack.getItem();
    return item == Items.WHEAT_SEEDS || item == Items.MELON_SEEDS || item == Items.PUMPKIN_SEEDS
        || item == Items.BEETROOT_SEEDS;
  }
}
