package net.evermod.common.resources;

import javax.annotation.Nonnull;
import net.minecraft.resources.ResourceLocation;

public class EverLocation {

  @SuppressWarnings("removal")
  public static ResourceLocation parse(@Nonnull String id) {
    return new ResourceLocation(id);
  }

  @SuppressWarnings("removal")
  public static ResourceLocation parse(@Nonnull String modid, @Nonnull String path) {
    return new ResourceLocation(modid, path);
  }
}
