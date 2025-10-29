package net.evermod.common.network.io;

import net.minecraft.network.FriendlyByteBuf;

public class ForgeBuffer implements EverBuffer {
  private final Object buf; // genérico

  public ForgeBuffer(Object buf) {
    this.buf = buf;
  }

  private FriendlyByteBuf f() {
    return (FriendlyByteBuf) buf;
  }

  @Override
  public void writeInt(int value) {
    f().writeInt(value);
  }

  @Override
  public int readInt() {
    return f().readInt();
  }

  @Override
  public void writeString(String text) {
    f().writeUtf(text);
  }

  @Override
  public String readString() {
    return f().readUtf();
  }
}
