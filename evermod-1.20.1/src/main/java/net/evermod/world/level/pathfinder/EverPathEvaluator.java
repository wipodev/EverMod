package net.evermod.world.level.pathfinder;

import net.evermod.world.level.EverLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

public class EverPathEvaluator {

  /**
   * Evalúa estáticamente el tipo de camino (Path Type) de un bloque en una posición específica,
   * abstrayendo los cambios de firma entre versiones de Minecraft.
   */
  public static BlockPathTypes getStaticType(Mob mob, BlockPos pos) {
    ServerLevel level = EverLevel.asServer(mob);
    return WalkNodeEvaluator.getBlockPathTypeStatic(level, pos.mutable());
  }
}
