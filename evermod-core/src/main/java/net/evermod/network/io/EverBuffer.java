package net.evermod.network.io;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Custom buffer wrapper extending Minecraft's FriendlyByteBuf to provide 
 * unified network serialization and deserialization across version modules.
 */
public class EverBuffer extends FriendlyByteBuf {

  /**
   * Constructs an EverBuffer instance wrapping an underlying Netty ByteBuf.
   *
   * @param parent The parent ByteBuf instance to wrap.
   */
  public EverBuffer(ByteBuf parent) {
    super(parent);
  }
}
