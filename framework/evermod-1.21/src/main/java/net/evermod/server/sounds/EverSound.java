package net.evermod.server.sounds;

import net.evermod.network.ChannelManager;
import net.evermod.network.packets.PlaySoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

public class EverSound {

  /**
   * 🔊 Reproducir.
   */
  public static void playToAll(Entity source, SoundEvent sound) {
    send("play", source.getId(), sound, 1.0F, 1.0F, false, null);
  }

  public static void playToAll(Entity source, SoundEvent sound, float volume, float pitch) {
    send("play", source.getId(), sound, volume, pitch, false, null);
  }

  public static void playToAll(Entity source, SoundEvent sound, float volume, float pitch,
      boolean looping) {
    send("play", source.getId(), sound, volume, pitch, looping, null);
  }

  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound) {
    send("play", source.getId(), sound, 1.0F, 1.0F, false, player);
  }

  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound, float volume,
      float pitch) {
    send("play", source.getId(), sound, volume, pitch, false, player);
  }

  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound, float volume,
      float pitch, boolean looping) {
    send("play", source.getId(), sound, volume, pitch, looping, player);
  }

  /**
   * 🎚️ Actualizar.
   */
  public static void updateVolumeToAll(Entity source, SoundEvent sound, float volume) {
    send("update", source.getId(), sound, volume, 1.0F, false, null);
  }

  public static void updatePitchToAll(Entity source, SoundEvent sound, float pitch) {
    send("update", source.getId(), sound, 1.0F, pitch, false, null);
  }

  public static void updateToAll(Entity source, SoundEvent sound, float volume, float pitch) {
    send("update", source.getId(), sound, volume, pitch, false, null);
  }

  public static void updateVolumeTo(ServerPlayer player, Entity source, SoundEvent sound,
      float volume) {
    send("update", source.getId(), sound, volume, 1.0F, false, player);
  }

  public static void updatePitchTo(ServerPlayer player, Entity source, SoundEvent sound,
      float pitch) {
    send("update", source.getId(), sound, 1.0F, pitch, false, player);
  }

  public static void updateTo(ServerPlayer player, Entity source, SoundEvent sound, float volume,
      float pitch) {
    send("update", source.getId(), sound, volume, pitch, false, player);
  }

  /**
   * 🛑 Detener uno o todos.
   */
  public static void stopToAll(Entity source, SoundEvent sound) {
    send("stop", source.getId(), sound, 0, 0, false, null);
  }

  public static void stopTo(ServerPlayer player, Entity source, SoundEvent sound) {
    send("stop", source.getId(), sound, 0, 0, false, player);
  }

  public static void stopAll() {
    send("stop_all", 0, null, 0, 0, false, null);
  }

  // --- Interno ---
  private static void send(String state, int sourceId, SoundEvent sound, float volume, float pitch,
      boolean looping, ServerPlayer player) {
    if (sound == null && !"stop_all".equals(state)) {
      return;
    }

    PlaySoundPacket packet = new PlaySoundPacket(sourceId, sound, volume, pitch, state, looping);
    if (player != null) {
      ChannelManager.sendToClient(packet, player);
    } else {
      ChannelManager.sendToAllClients(packet);
    }
  }
}
