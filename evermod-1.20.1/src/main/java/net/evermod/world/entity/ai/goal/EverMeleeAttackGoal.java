package net.evermod.world.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class EverMeleeAttackGoal extends MeleeAttackGoal {

  public EverMeleeAttackGoal(PathfinderMob mob, double speedModifier,
      boolean followingTargetEvenIfNotSeen) {
    super(mob, speedModifier, followingTargetEvenIfNotSeen);
  }

  @Override
  protected void checkAndPerformAttack(LivingEntity enemy, double distortionToEnemySqr) {
    double attackReach = this.getAttackReachSqr(enemy);
    if (distortionToEnemySqr <= attackReach && this.isTimeToAttack()) {
      this.onAttackPerform(enemy);
    }
    super.checkAndPerformAttack(enemy, distortionToEnemySqr);
  }

  protected void onAttackPerform(LivingEntity enemy) {}
}
