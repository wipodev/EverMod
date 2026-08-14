package net.evermod.world.entity.ai.goal;

import java.util.EnumSet;
import net.evermod.world.entity.EverEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * AI goal that forces an entity to navigate away from flowing water currents toward dry land or solid blocks.
 */
public class EscapeWaterCurrentGoal extends Goal {

  private final EverEntity entity;
  private final double speed;

  /**
   * Constructs an EscapeWaterCurrentGoal instance.
   *
   * @param entity The target entity wrapped in EverEntity.
   * @param speed The movement speed modifier when escaping.
   */
  public EscapeWaterCurrentGoal(EverEntity entity, double speed) {
    this.entity = entity;
    this.speed = speed;
    this.setFlags(EnumSet.of(Goal.Flag.MOVE));
  }

  @Override
  public boolean canUse() {
    return this.entity.isInWater() && isInFlowingWater();
  }

  @Override
  public void tick() {
    if (this.entity.isInWater()) {
      Vec3 escapeDirection = findEscapeDirection();
      if (escapeDirection != null) {
        this.entity.getNavigation().moveTo(
            escapeDirection.x, escapeDirection.y, escapeDirection.z, this.speed);

        // Jump if target escape vector is higher than current Y level
        if (escapeDirection.y > this.entity.getY() + 0.1) {
          this.entity.getJumpControl().jump();
        }
      }
    }
  }

  /**
   * Checks whether the entity is standing in flowing water rather than a source block.
   *
   * @return True if in flowing water, false otherwise.
   */
  private boolean isInFlowingWater() {
    BlockPos pos = this.entity.blockPosition();
    BlockState blockState = this.entity.everLevel().getBlockState(pos);
    return blockState.getBlock() == Blocks.WATER && blockState.getFluidState().getAmount() < 8;
  }

  /**
   * Searches horizontal adjacent positions to find a non-water block to navigate towards.
   *
   * @return A Vec3 targeting the escape block position, or null if no adjacent land/solid block is found.
   */
  private Vec3 findEscapeDirection() {
    BlockPos entityPos = this.entity.blockPosition();

    for (Direction direction : Direction.Plane.HORIZONTAL) {
      BlockPos checkPos = entityPos.relative(direction);
      BlockState state = this.entity.everLevel().getBlockState(checkPos);

      if (!state.getBlock().defaultBlockState().is(Blocks.WATER)) {
        return new Vec3(checkPos.getX(), checkPos.getY(), checkPos.getZ());
      }
    }
    return null;
  }
}
