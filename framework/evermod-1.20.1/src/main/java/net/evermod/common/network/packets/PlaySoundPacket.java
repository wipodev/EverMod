package net.evermod.common.network.packets;

import net.evermod.common.network.io.EverContext;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.evermod.client.handlers.ClientSoundHandler;

/**
 * Implementación Forge 1.19.2 del paquete de sonido.
 */
public class PlaySoundPacket extends PlaySoundPacketBase {

  public PlaySoundPacket(int entityId, SoundEvent sound, float volume, float pitch, String state,
      boolean looping) {
    super(entityId, ForgeRegistries.SOUND_EVENTS.getKey(sound).toString(), volume, pitch, state,
        looping);
  }

  @Override
  public void handle(EverContext context) {
    context.runClient(() -> ClientSoundHandler.handle(this));
  }
}
