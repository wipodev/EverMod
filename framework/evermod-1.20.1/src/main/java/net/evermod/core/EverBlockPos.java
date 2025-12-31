package net.evermod.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class EverBlockPos extends BlockPos {

  public EverBlockPos(int x, int y, int z) {
    super(x, y, z);
  }

  public EverBlockPos(double x, double y, double z) {
    super((int) Math.floor(x), (int) Math.floor(y), (int) Math.floor(z));
  }

  public EverBlockPos(Vec3 pos) {
    super((int) Math.floor(pos.x), (int) Math.floor(pos.y), (int) Math.floor(pos.z));
  }

  public EverBlockPos(Position pos) {
    this(pos.x(), pos.y(), pos.z());
  }

  public EverBlockPos(Vec3i pos) {
    this(pos.getX(), pos.getY(), pos.getZ());
  }

}
