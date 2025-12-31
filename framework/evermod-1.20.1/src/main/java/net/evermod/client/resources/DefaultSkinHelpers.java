package net.evermod.client.resources;

import java.util.UUID;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultSkinHelpers {

  public static ResourceLocation getDefaultSkin(UUID uuid) {
    return DefaultPlayerSkin.getDefaultSkin(uuid);
  }

  public static String getSkinModel(UUID uuid) {
    return DefaultPlayerSkin.getSkinModelName(uuid);
  }
}
