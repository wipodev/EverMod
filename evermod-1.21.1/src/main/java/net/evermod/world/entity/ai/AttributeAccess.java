package net.evermod.world.entity.ai;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

public class AttributeAccess {

  /**
   * Modifies the base value of a specific attribute.
   * 
   * @param entity The live entity (Player, Mob, etc.)
   * @param attribute The attribute holder (e.g., Attributes.MAX_HEALTH)
   * @param newValue The new base value.
   */
  public static void setBaseValue(LivingEntity entity, Holder<Attribute> attribute,
      double newValue) {
    AttributeInstance instance = entity.getAttribute(attribute);
    if (instance != null) {
      instance.setBaseValue(newValue);
    }
  }

  /**
   * Add an amount to the current base value.
   * 
   * @param entity The live entity (Player, Mob, etc.)
   * @param attribute The attribute holder (e.g., Attributes.MAX_HEALTH)
   * @param amount the value to add.
   */
  public static void addToBaseValue(LivingEntity entity, Holder<Attribute> attribute,
      double amount) {
    AttributeInstance instance = entity.getAttribute(attribute);
    if (instance != null) {
      instance.setBaseValue(instance.getBaseValue() + amount);
    }
  }
}
