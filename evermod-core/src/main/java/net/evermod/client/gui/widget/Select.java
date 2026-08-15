package net.evermod.client.gui.widget;

import net.evermod.client.gui.Border;
import net.evermod.client.gui.BorderColor;
import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.OverlayProvider;

/**
 * Concrete Select/Dropdown UI component with full styling capabilities.
 * Supports customizable colors, max dropdown height, and option highlight effects.
 *
 * @param <V> The type of values stored in options.
 * @author Wipodev
 */
public class Select<V> extends AbstractSelect<V> implements OverlayProvider {

  private int optionHeight = 18;
  private int maxVisibleOptions = 5;

  private int headerBackgroundColor = 0xFF222222;
  private int dropdownBackgroundColor = 0xFF1A1A1A;
  private int hoverBackgroundColor = 0xFF333344;
  private int selectedBackgroundColor = 0xFF007ACC;

  private int textColor = 0xFFFFFFFF;
  private int borderColor = 0xFF666666;
  private int arrowColor = 0xFFAAAAAA;

  /**
   * Constructs a Select component with position and dimensions.
   *
   * @param x      Screen X position in pixels.
   * @param y      Screen Y position in pixels.
   * @param width  Component width.
   * @param height Header height.
   */
  public Select(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  /**
   * Constructs a Select at origin (0, 0) with zero dimensions.
   */
  public Select() {
    super(0, 0, 100, 20);
  }

  @Override
  public boolean isOverlayActive() {
    return this.expanded && !this.options.isEmpty();
  }

  // --- STYLING SETTERS ---

  public Select<V> optionHeight(int optionHeight) {
    this.optionHeight = optionHeight;
    return this;
  }

  public Select<V> maxVisibleOptions(int max) {
    this.maxVisibleOptions = max;
    return this;
  }

  public Select<V> colors(int headerBg, int dropdownBg, int hoverBg, int selectedBg, int text) {
    this.headerBackgroundColor = headerBg;
    this.dropdownBackgroundColor = dropdownBg;
    this.hoverBackgroundColor = hoverBg;
    this.selectedBackgroundColor = selectedBg;
    this.textColor = text;
    return this;
  }

  // --- RENDERING ---

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    // 1. Draw Main Header Box
    graphics.drawRect(this.x, this.y, this.width, this.height, this.headerBackgroundColor,
        Border.all(1), BorderColor.all(this.borderColor));

    // Render current selection text
    String displayLabel =
        (this.selectedOption != null) ? this.selectedOption.getLabel() : "Select...";
    int textY = this.y + (this.height - 8) / 2;
    graphics.drawString(displayLabel, this.x + 6, textY, this.textColor, false);

    // Render dropdown arrow indicator (▼ or ▲)
    String arrow = this.expanded ? "▲" : "▼";
    int arrowX = this.x + this.width - 12;
    graphics.drawString(arrow, arrowX, textY, this.arrowColor, false);
  }

  @Override
  public void renderOverlay(EverGraphics graphics, int mouseX, int mouseY) {
    if (!isVisible() || !this.expanded || this.options.isEmpty()) {
      return;
    }

    int visibleCount = Math.min(this.options.size(), this.maxVisibleOptions);
    int dropdownHeight = visibleCount * this.optionHeight;
    int dropdownY = this.y + this.height;

    // Dropdown background box
    graphics.drawRect(this.x, dropdownY, this.width, dropdownHeight, this.dropdownBackgroundColor,
        Border.all(1), BorderColor.all(this.borderColor));

    for (int i = 0; i < visibleCount; i++) {
      Option<V> option = this.options.get(i);
      int itemY = dropdownY + (i * this.optionHeight);

      boolean isHovered = mouseX >= this.x && mouseX < this.x + this.width &&
          mouseY >= itemY && mouseY < itemY + this.optionHeight;
      boolean isSelected = (this.selectedOption == option);

      // Highlight active or hovered row
      if (isSelected) {
        graphics.drawRect(this.x + 1, itemY, this.width - 2, this.optionHeight,
            this.selectedBackgroundColor);
      } else if (isHovered) {
        graphics.drawRect(this.x + 1, itemY, this.width - 2, this.optionHeight,
            this.hoverBackgroundColor);
      }

      // Draw item label
      int itemTextY = itemY + (this.optionHeight - 8) / 2;
      graphics.drawString(option.getLabel(), this.x + 8, itemTextY, this.textColor, false);
    }
  }

  @Override
  protected int getOptionIndexAt(int mouseX, int mouseY) {
    if (!this.expanded || this.options.isEmpty()) {
      return -1;
    }

    int dropdownY = this.y + this.height;
    int visibleCount = Math.min(this.options.size(), this.maxVisibleOptions);
    int totalDropdownHeight = visibleCount * this.optionHeight;

    if (mouseX >= this.x && mouseX < this.x + this.width &&
        mouseY >= dropdownY && mouseY < dropdownY + totalDropdownHeight) {
      int relativeY = mouseY - dropdownY;
      return relativeY / this.optionHeight;
    }

    return -1;
  }
}
