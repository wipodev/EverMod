package net.evermod.server.sounds;

import net.evermod.network.ChannelManager;
import net.evermod.network.packets.PlaySoundPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;

/**
 * Server-side audio manager utility for playing, transitioning, and stopping sounds 
 * across clients via custom network packets.
 * 
 * @author Wipodev
 */
public class EverSound {

  // --- PLAY SOUNDS (To all clients) ---

  /**
   * Plays a sound event attached to an entity for all connected clients with default volume and pitch.
   *
   * @param source The source entity emitting the sound.
   * @param sound The sound event to play.
   */
  public static void playToAll(Entity source, SoundEvent sound) {
    send("play", source.getId(), sound, 1.0F, 1.0F, 1.0F, 1.0F, 0, false, null);
  }

  /**
   * Plays a sound event attached to an entity for all connected clients with custom volume and pitch.
   *
   * @param source The source entity emitting the sound.
   * @param sound The sound event to play.
   * @param volume The audio volume level.
   * @param pitch The audio pitch multiplier.
   */
  public static void playToAll(Entity source, SoundEvent sound, float volume, float pitch) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, false, null);
  }

  /**
   * Plays a sound event attached to an entity for all connected clients with full playback control.
   *
   * @param source The source entity emitting the sound.
   * @param sound The sound event to play.
   * @param volume The audio volume level.
   * @param pitch The audio pitch multiplier.
   * @param looping Whether the sound should repeat continuously.
   */
  public static void playToAll(Entity source, SoundEvent sound, float volume, float pitch,
      boolean looping) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, looping, null);
  }

  // --- PLAY SOUNDS (To a specific player) ---

  /**
   * Plays a sound event attached to an entity for a specific player with default volume and pitch.
   *
   * @param player The target player receiving the sound packet.
   * @param source The source entity emitting the sound.
   * @param sound The sound event to play.
   */
  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound) {
    send("play", source.getId(), sound, 1.0F, 1.0F, 1.0F, 1.0F, 0, false, player);
  }

  /**
   * Plays a sound event attached to an entity for a specific player with custom volume and pitch.
   *
   * @param player The target player receiving the sound packet.
   * @param source The source entity emitting the sound.
   * @param sound The sound event to play.
   * @param volume The audio volume level.
   * @param pitch The audio pitch multiplier.
   */
  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound, float volume,
      float pitch) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, false, player);
  }

  /**
   * Plays a sound event attached to an entity for a specific player with full playback control.
   *
   * @param player The target player receiving the sound packet.
   * @param source The source entity emitting the sound.
   * @param sound The sound event to play.
   * @param volume The audio volume level.
   * @param pitch The audio pitch multiplier.
   * @param looping Whether the sound should repeat continuously.
   */
  public static void playTo(ServerPlayer player, Entity source, SoundEvent sound, float volume,
      float pitch, boolean looping) {
    send("play", source.getId(), sound, volume, pitch, volume, pitch, 0, looping, player);
  }

  // --- PROGRESSIVE AUDIO TRANSITIONS (To all clients) ---

  /**
   * Smoothly transitions both volume and pitch over time for all connected clients.
   *
   * @param source The source entity emitting the sound.
   * @param sound The sound event to transition.
   * @param fromVolume Starting volume level.
   * @param toVolume Target volume level.
   * @param fromPitch Starting pitch multiplier.
   * @param toPitch Target pitch multiplier.
   * @param lifetimeTicks Duration of the transition in game ticks.
   * @param loop Whether the sound loops during and after the transition.
   */
  public static void transitionToAll(Entity source, SoundEvent sound, float fromVolume,
      float toVolume, float fromPitch, float toPitch, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, fromVolume, fromPitch, toVolume, toPitch,
        lifetimeTicks, loop, null);
  }

  /**
   * Smoothly transitions volume over time while keeping pitch at default (1.0F) for all clients.
   *
   * @param source The source entity emitting the sound.
   * @param sound The sound event to transition.
   * @param fromVolume Starting volume level.
   * @param toVolume Target volume level.
   * @param lifetimeTicks Duration of the transition in game ticks.
   * @param loop Whether the sound loops during and after the transition.
   */
  public static void transitionVolumeToAll(Entity source, SoundEvent sound, float fromVolume,
      float toVolume, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, fromVolume, 1.0F, toVolume, 1.0F, lifetimeTicks, loop,
        null);
  }

  /**
   * Smoothly transitions pitch over time while keeping volume at default (1.0F) for all clients.
   *
   * @param source The source entity emitting the sound.
   * @param sound The sound event to transition.
   * @param fromPitch Starting pitch multiplier.
   * @param toPitch Target pitch multiplier.
   * @param lifetimeTicks Duration of the transition in game ticks.
   * @param loop Whether the sound loops during and after the transition.
   */
  public static void transitionPitchToAll(Entity source, SoundEvent sound, float fromPitch,
      float toPitch, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, 1.0F, fromPitch, 1.0F, toPitch, lifetimeTicks, loop,
        null);
  }

  // --- PROGRESSIVE AUDIO TRANSITIONS (To a specific player) ---

  /**
   * Smoothly transitions both volume and pitch over time for a specific player.
   *
   * @param player The target player receiving the sound packet.
   * @param source The source entity emitting the sound.
   * @param sound The sound event to transition.
   * @param fromVolume Starting volume level.
   * @param toVolume Target volume level.
   * @param fromPitch Starting pitch multiplier.
   * @param toPitch Target pitch multiplier.
   * @param lifetimeTicks Duration of the transition in game ticks.
   * @param loop Whether the sound loops during and after the transition.
   */
  public static void transitionTo(ServerPlayer player, Entity source, SoundEvent sound,
      float fromVolume, float toVolume, float fromPitch, float toPitch, int lifetimeTicks,
      boolean loop) {
    send("transition", source.getId(), sound, fromVolume, fromPitch, toVolume, toPitch,
        lifetimeTicks, loop, player);
  }

  /**
   * Smoothly transitions volume over time while keeping pitch at default (1.0F) for a specific player.
   *
   * @param player The target player receiving the sound packet.
   * @param source The source entity emitting the sound.
   * @param sound The sound event to transition.
   * @param fromVolume Starting volume level.
   * @param toVolume Target volume level.
   * @param lifetimeTicks Duration of the transition in game ticks.
   * @param loop Whether the sound loops during and after the transition.
   */
  public static void transitionVolumeTo(ServerPlayer player, Entity source, SoundEvent sound,
      float fromVolume, float toVolume, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, fromVolume, 1.0F, toVolume, 1.0F, lifetimeTicks, loop,
        player);
  }

  /**
   * Smoothly transitions pitch over time while keeping volume at default (1.0F) for a specific player.
   *
   * @param player The target player receiving the sound packet.
   * @param source The source entity emitting the sound.
   * @param sound The sound event to transition.
   * @param fromPitch Starting pitch multiplier.
   * @param toPitch Target pitch multiplier.
   * @param lifetimeTicks Duration of the transition in game ticks.
   * @param loop Whether the sound loops during and after the transition.
   */
  public static void transitionPitchTo(ServerPlayer player, Entity source, SoundEvent sound,
      float fromPitch, float toPitch, int lifetimeTicks, boolean loop) {
    send("transition", source.getId(), sound, 1.0F, fromPitch, 1.0F, toPitch, lifetimeTicks, loop,
        player);
  }

  // --- STOP SOUNDS ---

  /**
   * Stops a specific sound event attached to an entity for all clients.
   *
   * @param source The source entity emitting the sound.
   * @param sound The sound event to stop.
   */
  public static void stopToAll(Entity source, SoundEvent sound) {
    send("stop", source.getId(), sound, 0, 0, 0, 0, 0, false, null);
  }

  /**
   * Stops a specific sound event attached to an entity for a specific player.
   *
   * @param player The target player receiving the stop packet.
   * @param source The source entity emitting the sound.
   * @param sound The sound event to stop.
   */
  public static void stopTo(ServerPlayer player, Entity source, SoundEvent sound) {
    send("stop", source.getId(), sound, 0, 0, 0, 0, 0, false, player);
  }

  /**
   * Stops all active custom sounds across all clients.
   */
  public static void stopAll() {
    send("stop_all", 0, null, 0, 0, 0, 0, 0, false, null);
  }

  // --- Master Internal Network Dispatcher ---

  /**
   * Constructs and dispatches a PlaySoundPacket through ChannelManager.
   */
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
