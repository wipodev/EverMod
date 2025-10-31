package net.evermod.common.resources;

import net.minecraft.resources.ResourceLocation;

public class EverLocation {

  public static ResourceLocation parse(String id) {
    return ResourceLocation.parse(id);
  }

  public static ResourceLocation parse(String modid, String path) {
    return ResourceLocation.fromNamespaceAndPath(modid, path);
  }
}
