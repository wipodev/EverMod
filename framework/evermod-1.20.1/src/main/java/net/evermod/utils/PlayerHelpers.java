package net.evermod.utils;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public class PlayerHelpers {

  public static ServerLevel getServerLevel(ServerPlayer player) {
    return player.serverLevel();
  }
}
