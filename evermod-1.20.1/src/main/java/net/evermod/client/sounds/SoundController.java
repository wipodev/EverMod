package net.evermod.client.sounds;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class SoundController {

  private static final Map<UUID, Map<SoundEvent, VariableVolumeSound>> activeSounds =
      new HashMap<>();

  public static void play(Entity entity, SoundEvent sound, SoundSource category, float volume,
      float pitch, boolean looping) {
    // SOLUCIÓN PUNTO 1: Limpieza preventiva de sonidos viejos antes de verificar si existe
    cleanDeadSoundsForEntity(entity.getUUID());

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

  // SOLUCIÓN PUNTO 4: Modificar progresivamente aplicando la orden recibida
  public static void transitionSound(Entity entity, SoundEvent sound, float targetVolume,
      float targetPitch, int ticks) {
    cleanDeadSoundsForEntity(entity.getUUID());

    Map<SoundEvent, VariableVolumeSound> soundMap = activeSounds.get(entity.getUUID());
    if (soundMap != null) {
      VariableVolumeSound instance = soundMap.get(sound);
      if (instance != null) {
        instance.setupTransition(targetVolume, targetPitch, ticks);
      }
    }
  }

  // SOLUCIÓN PUNTO 3: Detener un audio específico
  public static void stop(Entity entity, SoundEvent sound) {
    Map<SoundEvent, VariableVolumeSound> soundMap = activeSounds.get(entity.getUUID());
    if (soundMap == null)
      return;

    VariableVolumeSound instance = soundMap.remove(sound);
    if (instance != null) {
      instance.stopSound(); // Le avisa al sonido que se detenga
      Minecraft.getInstance().getSoundManager().stop(instance);
    }

    if (soundMap.isEmpty()) {
      activeSounds.remove(entity.getUUID());
    }
  }

  // SOLUCIÓN PUNTO 3: Detener TODOS los audios del sistema
  public static void stopAll() {
    for (Map<SoundEvent, VariableVolumeSound> soundMap : activeSounds.values()) {
      for (VariableVolumeSound instance : soundMap.values()) {
        if (instance != null) {
          instance.stopSound();
          Minecraft.getInstance().getSoundManager().stop(instance);
        }
      }
    }
    activeSounds.clear();
  }

  // SOLUCIÓN PUNTO 1 y 5: Limpiador interno de registros basura (Audios terminados o entidades
  // muertas)
  private static void cleanDeadSoundsForEntity(UUID entityId) {
    Map<SoundEvent, VariableVolumeSound> soundMap = activeSounds.get(entityId);
    if (soundMap == null)
      return;

    Iterator<Map.Entry<SoundEvent, VariableVolumeSound>> iterator = soundMap.entrySet().iterator();
    while (iterator.hasNext()) {
      Map.Entry<SoundEvent, VariableVolumeSound> entry = iterator.next();
      VariableVolumeSound soundInstance = entry.getValue();

      // Si el gestor de Minecraft ya no lo está reproduciendo (porque era fijo y terminó, o se
      // detuvo)
      if (soundInstance == null
          || !Minecraft.getInstance().getSoundManager().isActive(soundInstance)
          || soundInstance.isStopped()) {
        iterator.remove();
      }
    }

    if (soundMap.isEmpty()) {
      activeSounds.remove(entityId);
    }
  }
}
