package net.evermod.world.entity;

import net.minecraft.world.level.Level;
import javax.annotation.Nonnull;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;

public abstract class EverEntity extends PathfinderMob {

  private SynchedEntityData.Builder builder;

  protected EverEntity(EntityType<? extends PathfinderMob> type, Level level) {
    super(type, level);
  }

  @Override
  protected final void defineSynchedData(@Nonnull SynchedEntityData.Builder builder) {
    super.defineSynchedData(builder);
    this.builder = builder;
    defineEverSynchedData();
    this.builder = null;
  }

  protected abstract void defineEverSynchedData();

  protected final <T> void define(EntityDataAccessor<T> accessor, T defaultValue) {
    builder.define(accessor, defaultValue);
  }

  public Level everLevel() {
    return this.level();
  }

  public boolean onEverGround() {
    return this.onGround();
  }
}
