package net.evermod.world.level.pathfinder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.level.pathfinder.PathType;

public class EverPathEvaluator {

  /**
   * Evalúa estáticamente el tipo de camino (Path Type) de un bloque en una posición específica,
   * abstrayendo los cambios de firma entre versiones de Minecraft.
   */
  public static PathType getStaticType(Mob mob, BlockPos pos) {
    return WalkNodeEvaluator.getPathTypeStatic(mob, pos);
  }
}
