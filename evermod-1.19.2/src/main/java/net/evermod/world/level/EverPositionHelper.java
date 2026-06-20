package net.evermod.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class EverPositionHelper {

  public static boolean isValidStandingPos(Level level, BlockPos pos) {
    return isValidStandingPos(level, pos, false);
  }

  /**
   * Evalúa si una posición es completamente segura para que una entidad terrestre se posicione en
   * ella.
   */
  public static boolean isValidStandingPos(Level level, BlockPos pos, boolean excludeIce) {
    if (level == null || pos == null)
      return false;

    BlockPos below = pos.below();
    BlockState belowState = level.getBlockState(below);
    BlockState posState = level.getBlockState(pos);
    BlockState aboveState = level.getBlockState(pos.above());

    if (belowState.is(Blocks.WATER) || belowState.is(Blocks.LAVA)) {
      return false;
    }

    if (excludeIce && (belowState.is(Blocks.ICE) || belowState.is(Blocks.PACKED_ICE)
        || belowState.is(Blocks.BLUE_ICE))) {
      return false;
    }

    boolean hasHeadroom = posState.isAir() && aboveState.isAir();
    boolean solidBelow = belowState.isSolidRender(level, below)
        || belowState.isCollisionShapeFullBlock(level, below);

    return hasHeadroom && solidBelow;
  }

  /**
   * Determina si la entidad se encuentra actualmente en un entorno de cueva (subterráneo y oscuro).
   */
  public static boolean isInCave(Level level, Entity entity) {
    if (level == null || entity == null)
      return false;

    BlockPos pos = entity.blockPosition();
    int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ());

    boolean underSurface = entity.getY() < surfaceY - 6;
    boolean darkSky = level.getBrightness(LightLayer.SKY, pos) <= 1;

    return underSurface && darkSky;
  }

  /**
   * Busca una posición segura en la dirección hacia donde mira una entidad (ej. un Jugador).
   */
  public static BlockPos getSafePosInFrontOfTarget(Entity target, LivingEntity directionSource,
      double distance) {
    Vec3 look = directionSource.getViewVector(1.0F).normalize();

    double x = target.getX() + look.x * distance;
    double y = target.getY();
    double z = target.getZ() + look.z * distance;

    Level level = EverLevel.get(target);
    BlockPos base = new BlockPos((int) x, (int) y, (int) z);
    BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

    for (int dy = 8; dy >= -8; dy--) {
      mutablePos.set(base.getX(), base.getY() + dy, base.getZ());
      if (isValidStandingPos(level, mutablePos, false)) {
        return mutablePos.immutable();
      }
    }

    int heightmapY =
        level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, base.getX(), base.getZ());
    return new BlockPos(base.getX(), heightmapY, base.getZ());
  }

  /**
   * Busca un punto aleatorio seguro alrededor de cualquier entidad.
   */
  @Nullable
  public static BlockPos getRandomSafePosAround(Entity entity, int minDistance, int maxDistance,
      int maxAttempts, boolean excludeIce) {
    Level level = EverLevel.get(entity);

    for (int attempt = 0; attempt < maxAttempts; attempt++) {
      double angle = level.getRandom().nextDouble() * 2 * Math.PI;
      double distance = level.getRandom().nextDouble() * (maxDistance - minDistance) + minDistance;

      int x = (int) (entity.getX() + Math.cos(angle) * distance);
      int z = (int) (entity.getZ() + Math.sin(angle) * distance);
      int y = (int) entity.getY();

      if (isInCave(level, entity)) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dy = -5; dy <= 5; dy++) {
          pos.set(x, y + dy, z);
          if (isValidStandingPos(level, pos, excludeIce)) {
            return pos.immutable();
          }
        }
      } else {
        y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
        BlockPos pos = new BlockPos(x, y, z);
        if (isValidStandingPos(level, pos, excludeIce)) {
          return pos.immutable();
        }
      }
    }
    return null;
  }

  /**
   * Comprueba de manera segura si un Mob puede llegar físicamente a un bloque mediante Pathfinding.
   */
  public static boolean isPathReachable(Mob mob, BlockPos pos) {
    if (mob == null || pos == null || mob.getNavigation() == null)
      return false;

    if (!mob.getNavigation().isStableDestination(pos)) {
      return false;
    }

    Path path = mob.getNavigation().createPath(pos, 1);
    return path != null && path.canReach();
  }
}
