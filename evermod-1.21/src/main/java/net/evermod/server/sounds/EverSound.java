package net.evermod.server.sounds;

import net.evermod.network.ChannelManager;
import net.evermod.network.packets.PlaySoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

public class EverSound {

  /**
   * 🔊 REPRODUCIR (Hacia todos los clientes)
   */
  public static void playToAll(Entity source, SoundEvent sound) {
    send("play", source.getId(), sound, 1.0F, 1.0F, 1.0F, 1.0F, 0, false, null);
  }

  public static void playToAll(Entity source, SoundEvent sound, float volume, float pitch) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, false, null);
  }

  public static void playToAll(Entity source, SoundEvent sound, float volume, float pitch,
      boolean looping) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, looping, null);
  }

  /**
   * 🔊 REPRODUCIR (Hacia un jugador específico)
   */
  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound) {
    send("play", source.getId(), sound, 1.0F, 1.0F, 1.0F, 1.0F, 0, false, player);
  }

  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound, float volume,
      float pitch) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, false, player);
  }

  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound, float volume,
      float pitch, boolean looping) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, looping, player);
  }

  /**
   * 🎚️ TRANSICIONES PROGRESIVAS COMBINABLES (Hacia todos los clientes)
   */

  // Modificar Volumen y Pitch a la vez
  public static void transitionToAll(Entity source, SoundEvent sound, float fromVolume,
      float toVolume, float fromPitch, float toPitch, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, fromVolume, fromPitch, toVolume, toPitch,
        lifetimeTicks, loop, null);
  }

  // CORRECCIÓN: Nombre explícito para evitar conflicto de firma
  public static void transitionVolumeToAll(Entity source, SoundEvent sound, float fromVolume,
      float toVolume, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, fromVolume, 1.0F, toVolume, 1.0F, lifetimeTicks, loop,
        null);
  }

  // CORRECCIÓN: Nombre explícito para evitar conflicto de firma
  public static void transitionPitchToAll(Entity source, SoundEvent sound, float fromPitch,
      float toPitch, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, 1.0F, fromPitch, 1.0F, toPitch, lifetimeTicks, loop,
        null);
  }

  /**
   * 🎚️ TRANSICIONES PROGRESIVAS COMBINABLES (Hacia un jugador específico)
   */

  // Modificar Volumen y Pitch a la vez para un jugador
  public static void transitionTo(ServerPlayer player, Entity source, SoundEvent sound,
      float fromVolume, float toVolume, float fromPitch, float toPitch, int lifetimeTicks,
      boolean loop) {
    send("transition", source.getId(), sound, fromVolume, fromPitch, toVolume, toPitch,
        lifetimeTicks, loop, player);
  }

  // CORRECCIÓN: Nombre explícito para evitar conflicto de firma para un jugador
  public static void transitionVolumeTo(ServerPlayer player, Entity source, SoundEvent sound,
      float fromVolume, float toVolume, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, fromVolume, 1.0F, toVolume, 1.0F, lifetimeTicks, loop,
        player);
  }

  // CORRECCIÓN: Nombre explícito para evitar conflicto de firma para un jugador
  public static void transitionPitchTo(ServerPlayer player, Entity source, SoundEvent sound,
      float fromPitch, float toPitch, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, 1.0F, fromPitch, 1.0F, toPitch, lifetimeTicks, loop,
        player);
  }

  /**
   * 🛑 DETENER SONIDOS
   */
  public static void stopToAll(Entity source, SoundEvent sound) {
    send("stop", source.getId(), sound, 0, 0, 0, 0, 0, false, null);
  }

  public static void stopTo(ServerPlayer player, Entity source, SoundEvent sound) {
    send("stop", source.getId(), sound, 0, 0, 0, 0, 0, false, player);
  }

  public static void stopAll() {
    send("stop_all", 0, null, 0, 0, 0, 0, 0, false, null);
  }

  // --- Método de Envío Interno Maestro ---
  private static void send(String state, int sourceId, SoundEvent sound, float volume, float pitch,
      float targetVolume, float targetPitch, int transitionTicks, boolean looping,
      ServerPlayer player) {
    if (sound == null && !"stop_all".equals(state)) {
      return;
    }

    PlaySoundPacket packet = new PlaySoundPacket(sourceId, sound, volume, pitch, targetVolume,
        targetPitch, transitionTicks, state, looping);
    if (player != null) {
      ChannelManager.sendToClient(packet, player);
    } else {
      ChannelManager.sendToAllClients(packet);
    }
  }
}
