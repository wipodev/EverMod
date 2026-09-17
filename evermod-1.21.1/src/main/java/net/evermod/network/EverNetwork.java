package net.evermod.network;

import java.util.function.Consumer;
import net.evermod.network.annotations.EverPacketDirection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.network.NetworkDirection;

/**
 * 1.21+ Modern screen opening utilities.
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
    player.openMenu(provider, bufferWriter::accept);
  }

  public static NetworkDirection<RegistryFriendlyByteBuf> toNetworkDirection(
      EverPacketDirection dir) {
    return switch (dir) {
      case TO_CLIENT -> NetworkDirection.PLAY_TO_CLIENT;
      case TO_SERVER -> NetworkDirection.PLAY_TO_SERVER;
    };
  }
}
