package net.evermod.world.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class EverLevel {

  public static Level get(Entity entity) {
    return entity.level();
  }

  public static ServerLevel asServer(Entity entity) {
    Level level = entity.level();
    return level instanceof ServerLevel ? (ServerLevel) level : null;
  }

  public static Level asClient(Entity entity) {
    if (FMLEnvironment.dist == Dist.CLIENT) {
      Level level = entity.level();
      return level.isClientSide ? level : null;
    }
    return null;
  }
}
