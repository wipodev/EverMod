package net.evermod.common.network.io;

public interface EverContext {
  void runClient(Runnable task);

  void runServer(Runnable task);
}
