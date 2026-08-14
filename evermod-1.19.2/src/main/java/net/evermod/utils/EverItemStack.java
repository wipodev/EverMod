package net.evermod.utils;

import net.minecraft.world.item.ItemStack;

/**
 * Legacy NBT-based ItemStack helper implementation.
 *
 * @author Wipodev
 */

public final class EverItemStack {

  private EverItemStack() {}

  public static boolean isSameItemSameComponents(ItemStack stackA, ItemStack stackB) {
    return ItemStack.isSameItemSameTags(stackA, stackB);
  }

}
