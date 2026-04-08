package net.evermod.network;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.objectweb.asm.Type;
import net.evermod.network.annotations.EverPacket;
import net.evermod.network.annotations.EverPacketDirection;
import net.evermod.network.io.*;
import net.evermod.network.packets.PacketBase;
import net.evermod.resources.EverLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class ChannelManager {

  private static final Set<Class<?>> REGISTERED = ConcurrentHashMap.newKeySet();
  private static final int PROTOCOL_VERSION = 1;
  private static SimpleChannel CHANNEL;
  private static int messageID = 0;

  public static void autoRegister(String modid) {
    register(modid, "main_channel");
    autoRegisterPackets(modid);
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
  }

  public static void register(String modid) {
    register(modid, "main_channel");
  }

  public static <T extends PacketBase> void registerPacket(Class<T> type,
      Function<EverBuffer, T> decoder, NetworkDirection<RegistryFriendlyByteBuf> direction) {
    if (CHANNEL == null) {
      throw new IllegalStateException("ChannelManager was not initialized. Call register() first.");
    }
    if (!REGISTERED.add(type)) {
      throw new IllegalStateException("Packet already registered: " + type.getName());
    }
    CHANNEL.messageBuilder(type, messageID++, direction)
        .encoder((msg, buf) -> msg.encode(new EverBuffer(buf)))
        .decoder(buf -> decoder.apply(new EverBuffer(buf)))
        .consumerMainThread((msg, ctx) -> msg.handle(new EverContext(ctx))).add();
  }

  public static <T extends PacketBase> void registerPacketToClient(Class<T> type,
      Function<EverBuffer, T> decoder) {
    registerPacket(type, decoder, NetworkDirection.PLAY_TO_CLIENT);
  }

  public static <T extends PacketBase> void registerPacketToServer(Class<T> type,
      Function<EverBuffer, T> decoder) {
    registerPacket(type, decoder, NetworkDirection.PLAY_TO_SERVER);
  }

  public static void autoRegisterPackets(String modid) {
    ModFileScanData scanData = ModList.get().getModFileById(modid).getFile().getScanResult();
    MethodHandles.Lookup lookup = MethodHandles.lookup();

    for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
      if (!data.annotationType().equals(Type.getType(EverPacket.class))) {
        continue;
      }
      try {
        Class<?> rawClass = Class.forName(data.memberName());
        if (!PacketBase.class.isAssignableFrom(rawClass)) {
          throw new IllegalStateException(
              "@EverPacket used in a class that does not extend PacketBase: " + rawClass.getName());
        }
        registerTyped(rawClass.asSubclass(PacketBase.class), lookup);
      } catch (Throwable t) {
        throw new RuntimeException("Error registering packet automatically: " + data.memberName(),
            t);
      }
    }
  }

  private static <T extends PacketBase> void registerTyped(Class<T> packetClass,
      MethodHandles.Lookup lookup) throws Throwable {
    EverPacket annotation = packetClass.getAnnotation(EverPacket.class);
    NetworkDirection<RegistryFriendlyByteBuf> direction =
        toNetworkDirection(annotation.direction());
    MethodHandle decodeHandle = lookup.findStatic(packetClass, "decode",
        MethodType.methodType(packetClass, EverBuffer.class));
    registerPacket(packetClass, buffer -> {
      try {
        return packetClass.cast(decodeHandle.invoke(buffer));
      } catch (Throwable t) {
        throw new RuntimeException("Error decoding " + packetClass.getName(), t);
      }
    }, direction);
  }

  private static NetworkDirection<RegistryFriendlyByteBuf> toNetworkDirection(
      EverPacketDirection dir) {
    return switch (dir) {
      case TO_CLIENT -> NetworkDirection.PLAY_TO_CLIENT;
      case TO_SERVER -> NetworkDirection.PLAY_TO_SERVER;
    };
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
