package net.evermod.common.network.packets;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.evermod.client.handlers.ClientSoundHandler;
import net.evermod.common.network.io.EverBuffer;
import net.evermod.common.network.io.EverContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class PlaySoundPacket extends PacketBase<PlaySoundPacket> {

  protected int entityId;
  protected @Nonnull String soundLocation;
  protected float volume;
  protected float pitch;
  protected @Nonnull String state;
  protected boolean looping;

  public PlaySoundPacket() {
    this.entityId = 0;
    this.soundLocation = "evermod:unknown_sound";
    this.volume = 1.0F;
    this.pitch = 1.0F;
    this.state = "play";
    this.looping = false;
  }

  public PlaySoundPacket(int entityId, SoundEvent sound, float volume, float pitch, String state,
      boolean looping) {
    this(entityId,
        ForgeRegistries.SOUND_EVENTS.getKey(sound) != null
            ? ForgeRegistries.SOUND_EVENTS.getKey(sound).toString()
            : "evermod:unknown_sound",
        volume, pitch, state, looping);
  }

  public PlaySoundPacket(int entityId, String soundLocation, float volume, float pitch,
      String state, boolean looping) {
    if (Objects.isNull(soundLocation) || soundLocation.isBlank()) {
      throw new IllegalArgumentException("soundLocation no puede ser nulo o vacío");
    }
    if (Objects.isNull(state) || state.isBlank()) {
      throw new IllegalArgumentException("state no puede ser nulo o vacío");
    }
    this.entityId = entityId;
    this.soundLocation = soundLocation;
    this.volume = volume;
    this.pitch = pitch;
    this.state = state;
    this.looping = looping;
  }

  @Override
  public void encode(EverBuffer buffer) {
    buffer.writeInt(this.entityId);
    buffer.writeUtf(this.soundLocation);
    buffer.writeInt(Float.floatToIntBits(this.volume));
    buffer.writeInt(Float.floatToIntBits(this.pitch));
    buffer.writeUtf(this.state);
    buffer.writeInt(this.looping ? 1 : 0);
  }

  @Override
  public PlaySoundPacket decode(EverBuffer buffer) {
    this.entityId = buffer.readInt();
    String readSoundLocation = buffer.readUtf();
    this.soundLocation = readSoundLocation != null ? readSoundLocation : "evermod:unknown_sound";
    this.volume = Float.intBitsToFloat(buffer.readInt());
    this.pitch = Float.intBitsToFloat(buffer.readInt());
    String readState = buffer.readUtf();
    this.state = readState != null ? readState : "play";
    this.looping = buffer.readInt() == 1;
    return this;
  }

  @Override
  public void handle(EverContext context) {
    context.runClient(() -> ClientSoundHandler.handle(this));
  }

  public int getEntityId() {
    return this.entityId;
  }

  public String getSoundLocation() {
    return this.soundLocation;
  }

  public float getVolume() {
    return this.volume;
  }

  public float getPitch() {
    return this.pitch;
  }

  public String getState() {
    return this.state;
  }

  public boolean isLooping() {
    return this.looping;
  }
}
