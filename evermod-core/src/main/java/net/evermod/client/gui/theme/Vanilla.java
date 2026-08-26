package net.evermod.client.gui.theme;

import net.evermod.EverMod;
import net.evermod.client.graphics.style.Border;
import net.evermod.client.graphics.style.BorderColor;
import net.evermod.client.gui.api.style.TextAlignment;
import net.evermod.client.gui.widget.Button;
import net.evermod.client.gui.widget.Checkbox;
import net.evermod.client.gui.widget.Label;
import net.evermod.client.gui.widget.Select;
import net.evermod.client.gui.widget.Slider;
import net.evermod.math.EverMath;
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

  public static Checkbox checkbox() {
    Checkbox checkbox = new Checkbox(false)
        .padding(2);
    checkbox.getBox()
        .background(0xFF222222)
        .border(Border.DEFAULT, BorderColor.all(0xFF000000), BorderColor.all(0xFFFFFFFF));
    return checkbox;
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

  public static Slider percentSlider(String label, double defaultValue) {
    return slider()
        .step(1.0D)
        .text(val -> String.format("%s: %.0f%%", label, val))
        .value(defaultValue);
  }

  public static Slider optionSlider(String[] options, int defaultIndex) {
    return slider()
        .step(1.0D)
        .text(val -> options[(int) EverMath.clamp(val.intValue(), 0, options.length - 1)])
        .value((double) defaultIndex);
  }

  public static Select select() {
    return new Select()
        .size(120, 20)
        .border(1)
        .padding(4)
        .fontShadow(true)
        .textAlign(TextAlignment.CENTER)
        .background(btnBase)
        .optionStyle(option -> {
          option.background(btnDisabled);
          option.padding(4);
          option.fontShadow(true);
          option.textAlign(TextAlignment.CENTER);
          option.border(new Border(0, 1, 1, 1), BorderColor.DEFAULT, BorderColor.all(0xFFFFFFFF));
        });
  }
}
