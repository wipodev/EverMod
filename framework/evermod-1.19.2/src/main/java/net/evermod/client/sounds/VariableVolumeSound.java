package net.evermod.client.sounds;

import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class VariableVolumeSound extends EntityBoundSoundInstance {
  private float volume;
  private float pitch;
  private boolean looping;

  public VariableVolumeSound(SoundEvent sound, SoundSource category, float volume, float pitch,
      Entity entity, boolean looping) {
    super(sound, category, volume, pitch, entity, 0);
    this.volume = volume;
    this.pitch = pitch;
    this.looping = looping;
  }

  public void setVolume(float volume) {
    this.volume = volume;
  }

  @Override
  public float getVolume() {
    return this.volume;
  }

  public void setPitch(float pitch) {
    this.pitch = pitch;
  }

  @Override
  public float getPitch() {
    return this.pitch;
  }

  @Override
  public boolean isLooping() {
    return this.looping;
  }

  @Override
  public Attenuation getAttenuation() {
    return Attenuation.LINEAR;
  }
}
