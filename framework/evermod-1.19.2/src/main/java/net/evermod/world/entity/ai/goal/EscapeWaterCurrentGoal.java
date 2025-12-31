package net.evermod.world.entity.ai.goal;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class EscapeWaterCurrentGoal extends Goal {
  private final PathfinderMob entity;
  private final double speed;

  public EscapeWaterCurrentGoal(PathfinderMob entity, double speed) {
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
        this.entity.getNavigation().moveTo(escapeDirection.x, escapeDirection.y, escapeDirection.z,
            speed);

        if (escapeDirection.y > this.entity.getY() + 0.1) {
          this.entity.getJumpControl().jump();
        }
      }
    }
  }

  private boolean isInFlowingWater() {
    BlockPos pos = this.entity.blockPosition();
    BlockState blockState = this.entity.level.getBlockState(pos);
    return blockState.getBlock() == Blocks.WATER && blockState.getFluidState().getAmount() < 8;
  }

  private Vec3 findEscapeDirection() {
    BlockPos entityPos = this.entity.blockPosition();

    for (Direction direction : Direction.Plane.HORIZONTAL) {
      BlockPos checkPos = entityPos.relative(direction);
      BlockState state = this.entity.level.getBlockState(checkPos);

      if (!state.getBlock().defaultBlockState().is(Blocks.WATER)) {
        return new Vec3(checkPos.getX(), checkPos.getY(), checkPos.getZ());
      }
    }
    return null;
  }
}
