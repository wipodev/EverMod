package net.evermod.world.level.pathfinder;

import net.minecraft.world.level.pathfinder.PathType;

public enum EverPathTypes {
  WATER(PathType.WATER), LAVA(PathType.LAVA), FIRE(PathType.DANGER_FIRE), OPEN(
      PathType.OPEN), WALKABLE(PathType.WALKABLE);

  private final PathType forgeType;

  EverPathTypes(PathType forgeType) {
    this.forgeType = forgeType;
  }

  public PathType get() {
    return this.forgeType;
  }
}
