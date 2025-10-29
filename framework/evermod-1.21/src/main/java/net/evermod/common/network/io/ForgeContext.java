package net.evermod.common.network.io;

import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * Adaptador del contexto de red Forge 1.21 al sistema EverMod.
 */
public class ForgeContext implements EverContext {
  private final CustomPayloadEvent.Context ctx;

  public ForgeContext(CustomPayloadEvent.Context ctx) {
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
