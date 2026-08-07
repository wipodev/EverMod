package net.evermod.server.level;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ClientInformation;

public abstract class EverServerPlayer extends ServerPlayer {

  /**
   * Constructs an EverServerPlayer instance.
   *
   * @param minecraftServer   The server instance.
   * @param serverLevel       The level instance.
   * @param gameProfile       The player's game profile.
   * @param clientInformation The client settings and information.
   */
  public EverServerPlayer(MinecraftServer minecraftServer, ServerLevel serverLevel,
      GameProfile gameProfile) {
    super(minecraftServer, serverLevel, gameProfile, ClientInformation.createDefault());
  }

  /**
   * Checks if the player is currently on the ground.
   *
   * @return True if on ground, false otherwise.
   */
  public boolean onEverGround() {
    return this.onGround();
  }
}
