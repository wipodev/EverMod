package net.evermod.network.io;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;

public class EverBuffer extends FriendlyByteBuf {

  public EverBuffer(ByteBuf parent) {
    super(parent);
  }
}
