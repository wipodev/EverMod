package net.evermod.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class EverSwordItem extends SwordItem {

  public EverSwordItem(Tier tier, int attackDamage, float attackSpeed, Item.Properties properties) {
    super(tier, attackDamage, attackSpeed, properties);
  }
}
