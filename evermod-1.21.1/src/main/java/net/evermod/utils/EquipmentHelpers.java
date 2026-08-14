package net.evermod.utils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

public final class EquipmentHelpers {

  private EquipmentHelpers() {}

  public static EquipmentSlot getArmorSlot(ArmorItem armorItem) {
    return armorItem.getEquipmentSlot();
  }
}
