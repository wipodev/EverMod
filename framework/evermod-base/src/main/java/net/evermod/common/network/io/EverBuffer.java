package net.evermod.common.network.io;

public interface EverBuffer {
  void writeInt(int value);

  int readInt();

  void writeString(String text);

  String readString();
}
