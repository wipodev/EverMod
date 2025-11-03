package net.evermod.common.network.io;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class EverContext {
  private final Supplier<NetworkEvent.Context> ctxSupplier;

  public EverContext(Supplier<NetworkEvent.Context> ctxSupplier) {
    this.ctxSupplier = ctxSupplier;
  }

  public void runClient(Runnable task) {
    if (ctxSupplier.get().getDirection().getReceptionSide().isClient()) {
      ctxSupplier.get().enqueueWork(() -> {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> task.run());
      });
    }
    markHandled();
  }

  public void runServer(Runnable task) {
    if (ctxSupplier.get().getDirection().getReceptionSide().isServer()) {
      ctxSupplier.get().enqueueWork(task);
    }
    markHandled();
  }

  public void markHandled() {
    ctxSupplier.get().setPacketHandled(true);
  }
}
