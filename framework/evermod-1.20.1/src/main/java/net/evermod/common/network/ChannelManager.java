package net.evermod.common.network;

import java.util.function.Supplier;
import net.evermod.common.network.io.*;
import net.evermod.common.network.packets.PacketBase;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * Sistema de red EverMod — versión Forge 1.20.1
 * 
 * Mantiene el mismo estilo estático de Forge tradicional, pero con la API unificada de EverMod.
 */
public class ChannelManager {

  private static final String PROTOCOL_VERSION = "1";
  private static SimpleChannel CHANNEL;
  private static int messageID = 0;

  /** Crea y registra el canal principal de EverMod. */
  public static void register(String modid) {
    register(modid, "main_channel");
  }

  /** Permite usar un nombre de canal personalizado si se desea. */
  @SuppressWarnings("removal")
  public static void register(String modid, String channelName) {
    CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(modid, channelName))
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
        .serverAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();
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
        }).consumerMainThread((pkt, ctxSupplier) -> {
          Supplier<NetworkEvent.Context> context = (Supplier<NetworkEvent.Context>) ctxSupplier;
          pkt.handle(new ForgeContext(context.get()));
        }).add();
  }

  /** Envía un paquete al servidor. */
  public static void sendToServer(Object packet) {
    CHANNEL.sendToServer(packet);
  }

  /** Envía un paquete a un cliente específico. */
  public static void sendToClient(Object packet, Object player) {
    if (player instanceof ServerPlayer sp) {
      CHANNEL.send(PacketDistributor.PLAYER.with(() -> sp), packet);
    }
  }

  /** Envía un paquete a todos los clientes conectados. */
  public static void sendToAllClients(Object packet) {
    CHANNEL.send(PacketDistributor.ALL.noArg(), packet);
  }
}
