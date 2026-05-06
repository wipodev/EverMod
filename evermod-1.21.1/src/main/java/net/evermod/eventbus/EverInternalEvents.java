package net.evermod.eventbus;

import net.evermod.world.entity.EverEntity;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EverInternalEvents {

  @SubscribeEvent
  public static void onMobFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
    if (event.getEntity() instanceof EverEntity everEntity) {
      everEntity.everFinalizeSpawn(event.getLevel(), event.getDifficulty(), event.getSpawnType(),
          event.getSpawnData(), event.getSpawnTag());
    }
  }
}
