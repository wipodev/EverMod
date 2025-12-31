package net.evermod.network.annotations;

import net.minecraftforge.network.NetworkDirection;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EverPacket {

  NetworkDirection direction() default NetworkDirection.PLAY_TO_CLIENT;
}
