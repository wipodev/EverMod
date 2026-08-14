package net.evermod.network.packets;

import java.util.Objects;
import javax.annotation.Nonnull;
import net.evermod.client.handlers.ClientSoundHandler;
import net.evermod.network.annotations.EverPacket;
import net.evermod.network.io.EverBuffer;
import net.evermod.network.io.EverContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Network packet responsible for triggering, transitioning, or stopping sound events
 * on the client side for specific entities.
 */
@EverPacket
public class PlaySoundPacket extends PacketBase {

  private final int entityId;
  private final @Nonnull String soundLocation;
  private final float volume;
  private final float pitch;
  private final float targetVolume;
  private final float targetPitch;
  private final int transitionTicks;
  private final @Nonnull String state;
  private final boolean looping;

  /**
   * Primary constructor using raw sound ResourceLocation string.
   *
   * @param entityId The target entity ID attached to the sound.
   * @param soundLocation The string representation of the SoundEvent ResourceLocation.
   * @param volume Initial sound volume.
   * @param pitch Initial sound pitch.
   * @param targetVolume Final target volume for transitions.
   * @param targetPitch Final target pitch for transitions.
   * @param transitionTicks Duration in ticks for smooth transitions.
   * @param state Execution state ("play", "transition", "stop", "stop_all").
   * @param looping Whether the sound should loop continuously.
   */
  public PlaySoundPacket(int entityId, String soundLocation, float volume, float pitch,
      float targetVolume, float targetPitch, int transitionTicks, String state, boolean looping) {
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
    this.targetVolume = targetVolume;
    this.targetPitch = targetPitch;
    this.transitionTicks = transitionTicks;
    this.state = state;
    this.looping = looping;
  }

  /**
   * Overloaded constructor for direct SoundEvent instance usage.
   */
  public PlaySoundPacket(int entityId, SoundEvent sound, float volume, float pitch,
      float targetVolume, float targetPitch, int transitionTicks, String state, boolean looping) {
    this(entityId,
        ForgeRegistries.SOUND_EVENTS.getKey(sound) != null
            ? ForgeRegistries.SOUND_EVENTS.getKey(sound).toString()
            : "evermod:unknown_sound",
        volume, pitch, targetVolume, targetPitch, transitionTicks, state, looping);
  }

  @Override
  public void encode(EverBuffer buffer) {
    buffer.writeInt(this.entityId);
    buffer.writeUtf(this.soundLocation);
    buffer.writeInt(Float.floatToIntBits(this.volume));
    buffer.writeInt(Float.floatToIntBits(this.pitch));
    buffer.writeInt(Float.floatToIntBits(this.targetVolume));
    buffer.writeInt(Float.floatToIntBits(this.targetPitch));
    buffer.writeInt(this.transitionTicks);
    buffer.writeUtf(this.state);
    buffer.writeInt(this.looping ? 1 : 0);
  }

  /**
   * Decodes packet data from the incoming EverBuffer stream.
   *
   * @param buffer The incoming buffer containing packet payload.
   * @return A new PlaySoundPacket instance initialized with decoded data.
   */
  public static PlaySoundPacket decode(EverBuffer buffer) {
    int entityId = buffer.readInt();
    String soundLocation = buffer.readUtf();
    float volume = Float.intBitsToFloat(buffer.readInt());
    float pitch = Float.intBitsToFloat(buffer.readInt());
    float targetVolume = Float.intBitsToFloat(buffer.readInt());
    float targetPitch = Float.intBitsToFloat(buffer.readInt());
    int transitionTicks = buffer.readInt();
    String state = buffer.readUtf();
    boolean looping = buffer.readInt() == 1;

    return new PlaySoundPacket(entityId,
        soundLocation != null ? soundLocation : "evermod:unknown_sound", volume, pitch,
        targetVolume, targetPitch, transitionTicks, state != null ? state : "play", looping);
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

  public float getTargetVolume() {
    return this.targetVolume;
  }

  public float getTargetPitch() {
    return this.targetPitch;
  }

  public int getTransitionTicks() {
    return this.transitionTicks;
  }

  public String getState() {
    return this.state;
  }

  public boolean isLooping() {
    return this.looping;
  }
}
