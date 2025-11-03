package net.evermod.client.handlers;

import net.evermod.client.sounds.SoundController;
import net.evermod.common.network.packets.PlaySoundPacket;
import net.evermod.common.resources.EverLocation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.registries.ForgeRegistries;

public class ClientSoundHandler {

  public static void handle(PlaySoundPacket msg) {
    ClientLevel level = Minecraft.getInstance().level;
    if (level == null) {
      return;
    }

    String state = msg.getState();
    if (state == null) {
      return;
    }

    if (state.equals("stop_all")) {
      SoundController.stopAll();
      return;
    }

    Entity entity = level.getEntity(msg.getEntityId());
    if (entity == null) {
      return;
    }

    String soundLocation = msg.getSoundLocation();
    if (soundLocation == null) {
      return;
    }

    SoundEvent sound = ForgeRegistries.SOUND_EVENTS.getValue(EverLocation.parse(soundLocation));
    if (sound == null) {
      return;
    }

    switch (state) {
      case "play" -> SoundController.play(entity, sound, SoundSource.HOSTILE, msg.getVolume(),
          msg.getPitch(), msg.isLooping());
      case "update" -> SoundController.setVolume(entity, sound, msg.getVolume(), msg.getPitch());
      case "stop" -> SoundController.stop(entity, sound);
      default -> {
      }
    }
  }
}
