package net.evermod.network;

import java.util.function.Consumer;
import net.evermod.network.annotations.EverPacketDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkHooks;

/**
 * 1.19.2 / 1.20.1 Legacy screen opening utilities.
 *
 * @author Wipodev
 */
public final class EverNetwork {

  private EverNetwork() {}

  /**
   * Opens a container screen for a server player with custom buffer data.
   *
   * @param player Target server player.
   * @param provider Menu provider.
   * @param bufferWriter Writer for extra container payload data.
   */
  public static void openMenu(ServerPlayer player, MenuProvider provider,
      Consumer<FriendlyByteBuf> bufferWriter) {
    NetworkHooks.openScreen(player, provider, bufferWriter::accept);
  }

  public static NetworkDirection toNetworkDirection(EverPacketDirection direction) {
    return direction == EverPacketDirection.TO_SERVER
        ? NetworkDirection.PLAY_TO_SERVER
        : NetworkDirection.PLAY_TO_CLIENT;
  }
}
