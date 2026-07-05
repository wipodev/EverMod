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
  protected void checkAndPerformAttack(LivingEntity enemy) {
    if (this.canPerformAttack(enemy)) {
      this.onAttackPerform(enemy);
    }
    super.checkAndPerformAttack(enemy);
  }

  protected void onAttackPerform(LivingEntity enemy) {}
}
