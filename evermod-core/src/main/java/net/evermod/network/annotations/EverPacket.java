package net.evermod.network.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used for automatic network packet scanning and registration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EverPacket {

  /**
   * Defines the target direction for this network packet.
   *
   * @return The packet direction.
   */
  EverPacketDirection direction() default EverPacketDirection.TO_CLIENT;
}
