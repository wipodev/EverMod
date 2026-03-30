package net.evermod.world.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import java.util.function.Supplier;

public class EverItem extends Item {

  protected EverItem(EverItem.Properties properties) {
    super(properties);
  }

  public static class Properties extends Item.Properties implements Supplier<Item> {

    @Override
    public Properties food(FoodProperties food) {
      super.food(food);
      return this;
    }

    @Override
    public Properties stacksTo(int count) {
      super.stacksTo(count);
      return this;
    }

    @Override
    public Properties defaultDurability(int durability) {
      super.defaultDurability(durability);
      return this;
    }

    @Override
    public Properties durability(int durability) {
      super.durability(durability);
      return this;
    }

    @Override
    public Properties craftRemainder(Item item) {
      super.craftRemainder(item);
      return this;
    }

    // Método personalizado de EverMod
    public Properties tab(EverCreativeTab tab) {
      super.tab(tab.getVanillaTab());
      return this;
    }

    @Override
    public Properties rarity(Rarity rarity) {
      super.rarity(rarity);
      return this;
    }

    @Override
    public Properties fireResistant() {
      super.fireResistant();
      return this;
    }

    @Override
    public Properties setNoRepair() {
      super.setNoRepair();
      return this;
    }

    @Override
    public Item get() {
      return new EverItem(this);
    }
  }
}
