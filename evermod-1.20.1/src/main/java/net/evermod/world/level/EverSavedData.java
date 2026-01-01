package net.evermod.world.level;

import java.util.function.Supplier;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public abstract class EverSavedData extends SavedData {

  protected abstract void read(CompoundTag tag);

  protected abstract CompoundTag write(CompoundTag tag);

  @Override
  public final CompoundTag save(@Nonnull CompoundTag tag) {
    return write(tag);
  }

  protected final void loadInternal(CompoundTag tag) {
    read(tag);
  }

  protected static <T extends EverSavedData> T get(ServerLevel level, String id,
      Supplier<T> constructor) {
    return level.getDataStorage().computeIfAbsent(tag -> {
      T data = constructor.get();
      data.loadInternal(tag);
      return data;
    }, constructor, id);
  }
}
