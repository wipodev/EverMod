package net.evermod.common.network.packets;

import net.evermod.common.network.io.EverBuffer;
import net.evermod.common.network.io.EverContext;

public abstract class PacketBase<T extends PacketBase<T>> {

  public abstract void encode(EverBuffer buffer);

  public abstract T decode(EverBuffer buffer);

  public abstract void handle(EverContext context);
}
