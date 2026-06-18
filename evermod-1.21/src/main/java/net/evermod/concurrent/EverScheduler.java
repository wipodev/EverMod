package net.evermod.concurrent;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

public class EverScheduler {

  private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue =
      new ConcurrentLinkedQueue<>();

  public static void queueServerWork(int tick, Runnable action) {
    if (action == null || tick < 0) {
      return;
    }
    workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
  }

  public static void internalTick() {
    if (workQueue.isEmpty()) {
      return;
    }

    List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();

    workQueue.forEach(work -> {
      work.setValue(work.getValue() - 1);
      if (work.getValue() <= 0) {
        actions.add(work);
      }
    });

    for (AbstractMap.SimpleEntry<Runnable, Integer> e : actions) {
      try {
        e.getKey().run();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }

    workQueue.removeAll(actions);
  }
}
