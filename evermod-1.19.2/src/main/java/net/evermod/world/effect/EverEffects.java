package net.evermod.world.effect;

import net.evermod.concurrent.EverScheduler;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

public class EverEffects {

  public static void playEating(Mob entity, ServerLevel level, ItemStack eatingItem, int repeat) {
    if (entity == null || !entity.isAlive() || eatingItem.isEmpty()) {
      return;
    }

    double frontDistance = 0.4;
    double sideDistance = 0.01;

    for (int delay = 4; delay <= repeat; delay += 2) {
      EverScheduler.queueServerWork(delay, () -> {
        if (!entity.isAlive()) {
          return;
        }

        double radians = Math.toRadians(entity.yBodyRot);
        double offsetX = -Math.sin(radians) * frontDistance + Math.cos(radians) * sideDistance;
        double offsetZ = Math.cos(radians) * frontDistance + Math.sin(radians) * sideDistance;

        double posX = entity.getX() + offsetX;
        double posY = entity.getY() + entity.getEyeHeight() * 0.8;
        double posZ = entity.getZ() + offsetZ;

        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, eatingItem), posX, posY,
            posZ, 5, 0.1, 0.1, 0.1, 0.05);

        level.playSound(null, posX, posY, posZ, SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1.0F,
            1.0F);
      });
    }
  }
}
