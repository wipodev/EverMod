package net.evermod.common.network;

import net.evermod.common.network.io.*;
import net.evermod.common.network.packets.PacketBase;
import net.evermod.common.network.packets.PlaySoundPacketBase;
import net.evermod.common.resources.EverLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

/**
 * Sistema de red EverMod — versión Forge 1.21
 * 
 * Mantiene el mismo estilo estático de Forge tradicional, pero con la API unificada de EverMod.
 */
public class ChannelManager {

  private static final int PROTOCOL_VERSION = 1;
  private static SimpleChannel CHANNEL;
  private static int messageID = 0;

  /** Crea y registra el canal principal de EverMod. */
  public static void register(String modid) {
    register(modid, "main_channel");
  }

  /** Permite usar un nombre de canal personalizado si se desea. */
  public static void register(String modid, String channelName) {
    CHANNEL = ChannelBuilder.named(EverLocation.parse(modid, channelName))
        .networkProtocolVersion(PROTOCOL_VERSION)
        .clientAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
        .serverAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION)).simpleChannel();

    registerMessage(PlaySoundPacketBase.class);
  }

  /** Registra un paquete universal EverMod. */
  public static <T extends PacketBase<T>> void registerMessage(Class<T> packetClass) {
    CHANNEL.messageBuilder(packetClass, messageID++)
        .encoder((pkt, buf) -> pkt.encode(new ForgeBuffer(buf))).decoder(buf -> {
          try {
            T packet = packetClass.getDeclaredConstructor().newInstance();
            packet.decode(new ForgeBuffer(buf));
            return packet;
          } catch (Exception e) {
            throw new RuntimeException("Error decoding " + packetClass.getSimpleName(), e);
          }
        }).consumerMainThread((pkt, ctx) -> pkt.handle(new ForgeContext(ctx))).add();
  }

  /** Envía un paquete al servidor. */
  public static void sendToServer(Object packet) {
    CHANNEL.send(packet, PacketDistributor.SERVER.noArg());
  }

  /** Envía un paquete a un cliente específico. */
  public static void sendToClient(Object packet, Object player) {
    if (player instanceof ServerPlayer sp) {
      CHANNEL.send(packet, PacketDistributor.PLAYER.with(sp));
    }
  }

  /** Envía un paquete a todos los clientes conectados. */
  public static void sendToAllClients(Object packet) {
    CHANNEL.send(packet, PacketDistributor.ALL.noArg());
  }
}
