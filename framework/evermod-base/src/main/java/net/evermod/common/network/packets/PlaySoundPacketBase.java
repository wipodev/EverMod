package net.evermod.common.network.packets;

import net.evermod.common.network.io.EverBuffer;
import net.evermod.common.network.io.EverContext;

/**
 * Paquete EverMod universal para reproducir sonidos desde el servidor en el cliente.
 * 
 * El módulo base define solo la estructura y la interfaz abstracta; las implementaciones de Forge o
 * Fabric deben definir la lógica de reproducción concreta.
 */
public abstract class PlaySoundPacketBase extends PacketBase<PlaySoundPacketBase> {

  protected int entityId;
  protected String soundLocation;
  protected float volume;
  protected float pitch;
  protected String state;
  protected boolean looping;

  public PlaySoundPacketBase(int entityId, String soundLocation, float volume, float pitch,
      String state, boolean looping) {
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
    buffer.writeString(this.soundLocation);
    buffer.writeInt(Float.floatToIntBits(this.volume));
    buffer.writeInt(Float.floatToIntBits(this.pitch));
    buffer.writeString(this.state);
    buffer.writeInt(this.looping ? 1 : 0);
  }

  @Override
  public PlaySoundPacketBase decode(EverBuffer buffer) {
    this.entityId = buffer.readInt();
    this.soundLocation = buffer.readString();
    this.volume = Float.intBitsToFloat(buffer.readInt());
    this.pitch = Float.intBitsToFloat(buffer.readInt());
    this.state = buffer.readString();
    this.looping = buffer.readInt() == 1;
    return this;
  }

  /**
   * Método abstracto de manejo. Cada versión debe implementar su propia lógica (Forge, Fabric,
   * etc).
   */
  @Override
  public abstract void handle(EverContext context);

  // Getters
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
