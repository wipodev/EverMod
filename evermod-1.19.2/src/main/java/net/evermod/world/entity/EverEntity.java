package net.evermod.world.entity;

import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

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

  public static void spawn(EverEntity entity, Level level, MobSpawnType spawnType) {
    if (level instanceof ServerLevel serverLevel) {
      entity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()),
          spawnType, null, null);
      serverLevel.addFreshEntity(entity);
    }
  }

  public static void spawn(EverEntity entity, Level level, MobSpawnType spawnType,
      @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
    if (level instanceof ServerLevel serverLevel) {
      entity.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(entity.blockPosition()),
          spawnType, spawnData, dataTag);
      serverLevel.addFreshEntity(entity);
    }
  }

  public void everFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
      MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
    // Implementación opcional por el usuario
  }

  @Override
  public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty,
      MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
    SpawnGroupData result = super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    this.everFinalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    return result;
  }

  public void everDropExperience() {
    this.dropExperience();
  }

  public Level everLevel() {
    return this.level;
  }

  public boolean onEverGround() {
    return this.isOnGround();
  }
}
