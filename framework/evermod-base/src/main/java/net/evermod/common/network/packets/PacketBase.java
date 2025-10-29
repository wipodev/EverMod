package net.evermod.common.network.packets;

import net.evermod.common.network.io.EverBuffer;
import net.evermod.common.network.io.EverContext;

/**
 * Base genérica para todos los paquetes de EverMod.
 * 
 * Los mods deben extender esta clase para definir sus propios paquetes. Cada implementación define
 * cómo se codifica, decodifica y maneja su mensaje.
 */
public abstract class PacketBase<T extends PacketBase<T>> {

  /**
   * Codifica los datos del paquete para enviarlos por red.
   * 
   * @param buffer El buffer de escritura (tipo depende de la versión de Forge).
   */
  public abstract void encode(EverBuffer buffer);

  /**
   * Decodifica los datos recibidos desde el buffer.
   * 
   * @param buffer El buffer de lectura (tipo depende de la versión de Forge).
   * @return Instancia del paquete decodificado.
   */
  public abstract T decode(EverBuffer buffer);

  /**
   * Maneja la lógica al recibir el paquete.
   * 
   * @param context Contexto de red (cliente o servidor según el lado).
   */
  public abstract void handle(EverContext context);
}
