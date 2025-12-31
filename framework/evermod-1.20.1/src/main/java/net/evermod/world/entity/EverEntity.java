package net.evermod.world.entity;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;

public interface EverEntity {

  default Level everLevel() {
    return ((Entity) (Object) this).level();
  }
}
