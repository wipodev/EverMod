package net.evermod.sounds;

import net.evermod.resources.EverLocation;
import net.minecraft.sounds.SoundEvent;

public class EverSoundEvent {

  public static SoundEvent create(String modid, String name) {
    return SoundEvent.createVariableRangeEvent(EverLocation.parse(modid, name));
  }
}
