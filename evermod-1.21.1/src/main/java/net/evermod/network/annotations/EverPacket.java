package net.evermod.network.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EverPacket {

  EverPacketDirection direction() default EverPacketDirection.TO_CLIENT;
}
