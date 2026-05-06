package net.evermod.world.level;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import net.evermod.core.EverBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class EverPlayerTargeting {

  public static Player findValidTarget(Mob mob, double searchRange, boolean alwaysFollow) {
    return findValidTarget(EverLevel.get(mob), mob.position(), searchRange, alwaysFollow, false);
  }

  public static Player findValidTarget(Mob mob, double searchRange, boolean alwaysFollow,
      boolean randomize) {
    return findValidTarget(EverLevel.get(mob), mob.position(), searchRange, alwaysFollow,
        randomize);
  }

  public static Player findValidTarget(Level level) {
    return findValidTarget(level, new Vec3(0, 64, 0), -1, false, true);
  }

  public static List<Player> findValidTargets(Mob mob, double searchRange) {
    List<Player> players = EverLevel.get(mob).getEntitiesOfClass(Player.class,
        new AABB(mob.position(), mob.position()).inflate(searchRange), Player::isAlive);

    return players.stream().filter(p -> isValidTarget(mob, p, searchRange)).toList();
  }

  public static Player findValidTarget(Level level, Vec3 origin, double searchRange,
      boolean alwaysFollow, boolean randomize) {
    List<Player> players = level.getEntitiesOfClass(Player.class,
        new AABB(origin, origin).inflate(searchRange), Player::isAlive);

    List<Player> validPlayers = players.stream()
        .filter(p -> isValidTarget(origin, p, searchRange, false, alwaysFollow, false)).toList();

    if (validPlayers.isEmpty()) {
      return null;
    }

    if (randomize) {
      return validPlayers.get(new Random().nextInt(validPlayers.size()));
    }

    return validPlayers.stream()
        .min(Comparator.comparingDouble(p -> p.position().distanceToSqr(origin))).orElse(null);
  }

  public static Vec3 getClosestPlayerOffsetPos(Mob mob, double range, boolean alwaysFollow,
      double offset) {
    return getClosestPlayerOffsetPos(EverLevel.get(mob), mob.position(), range, alwaysFollow,
        offset);
  }

  public static Vec3 getClosestPlayerOffsetPos(Level level, Vec3 origin, double range,
      boolean alwaysFollow, double offset) {
    Player closest = findValidTarget(level, origin, range, alwaysFollow, false);
    if (closest != null) {
      return closest.position().add(offset, 0, offset);
    }
    return origin;
  }

  public static boolean isValidTarget(Mob mob) {
    return isValidTarget(mob, -1);
  }

  public static boolean isValidTarget(Mob mob, double range) {
    return isValidTarget(mob, range, false);
  }

  public static boolean isValidTarget(Mob mob, double range, boolean invertRange) {
    return isValidTarget(mob, range, invertRange, false);
  }

  public static boolean isValidTarget(Mob mob, double range, boolean invertRange,
      boolean alwaysFollow) {
    return isValidTarget(mob, range, invertRange, alwaysFollow, false);
  }

  public static boolean isValidTarget(Mob mob, double range, boolean invertRange,
      boolean alwaysFollow, boolean inPlane) {
    if (mob.getTarget() instanceof Player player) {
      return isValidTarget(mob.position(), player, range, alwaysFollow, invertRange, inPlane);
    }
    return false;
  }

  public static boolean isValidTarget(Mob mob, Player player) {
    return isValidTarget(mob, player, -1);
  }

  public static boolean isValidTarget(Mob mob, Player player, double range) {
    return isValidTarget(mob, player, range, false);
  }

  public static boolean isValidTarget(Mob mob, Player player, double range, boolean invertRange) {
    return isValidTarget(mob, player, range, invertRange, false);
  }

  public static boolean isValidTarget(Mob mob, Player player, double range, boolean invertRange,
      boolean alwaysFollow) {
    return isValidTarget(mob, player, range, invertRange, alwaysFollow, false);
  }

  public static boolean isValidTarget(Mob mob, Player player, double range, boolean invertRange,
      boolean alwaysFollow, boolean inPlane) {
    return isValidTarget(mob.position(), player, range, alwaysFollow, invertRange, inPlane);
  }

  public static boolean isValidTarget(Vec3 origin, Player player, double range, boolean invertRange,
      boolean alwaysFollow, boolean inPlane) {
    if (player == null || !player.isAlive()) {
      return false;
    }
    if (!alwaysFollow && (player.isCreative() || player.isSpectator())) {
      return false;
    }
    if (range < 0) {
      return true;
    }
    return isValidRange(origin, player, range, invertRange, inPlane);
  }

  public static boolean isValidRange(Mob mob, double range) {
    return isValidRange(mob, range, false);
  }

  public static boolean isValidRange(Mob mob, double range, boolean invertRange) {
    return isValidRange(mob, range, invertRange, false);
  }

  public static boolean isValidRange(Mob mob, double range, boolean invertRange, boolean inPlane) {
    if (mob.getTarget() instanceof Player player) {
      return isValidRange(mob.position(), player, range, invertRange, inPlane);
    }
    return false;
  }

  public static boolean isValidRange(Vec3 origin, Player player, double range, boolean invertRange,
      boolean inPlane) {
    boolean inRange;
    if (inPlane) {
      double dx = player.getX() - origin.x;
      double dz = player.getZ() - origin.z;
      inRange = dx * dx + dz * dz <= range * range;
    } else {
      inRange = player.position().distanceToSqr(origin) <= range * range;
    }
    return invertRange ? !inRange : inRange;
  }

  public static void moveToPosition(Mob mob, Entity target, LivingEntity viewSource) {
    if (viewSource instanceof Player player) {
      moveToPosition(mob, target, player, 0.0);
    } else {
      mob.setPos(target.position());
    }
  }

  public static void moveToPosition(Mob mob, Player viewSource, double distance) {
    moveToPosition(mob, viewSource, viewSource, distance);
  }

  public static void moveToPosition(Mob mob, Entity target, Player viewSource, double distance) {
    Vec3 look = viewSource.getViewVector(1.0F).normalize();

    double x = target.getX() + look.x * distance;
    double y = target.getY();
    double z = target.getZ() + look.z * distance;

    BlockPos base = new EverBlockPos(x, y, z);
    BlockPos safePos =
        EverLevel.get(viewSource).getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, base);

    mob.setPos(safePos.getX() + 0.5, safePos.getY(), safePos.getZ() + 0.5);

    lookAtPlayer(mob, viewSource);
  }

  public static void lookAtPlayer(Mob mob, Player player) {
    if (player == null) {
      return;
    }
    Vec3 toTarget = player.getEyePosition(1.0F).subtract(mob.getEyePosition(1.0F));
    float yaw = (float) Math.toDegrees(Math.atan2(toTarget.z, toTarget.x)) - 90.0F;

    mob.setYRot(yaw);
    mob.setXRot(0.0F);
    mob.yRotO = yaw;
    mob.xRotO = 0.0F;
    mob.yHeadRot = yaw;
    mob.yBodyRot = yaw;
  }
}
