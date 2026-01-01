package net.evermod.utils;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

public class EquipmentHelpers {

  public static EquipmentSlot getArmorSlot(ArmorItem armorItem) {
    return armorItem.getEquipmentSlot();
  }
}
