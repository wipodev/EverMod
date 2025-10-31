package net.evermod.common.resources;

import net.minecraft.resources.ResourceLocation;

public class EverLocation {

  @SuppressWarnings("removal")
  public static ResourceLocation parse(String id) {
    return new ResourceLocation(id);
  }

  @SuppressWarnings("removal")
  public static ResourceLocation parse(String modid, String path) {
    return new ResourceLocation(modid, path);
  }
}
