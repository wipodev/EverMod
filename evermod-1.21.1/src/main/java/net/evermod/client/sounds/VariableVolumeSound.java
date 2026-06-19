package net.evermod.client.sounds;

import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public class VariableVolumeSound extends EntityBoundSoundInstance {
  private boolean isStopped = false;
  private final boolean looping;
  private final Entity boundEntity;

  // Variables para la transición progresiva en el cliente
  private float currentVolume;
  private float currentPitch;
  private float targetVolume;
  private float targetPitch;
  private int transitionDuration = 0;
  private int transitionTimer = 0;

  private float startVolume;
  private float startPitch;

  public VariableVolumeSound(SoundEvent sound, SoundSource category, float volume, float pitch,
      Entity entity, boolean looping) {
    super(sound, category, volume, pitch, entity, 0);
    this.boundEntity = entity;
    this.looping = looping;
    this.currentVolume = volume;
    this.currentPitch = pitch;
    this.targetVolume = volume;
    this.targetPitch = pitch;
  }

  @Override
  public void tick() {
    // Si la entidad muere o desaparece, el sonido se detiene
    if (this.boundEntity == null || this.boundEntity.isRemoved()) {
      this.stopSound();
      return;
    }

    // Manejo de transiciones progresivas en el cliente
    if (this.transitionTimer < this.transitionDuration) {
      this.transitionTimer++;
      float progress = (float) this.transitionTimer / this.transitionDuration;

      // Interpolación lineal
      this.currentVolume = this.startVolume + (this.targetVolume - this.startVolume) * progress;
      this.currentPitch = this.startPitch + (this.targetPitch - this.startPitch) * progress;
    }
  }

  // Configurar una nueva transición desde el paquete
  public void setupTransition(float targetVolume, float targetPitch, int ticks) {
    this.startVolume = this.currentVolume;
    this.startPitch = this.currentPitch;
    this.targetVolume = targetVolume;
    this.targetPitch = targetPitch;
    this.transitionDuration = ticks;
    this.transitionTimer = 0;
  }

  public void stopSound() {
    this.isStopped = true;
  }

  @Override
  public boolean isStopped() {
    return this.isStopped;
  }

  @Override
  public float getVolume() {
    return this.currentVolume;
  }

  @Override
  public float getPitch() {
    return this.currentPitch;
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
