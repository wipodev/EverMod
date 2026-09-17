package net.evermod.config;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EverConfigScreen {
  // Colocada sobre una clase que herede de Screen para usarla como GUI personalizada
}
