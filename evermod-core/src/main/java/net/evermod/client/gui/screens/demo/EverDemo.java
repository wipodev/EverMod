package net.evermod.client.gui.screens.demo;

import net.evermod.client.gui.api.style.TextAlignment;
import net.evermod.client.gui.screens.EverScreen;
import net.evermod.client.gui.theme.Vanilla;
import net.minecraft.network.chat.Component;

public class EverDemo extends EverScreen {

  public EverDemo() {
    super(Component.literal("EverUI Layout Demonstration"));
  }

  @Override
  public void setupUI() {

    this.add(Vanilla.label("=== MAIN BOX CONTAINER ===")
        .position(20, 10)
        .tooltip("tip de label"));

    this.add(Vanilla.select()
        .position(20, 30)
        .options("Español", "English", "Deutsch")
        .onChange(lang -> System.out.println("Idioma: " + lang)));

    this.add(Vanilla.button("Click Me", () -> System.out.println("Button clicked!"))
        .position(20, 60)
        .tooltip("Boton de Ejemplo"));

    this.add(Vanilla.label("Prueba Fill")
        .position(20, 90)
        .fillMaxWidth()
        .border(1)
        .textAlign(TextAlignment.RIGHT));

    this.add(Vanilla.slider()
        .position(20, 120)
        .value(30.0D)
        .text(val -> String.format("Volume: %.0f%%", val))
        .onChange(val -> {
          System.out.println(String.format("Volume: %s", val));
        }));

    this.add(Vanilla.percentSlider("Música", 80.0D)
        .position(20, 150)
        .onChange(val -> System.out.println(String.format("Musica: %s", val))));

    this.add(Vanilla.checkbox()
        .position(20, 180)
        .text("Check de prueba")
        .onChange(() -> System.out.println("Check clicked!")));
  }
}
