package net.evermod.common.network;

import net.evermod.common.network.io.*;
import net.evermod.common.network.packets.PacketBase;
import net.evermod.common.network.packets.PlaySoundPacket;
import net.evermod.common.resources.EverLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ChannelManager {

  private static final int PROTOCOL_VERSION = 1;
  private static SimpleChannel CHANNEL;
  private static int messageID = 0;

  public static void register(String modid) {
    register(modid, "main_channel");
  }

  public static void register(String modid, String channelName) {
    if (modid == null || modid.isEmpty()) {
      throw new IllegalArgumentException("Mod ID cannot be null or empty");
    }
    if (channelName == null || channelName.isEmpty()) {
      throw new IllegalArgumentException("Channel name cannot be null or empty");
    }
    CHANNEL = ChannelBuilder.named(EverLocation.parse(modid, channelName))
        .networkProtocolVersion(PROTOCOL_VERSION)
        .clientAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
        .serverAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION)).simpleChannel();

    registerPacket(PlaySoundPacket.class);
  }

  public static <T extends PacketBase<T>> void registerPacket(Class<T> packetClass) {
    CHANNEL.messageBuilder(packetClass, messageID++, NetworkDirection.PLAY_TO_CLIENT)
        .encoder((msg, buf) -> msg.encode(new EverBuffer(buf))).decoder(buf -> {
          try {
            return packetClass.getDeclaredConstructor().newInstance().decode(new EverBuffer(buf));
          } catch (Exception e) {
            throw new RuntimeException("Error decoding " + packetClass.getSimpleName(), e);
          }
        }).consumerMainThread((msg, ctxSupplier) -> msg.handle(new EverContext(ctxSupplier))).add();
  }

  public static void sendToServer(Object packet) {
    CHANNEL.send(packet, PacketDistributor.SERVER.noArg());
  }

  public static void sendToClient(Object packet, Object player) {
    if (player instanceof ServerPlayer sp) {
      CHANNEL.send(packet, PacketDistributor.PLAYER.with(sp));
    }
  }

  public static void sendToAllClients(Object packet) {
    CHANNEL.send(packet, PacketDistributor.ALL.noArg());
  }
}
