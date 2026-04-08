package net.evermod.network.packets;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.evermod.client.handlers.ClientSoundHandler;
import net.evermod.network.annotations.EverPacket;
import net.evermod.network.io.EverBuffer;
import net.evermod.network.io.EverContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

@EverPacket
public class PlaySoundPacket extends PacketBase {

  private final int entityId;
  private final @Nonnull String soundLocation;
  private final float volume;
  private final float pitch;
  private final @Nonnull String state;
  private final boolean looping;

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

  public PlaySoundPacket(int entityId, SoundEvent sound, float volume, float pitch, String state,
      boolean looping) {
    this(entityId,
        ForgeRegistries.SOUND_EVENTS.getKey(sound) != null
            ? ForgeRegistries.SOUND_EVENTS.getKey(sound).toString()
            : "evermod:unknown_sound",
        volume, pitch, state, looping);
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

  public static PlaySoundPacket decode(EverBuffer buffer) {
    int entityId = buffer.readInt();
    String soundLocation = buffer.readUtf();
    float volume = Float.intBitsToFloat(buffer.readInt());
    float pitch = Float.intBitsToFloat(buffer.readInt());
    String state = buffer.readUtf();
    boolean looping = buffer.readInt() == 1;

    return new PlaySoundPacket(entityId,
        soundLocation != null ? soundLocation : "evermod:unknown_sound", volume, pitch,
        state != null ? state : "play", looping);
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
