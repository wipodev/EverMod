package net.evermod.network;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.objectweb.asm.Type;
import net.evermod.network.annotations.EverPacket;
import net.evermod.network.annotations.EverPacketDirection;
import net.evermod.network.io.EverBuffer;
import net.evermod.network.packets.PacketBase;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;

/**
 * Central channel manager acting as the main public static facade for networking.
 * Handles packet scanning, ID tracking, and delegates low-level actions to an internal adapter.
 * 
 * @author Wipodev
 */
public final class ChannelManager {

  private static final Set<Class<?>> REGISTERED_PACKETS = ConcurrentHashMap.newKeySet();
  private static final Map<String, Integer> MESSAGE_IDS = new ConcurrentHashMap<>();

  private static INetworkAdapter adapter;

  private ChannelManager() {
    // Static utility class
  }

  /**
   * Binds the version-specific network adapter. Called internally by the version module.
   * 
   * @param networkAdapter The concrete version adapter.
   */
  public static void init(INetworkAdapter networkAdapter) {
    adapter = networkAdapter;
  }

  // --- PUBLIC STATIC FACADE ---

  /**
   * Registers the main channel and automatically scans packets for a target mod.
   * 
   * @param modid Target mod ID.
   */
  public static void autoRegister(String modid) {
    register(modid, "main_channel");
    autoRegisterPackets(modid);
  }

  /**
   * Registers a default 'main_channel' for a target mod.
   * 
   * @param modid Target mod ID.
   */
  public static void register(String modid) {
    register(modid, "main_channel");
  }

  /**
   * Registers a custom channel name for a target mod.
   * 
   * @param modid Target mod ID.
   * @param channelName Custom channel name.
   */
  public static void register(String modid, String channelName) {
    ensureInitialized();
    MESSAGE_IDS.putIfAbsent(modid, 0);
    adapter.createChannel(modid, channelName);
  }

  /**
   * Registers a packet into the specified mod's network channel.
   * 
   * @param <T> Packet type extending PacketBase.
   * @param modid Target mod ID.
   * @param type Packet class.
   * @param decoder Function to decode buffer into packet.
   * @param direction Network direction.
   */
  public static <T extends PacketBase> void registerPacket(
      String modid, Class<T> type, Function<EverBuffer, T> decoder, EverPacketDirection direction) {
    ensureInitialized();
    if (!REGISTERED_PACKETS.add(type)) {
      return;
    }

    register(modid);
    int nextId =
        MESSAGE_IDS.compute(modid, (k, currentId) -> currentId == null ? 1 : currentId + 1) - 1;
    adapter.registerPacket(modid, nextId, type, decoder, direction);
  }

  /**
   * Sends a packet to the server from a target mod channel.
   * 
   * @param modid Target mod ID.
   * @param packet Packet instance.
   */
  public static void sendToServer(String modid, Object packet) {
    ensureInitialized();
    adapter.sendToServer(modid, packet);
  }

  /**
   * Sends a packet to a specific client player.
   * 
   * @param modid Target mod ID.
   * @param packet Packet instance.
   * @param player Target player object.
   */
  public static void sendToClient(String modid, Object packet, Object player) {
    ensureInitialized();
    adapter.sendToClient(modid, packet, player);
  }

  /**
   * Sends a packet to all connected clients.
   * 
   * @param modid Target mod ID.
   * @param packet Packet instance.
   */
  public static void sendToAllClients(String modid, Object packet) {
    ensureInitialized();
    adapter.sendToAllClients(modid, packet);
  }

  // --- INTERNAL HELPER LOGIC ---

  private static void autoRegisterPackets(String modid) {
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
        registerTyped(modid, rawClass.asSubclass(PacketBase.class), lookup);
      } catch (Throwable t) {
        throw new RuntimeException("Error registering packet automatically: " + data.memberName(),
            t);
      }
    }
  }

  private static <T extends PacketBase> void registerTyped(
      String modid, Class<T> packetClass, MethodHandles.Lookup lookup) throws Throwable {
    EverPacket annotation = packetClass.getAnnotation(EverPacket.class);
    MethodHandle decodeHandle = lookup.findStatic(packetClass, "decode",
        MethodType.methodType(packetClass, EverBuffer.class));

    registerPacket(modid, packetClass, buffer -> {
      try {
        return packetClass.cast(decodeHandle.invoke(buffer));
      } catch (Throwable t) {
        throw new RuntimeException("Error decoding " + packetClass.getName(), t);
      }
    }, annotation.direction());
  }

  private static void ensureInitialized() {
    if (adapter == null) {
      throw new IllegalStateException("NetworkAdapter has not been initialized for EverMod.");
    }
  }
}
