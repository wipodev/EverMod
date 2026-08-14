package net.evermod.network.packets;

import net.evermod.network.io.EverBuffer;
import net.evermod.network.io.EverContext;

/**
 * Abstract base class for all network packets within the Evermod framework.
 * Classes extending this must implement custom encoding and handling logic.
 */
public abstract class PacketBase {

  /**
   * Encodes the packet payload into the provided EverBuffer.
   *
   * @param buffer The buffer where packet data will be written.
   */
  public abstract void encode(EverBuffer buffer);

  /**
   * Handles the packet execution logic on the receiving side (client or server).
   *
   * @param context The network execution context.
   */
  public abstract void handle(EverContext context);
}
