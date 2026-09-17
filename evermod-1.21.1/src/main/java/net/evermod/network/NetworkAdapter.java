package net.evermod.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import net.evermod.network.annotations.EverPacketDirection;
import net.evermod.network.io.EverBuffer;
import net.evermod.network.io.EverContext;
import net.evermod.network.packets.PacketBase;
import net.evermod.resources.EverLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.Channel.VersionTest;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

public class NetworkAdapter implements INetworkAdapter {

  private static final int PROTOCOL_VERSION = 1;
  private final Map<String, SimpleChannel> channels = new ConcurrentHashMap<>();

  @Override
  public void createChannel(String modid, String channelName) {
    if (modid == null || modid.isEmpty()) {
      throw new IllegalArgumentException("Mod ID cannot be null or empty");
    }
    if (channelName == null || channelName.isEmpty()) {
      throw new IllegalArgumentException("Channel name cannot be null or empty");
    }

    SimpleChannel channel = ChannelBuilder
        .named(EverLocation.parse(modid, channelName))
        .networkProtocolVersion(PROTOCOL_VERSION)
        .clientAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
        .serverAcceptedVersions(VersionTest.exact(PROTOCOL_VERSION))
        .simpleChannel();

    channels.put(modid, channel);
  }

  @Override
  public <T extends PacketBase> void registerPacket(String modid, int id, Class<T> type,
      Function<EverBuffer, T> decoder, EverPacketDirection direction) {
    SimpleChannel channel = channels.get(modid);
    if (channel == null) {
      throw new IllegalStateException("ChannelManager was not initialized. Call register() first.");
    }

    NetworkDirection<RegistryFriendlyByteBuf> dir = EverNetwork.toNetworkDirection(direction);

    channel.messageBuilder(type, id, dir)
        .encoder((msg, buf) -> msg.encode(new EverBuffer(buf)))
        .decoder(buf -> decoder.apply(new EverBuffer(buf)))
        .consumerMainThread((msg, ctx) -> msg.handle(new EverContext(ctx)))
        .add();
  }

  @Override
  public void sendToServer(String modid, Object packet) {
    SimpleChannel channel = channels.get(modid);
    if (channel != null) {
      channel.send(packet, PacketDistributor.SERVER.noArg());
    }
  }

  @Override
  public void sendToClient(String modid, Object packet, Object player) {
    SimpleChannel channel = channels.get(modid);
    if (channel != null && player instanceof ServerPlayer sp) {
      channel.send(packet, PacketDistributor.PLAYER.with(sp));
    }
  }

  @Override
  public void sendToAllClients(String modid, Object packet) {
    SimpleChannel channel = channels.get(modid);
    if (channel != null) {
      channel.send(packet, PacketDistributor.ALL.noArg());
    }
  }
}
