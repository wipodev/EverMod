package net.evermod.config;

import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ConfigClientRegistry {

  @SuppressWarnings("removal")
  public static void registerScreen() {
    if (FMLEnvironment.dist == Dist.CLIENT) {
      ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class,
          () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, parentScreen) -> {
            // Si el desarrollador definió su propia pantalla, la instanciamos por reflexión
            if (ConfigManager.customScreenClass != null) {
              try {
                return (Screen) ConfigManager.customScreenClass.getConstructor(Screen.class)
                    .newInstance(parentScreen);
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            return new EverDefaultConfigGui(parentScreen);
          }));
    }
  }
}
