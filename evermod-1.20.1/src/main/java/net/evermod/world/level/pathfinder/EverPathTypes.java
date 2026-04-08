package net.evermod.world.level.pathfinder;

import net.minecraft.world.level.pathfinder.BlockPathTypes;

public enum EverPathTypes {
  WATER(BlockPathTypes.WATER), LAVA(BlockPathTypes.LAVA), FIRE(BlockPathTypes.DANGER_FIRE), OPEN(
      BlockPathTypes.OPEN), WALKABLE(BlockPathTypes.WALKABLE);

  private final BlockPathTypes forgeType;

  EverPathTypes(BlockPathTypes forgeType) {
    this.forgeType = forgeType;
  }

  public BlockPathTypes get() {
    return this.forgeType;
  }
}
