package net.evermod.common.resources;

import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

public class EverLocation {

  public static ResourceLocation parse(@Nonnull String id) {
    return ResourceLocation.parse(id);
  }

  public static ResourceLocation parse(@Nonnull String modid, @Nonnull String path) {
    return ResourceLocation.fromNamespaceAndPath(modid, path);
  }
}
