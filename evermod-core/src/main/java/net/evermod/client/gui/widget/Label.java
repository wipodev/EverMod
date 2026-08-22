package net.evermod.client.gui.widget;

import net.minecraft.network.chat.Component;

public class Label extends AbstractLabel<Label> {

  public Label(Component text) {
    super(text);
  }

  public Label(String text) {
    super(text);
  }

  public Label() {
    super();
  }
}
