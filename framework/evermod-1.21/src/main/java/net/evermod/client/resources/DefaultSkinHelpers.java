package net.evermod.client.resources;

import java.util.UUID;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DefaultSkinHelpers {

  public static ResourceLocation getDefaultSkin(UUID uuid) {
    return DefaultPlayerSkin.get(uuid).texture();
  }

  public static String getSkinModel(UUID uuid) {
    PlayerSkin skin = DefaultPlayerSkin.get(uuid);
    return skin.model().id();
  }
}
