package net.evermod.world.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;

public abstract class EverEntity extends PathfinderMob {

  protected EverEntity(EntityType<? extends PathfinderMob> type, Level level) {
    super(type, level);
  }

  @Override
  protected final void defineSynchedData() {
    super.defineSynchedData();
    defineEverSynchedData();
  }

  protected abstract void defineEverSynchedData();

  protected final <T> void define(EntityDataAccessor<T> accessor, T defaultValue) {
    this.entityData.define(accessor, defaultValue);
  }

  public Level everLevel() {
    return this.level;
  }

  public boolean onEverGround() {
    return this.isOnGround();
  }
}
