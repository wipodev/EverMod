package net.evermod.network.packets;

import net.evermod.network.io.EverBuffer;
import net.evermod.network.io.EverContext;

public abstract class PacketBase {

  public abstract void encode(EverBuffer buffer);

  public abstract void handle(EverContext context);
}
