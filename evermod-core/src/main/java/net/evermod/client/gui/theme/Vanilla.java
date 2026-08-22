package net.evermod.client.gui.theme;

import net.evermod.EverMod;
import net.evermod.client.graphics.style.Border;
import net.evermod.client.graphics.style.BorderColor;
import net.evermod.client.gui.api.style.TextAlignment;
import net.evermod.client.gui.widget.Button;
import net.evermod.client.gui.widget.Checkbox;
import net.evermod.client.gui.widget.Label;
import net.evermod.client.gui.widget.Slider;
import net.evermod.resources.EverLocation;
import net.minecraft.resources.ResourceLocation;

public final class Vanilla {

  private static final ResourceLocation btnBase =
      EverLocation.parse(EverMod.EVER_ID, "textures/gui/button.png");
  private static final ResourceLocation btnDisabled =
      EverLocation.parse(EverMod.EVER_ID, "textures/gui/button_disabled.png");
  private static final ResourceLocation btnSlider =
      EverLocation.parse(EverMod.EVER_ID, "textures/gui/handle_slider.png");

  private Vanilla() {}

  public static Button button(String text, Runnable onClick) {
    return new Button(text)
        .onClick(onClick)
        .size(120, 20)
        .padding(2, 4)
        .textAlign(TextAlignment.CENTER)
        .border(Border.DEFAULT, BorderColor.DEFAULT, BorderColor.all(0xFFFFFFFF))
        .background(btnBase, btnBase, btnDisabled)
        .fontShadow(true);
  }

  public static Label label(String text) {
    return new Label(text)
        .fontShadow(true);
  }

  public static Slider slider() {
    Slider slider = new Slider()
        .size(120, 20)
        .border(1)
        .background(btnDisabled)
        .textAlign(TextAlignment.CENTER);
    slider.getHandle()
        .border(Border.DEFAULT, BorderColor.DEFAULT, BorderColor.all(0xFFFFFFFF))
        .background(btnSlider);
    return slider;
  }

  public static Checkbox checkbox() {
    Checkbox checkbox = new Checkbox(false)
        .padding(2);
    checkbox.getBox()
        .background(0xFF222222)
        .border(Border.DEFAULT, BorderColor.all(0xFF000000), BorderColor.all(0xFFFFFFFF));
    return checkbox;
  }
}
