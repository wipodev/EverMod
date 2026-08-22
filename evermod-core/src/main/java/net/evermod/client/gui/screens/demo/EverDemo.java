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
        .position(20, 20)
        .tooltip("tip de label"));

    this.add(Vanilla.button("Click Me", () -> System.out.println("Button clicked!"))
        .position(20, 40)
        .tooltip("Boton de Ejemplo"));

    this.add(Vanilla.label("Prueba Fill")
        .position(20, 70)
        .fillMaxWidth()
        .border(1)
        .textAlign(TextAlignment.RIGHT));

    this.add(Vanilla.slider()
        .position(20, 100)
        .value(30.0D)
        .text(val -> String.format("Volume: %.0f%%", val))
        .onChange(val -> {
          System.out.println(String.format("Volume: %s", val));
        }));

    this.add(Vanilla.percentSlider("Música", 80.0D)
        .position(20, 130)
        .onChange(val -> System.out.println(String.format("Musica: %s", val))));

    this.add(Vanilla.checkbox()
        .position(20, 160)
        .text("Check de prueba")
        .onChange(() -> System.out.println("Check clicked!")));
  }
}
