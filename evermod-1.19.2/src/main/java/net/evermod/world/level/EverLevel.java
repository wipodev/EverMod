package net.evermod.world.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class EverLevel {

  public static Level get(Entity entity) {
    return entity.level;
  }

  public static ServerLevel asServer(Entity entity) {
    return entity.level instanceof ServerLevel ? (ServerLevel) entity.level : null;
  }

  // Usamos Object o un chequeo de Dist para evitar que el Server cargue ClientLevel
  public static Level asClient(Entity entity) {
    if (FMLEnvironment.dist == Dist.CLIENT) {
      return entity.level.isClientSide ? entity.level : null;
    }
    return null;
  }
}
