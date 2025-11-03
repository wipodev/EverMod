package net.evermod.common.network.io;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.DistExecutor;

public class EverContext {
  private final CustomPayloadEvent.Context ctxSupplier;

  public EverContext(CustomPayloadEvent.Context ctxSupplier) {
    this.ctxSupplier = ctxSupplier;
  }

  public void runClient(Runnable task) {
    if (ctxSupplier.isClientSide()) {
      ctxSupplier.enqueueWork(() -> {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> task.run());
      });
    }
    markHandled();
  }

  public void runServer(Runnable task) {
    if (ctxSupplier.isServerSide()) {
      ctxSupplier.enqueueWork(task);
    }
    markHandled();
  }

  public void markHandled() {
    ctxSupplier.setPacketHandled(true);
  }
}
