package net.evermod.client.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SoundController {

  private static final Map<UUID, Map<SoundEvent, VariableVolumeSound>> activeSounds =
      new HashMap<>();

  public static void play(Entity entity, SoundEvent sound, float volume, float pitch) {
    stop(entity, sound);
    play(entity, sound, SoundSource.HOSTILE, volume, pitch, false);
  }

  public static void play(Entity entity, SoundEvent sound, SoundSource category, float volume,
      float pitch) {
    play(entity, sound, category, volume, pitch, true);
  }

  public static void play(Entity entity, SoundEvent sound, SoundSource category, float volume,
      float pitch, boolean looping) {
    UUID id = entity.getUUID();
    Map<SoundEvent, VariableVolumeSound> soundMap =
        activeSounds.computeIfAbsent(id, k -> new HashMap<>());

    if (soundMap.containsKey(sound)) {
      return;
    }
    VariableVolumeSound instance =
        new VariableVolumeSound(sound, category, volume, pitch, entity, looping);
    soundMap.put(sound, instance);
    Minecraft.getInstance().getSoundManager().play(instance);
  }

  public static void stop(Entity entity, SoundEvent sound) {
    Map<SoundEvent, VariableVolumeSound> soundMap = activeSounds.get(entity.getUUID());
    if (soundMap == null) {
      return;
    }

    SoundInstance instance = soundMap.remove(sound);
    if (instance != null) {
      Minecraft.getInstance().getSoundManager().stop(instance);
    }

    if (soundMap.isEmpty()) {
      activeSounds.remove(entity.getUUID());
    }
  }

  public static void stopAll() {
    for (Map<SoundEvent, VariableVolumeSound> soundMap : activeSounds.values()) {
      for (SoundInstance instance : soundMap.values()) {
        Minecraft.getInstance().getSoundManager().stop(instance);
      }
    }

    activeSounds.clear();
  }

  public static boolean isPlaying(Entity entity, SoundEvent sound) {
    Map<SoundEvent, VariableVolumeSound> soundMap = activeSounds.get(entity.getUUID());
    return soundMap != null && soundMap.containsKey(sound);
  }

  public static void setVolume(Entity entity, SoundEvent sound, float volume, float pitch) {
    Map<SoundEvent, VariableVolumeSound> soundMap = activeSounds.get(entity.getUUID());
    if (soundMap == null) {
      return;
    }
    VariableVolumeSound instance = soundMap.get(sound);
    if (instance != null) {
      instance.setVolume(volume);
      instance.setPitch(pitch);
    }
  }

  public static void clear() {
    activeSounds.clear();
  }
}
