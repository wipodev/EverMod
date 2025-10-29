package net.evermod.common.network.io;

import net.minecraftforge.network.NetworkEvent;

public class ForgeContext implements EverContext {
  private final NetworkEvent.Context ctx;

  public ForgeContext(NetworkEvent.Context ctx) {
    this.ctx = ctx;
  }

  @Override
  public void runClient(Runnable task) {
    ctx.enqueueWork(task);
  }

  @Override
  public void runServer(Runnable task) {
    ctx.enqueueWork(task);
  }
}
