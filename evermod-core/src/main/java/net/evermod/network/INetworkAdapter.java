package net.evermod.network;

import net.evermod.network.annotations.EverPacketDirection;
import net.evermod.network.io.EverBuffer;
import net.evermod.network.packets.PacketBase;
import java.util.function.Function;

/**
 * Internal interface defining version-specific networking operations.
 * 
 * @author Wipodev
 */
public interface INetworkAdapter {

  /**
   * Creates and registers a physical network channel for the specified mod.
   * 
   * @param modid Target mod ID.
   * @param channelName Name of the channel.
   */
  void createChannel(String modid, String channelName);

  /**
   * Registers a packet into the version-specific channel pipeline.
   * 
   * @param <T> Packet type extending PacketBase.
   * @param modid Target mod ID.
   * @param id Unique packet ID for the channel.
   * @param type Packet class.
   * @param decoder Packet decoder function.
   * @param direction EverMod network direction.
   */
  <T extends PacketBase> void registerPacket(
      String modid, int id, Class<T> type, Function<EverBuffer, T> decoder,
      EverPacketDirection direction);

  /**
   * Sends a packet to the server.
   * 
   * @param modid Target mod ID.
   * @param packet Packet instance.
   */
  void sendToServer(String modid, Object packet);

  /**
   * Sends a packet to a specific client player.
   * 
   * @param modid Target mod ID.
   * @param packet Packet instance.
   * @param player Target player object.
   */
  void sendToClient(String modid, Object packet, Object player);

  /**
   * Sends a packet to all connected clients.
   * 
   * @param modid Target mod ID.
   * @param packet Packet instance.
   */
  void sendToAllClients(String modid, Object packet);
}
