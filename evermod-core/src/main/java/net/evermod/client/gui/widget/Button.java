package net.evermod.client.gui.widget;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public class Button extends AbstractLabel<Button> {

  private Consumer<Button> onClickAction;

  public Button(Component text) {
    super(text);
  }

  public Button(String text) {
    super(text);
  }

  public Button() {
    super();
  }

  public Button onClick(Consumer<Button> action) {
    this.onClickAction = action;
    return self();
  }

  public Button onClick(Runnable action) {
    this.onClickAction = button -> action.run();
    return self();
  }

  public void playDownSound() {
    Minecraft.getInstance()
        .getSoundManager()
        .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (super.mouseClicked(mouseX, mouseY, button)) {
      if (button == 0) {
        this.playDownSound();
        if (this.onClickAction != null) {
          this.onClickAction.accept(this);
        }
        return true;
      }
    }
    return false;
  }
}
