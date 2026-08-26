package net.evermod.client.gui.widget;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.graphics.font.EverFont;
import net.evermod.client.gui.api.OverlayProvider;

public class Select extends AbstractLabel<Select> implements OverlayProvider {

  private final List<String> options = new ArrayList<>();
  private int selectedIndex = 0;
  private boolean expanded = false;
  private IntConsumer onChangeAction;
  private int optionHeight = 20;
  private int maxVisible = 5;
  private final List<Button> optionButtons = new ArrayList<>();
  private Consumer<Button> optionStyler;

  public Select(String text) {
    super(text);
  }

  public Select() {
    super();
  }

  public Select options(String... values) {
    return options(Arrays.asList(values));
  }

  public Select options(List<String> values) {
    this.options.clear();
    this.options.addAll(values);
    if (this.selectedIndex >= this.options.size()) {
      this.selectedIndex = 0;
    }
    rebuildOptions();
    return self();
  }

  public Select optionStyle(Consumer<Button> styler) {
    this.optionStyler = styler;
    applyOptionStyles();
    return self();
  }

  public Select onChange(IntConsumer action) {
    this.onChangeAction = action;
    return self();
  }

  public Select selectedIndex(int index) {
    if (index >= 0 && index < this.options.size() && this.selectedIndex != index) {
      this.selectedIndex = index;
      if (this.onChangeAction != null) {
        this.onChangeAction.accept(index);
      }
    }
    this.expanded = false;
    updateHeaderText();
    return self();
  }

  public int getSelectedIndex() {
    return this.selectedIndex;
  }

  public Select optionHeight(int optionHeight) {
    this.optionHeight = optionHeight;
    return self();
  }

  public Select maxVisible(int maxVisible) {
    this.maxVisible = maxVisible;
    return self();
  }

  private void updateHeaderText() {
    String labelText = (!this.options.isEmpty() && this.selectedIndex < this.options.size())
        ? this.options.get(this.selectedIndex)
        : "Select...";
    this.text(labelText);
  }

  private void rebuildOptions() {
    this.optionButtons.clear();
    for (int i = 0; i < this.options.size(); i++) {
      final int index = i;
      Button btn = new Button(this.options.get(i))
          .onClick(() -> selectedIndex(index));
      btn.setParent(this);
      this.optionButtons.add(btn);
    }
    applyOptionStyles();
    updateHeaderText();
  }

  private void applyOptionStyles() {
    if (this.optionStyler != null) {
      for (Button btn : this.optionButtons) {
        this.optionStyler.accept(btn);
      }
    }
  }

  @Override
  public boolean isOverlayActive() {
    return this.visible && this.expanded && !this.options.isEmpty();
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.expanded) {
      int count = Math.min(this.optionButtons.size(), this.maxVisible);
      for (int i = 0; i < count; i++) {
        if (this.optionButtons.get(i).mouseClicked(mouseX, mouseY, button)) {
          return true;
        }
      }
      this.expanded = false;
      updateHeaderText();
      return true;
    }

    if (super.mouseClicked(mouseX, mouseY, button)) {
      if (button == 0) {
        this.expanded = !this.expanded;
        updateHeaderText();
        return true;
      }
    }
    return false;
  }

  @Override
  protected void renderContent(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    super.renderContent(graphics, mouseX, mouseY, partialTicks);
    EverFont font = this.getFont();
    String arrow = this.expanded ? "▲" : "▼";
    int arrowWidth = font.width(arrow);
    int padRight = this.getContentPaddingRight();
    int textY = (this.height - font.fontHeight()) / 2;

    int arrowX = this.width - arrowWidth - padRight;
    graphics.drawString(arrow, arrowX, textY, this.getColor(), this.getTextShadow());
  }

  @Override
  public void renderOverlay(EverGraphics graphics, int mouseX, int mouseY) {
    if (!isVisible() || !this.expanded) {
      return;
    }

    int count = Math.min(this.optionButtons.size(), this.maxVisible);
    graphics.push();
    graphics.translate(this.getGlobalX(), this.getGlobalY(), 0.0F);
    for (int i = 0; i < count; i++) {
      Button btn = this.optionButtons.get(i);
      btn.enabled(this.enabled);
      btn.setBounds(0, this.height + (i * this.optionHeight), this.width, this.optionHeight);
      btn.render(graphics, mouseX, mouseY, 0.0F);
    }
    graphics.pop();
  }
}
