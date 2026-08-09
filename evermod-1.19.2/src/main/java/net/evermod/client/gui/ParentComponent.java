package net.evermod.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

/**
 * Abstract base class for UI components that can contain and manage child components.
 * Propagates rendering, bounds checks, and user input events down the component hierarchy.
 *
 * @author Wipodev
 */
public abstract class ParentComponent extends AbstractComponent {

  public enum BackgroundMode {
    STRETCH, CENTER, TILE
  }

  protected final List<UIComponent> children = new ArrayList<>();
  protected int backgroundColor = 0x00000000;
  protected ResourceLocation backgroundImage = null;
  protected BackgroundMode backgroundMode = BackgroundMode.STRETCH;
  protected int textureWidth = 256;
  protected int textureHeight = 256;
  private boolean initialized = false;

  public ParentComponent(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public ParentComponent() {
    super();
  }

  /**
     * Lifecycle method meant to be overridden by custom component subclasses
     * to declaratively populate child components upon initialization.
     */
  protected void build() {
    // Default implementation does nothing. Overridden in sub-components.
  }

  /**
     * Ensures child components are built exactly once before rendering or event handling.
     */
  protected void ensureInitialized() {
    if (!this.initialized) {
      this.initialized = true;
      build();
    }
  }

  // --- BACKGROUND FLUENT SETTERS ---

  public int getBackgroundColor() {
    return this.backgroundColor;
  }

  public BackgroundMode getBackgroundMode() {
    return this.backgroundMode;
  }

  public ParentComponent background(ResourceLocation texture, int textureWidth, int textureHeight,
      BackgroundMode mode, int ARGBColor) {
    this.backgroundImage = texture;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
    this.backgroundMode = mode;
    this.backgroundColor = ARGBColor;
    return this;
  }

  public ParentComponent background(ResourceLocation texture, int textureWidth, int textureHeight,
      BackgroundMode mode) {
    return background(texture, textureWidth, textureHeight, mode, 0x00000000);
  }

  public ParentComponent background(ResourceLocation texture, int textureWidth, int textureHeight) {
    return background(texture, textureWidth, textureHeight, BackgroundMode.STRETCH);
  }

  public ParentComponent background(ResourceLocation texture, BackgroundMode mode, int ARGBColor) {
    return background(texture, 256, 256, mode, ARGBColor);
  }

  public ParentComponent background(ResourceLocation texture, BackgroundMode mode) {
    return background(texture, 256, 256, mode);
  }

  public ParentComponent background(ResourceLocation texture) {
    return background(texture, 256, 256, BackgroundMode.STRETCH);
  }

  public ParentComponent background(int ARGBColor) {
    this.backgroundColor = ARGBColor;
    return this;
  }

  public ParentComponent backgroundColor(int ARGBColor) {
    return background(ARGBColor);
  }

  // --- CHILD MANAGEMENT ---

  /**
   * Adds a child component to this container.
   *
   * @param child Component to add.
   * @return This container instance for chainable calls.
   */
  public ParentComponent addChild(UIComponent child) {
    if (child != null && !this.children.contains(child)) {
      this.children.add(child);
    }
    return this;
  }

  /**
   * Removes a child component from this container.
   *
   * @param child Component to remove.
   * @return True if the child was removed.
   */
  public boolean removeChild(UIComponent child) {
    return this.children.remove(child);
  }

  /**
   * Clears all child components from this container.
   */
  public void clearChildren() {
    this.children.clear();
  }

  /**
   * Gets an unmodifiable view of the child components list.
   *
   * @return Immutable list of children.
   */
  public List<UIComponent> getChildren() {
    return Collections.unmodifiableList(this.children);
  }

  // --- PROPAGATED RENDERING ---

  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!isVisible()) {
      return;
    }

    ensureInitialized();
    renderBackground(graphics, mouseX, mouseY, partialTicks);

    // Render all visible children
    for (UIComponent child : this.children) {
      if (child.isVisible()) {
        child.render(graphics, mouseX, mouseY, partialTicks);
      }
    }
  }

  /**
   * Renders container background color or images using EverGraphics.
   */
  protected void renderBackground(EverGraphics graphics, int mouseX, int mouseY,
      float partialTicks) {
    // 1. Render solid color background if present
    if ((this.backgroundColor >> 24 & 0xFF) > 0) {
      graphics.fill(this.x, this.y, this.x + this.width, this.y + this.height,
          this.backgroundColor);
    }

    // 2. Render background image if set according to BackgroundMode
    if (this.backgroundImage != null) {
      switch (this.backgroundMode) {
        case CENTER:
          // Correctly center the image based on real texture dimensions
          int centerX = this.x + (this.width - this.textureWidth) / 2;
          int centerY = this.y + (this.height - this.textureHeight) / 2;
          graphics.drawTexture(this.backgroundImage, centerX, centerY, this.textureWidth,
              this.textureHeight, this.textureWidth, this.textureHeight);
          break;

        case TILE:
          // Tile image horizontally and vertically across the container bounds
          for (int tileX = 0; tileX < this.width; tileX += this.textureWidth) {
            for (int tileY = 0; tileY < this.height; tileY += this.textureHeight) {
              int drawWidth = Math.min(this.textureWidth, this.width - tileX);
              int drawHeight = Math.min(this.textureHeight, this.height - tileY);

              graphics.drawTexture(this.backgroundImage, this.x + tileX, this.y + tileY, drawWidth,
                  drawHeight, this.textureWidth, this.textureHeight);
            }
          }
          break;

        case STRETCH:
        default:
          graphics.drawTexture(this.backgroundImage, this.x, this.y, this.width, this.height,
              this.textureWidth, this.textureHeight);
          break;
      }
    }
  }

  // --- PROPAGATED INPUT EVENTS ---

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!isVisible() || !isEnabled()) {
      return false;
    }

    ensureInitialized();

    // Iterate backwards so components drawn on top (front) receive clicks first
    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.mouseClicked(mouseX, mouseY, button)) {
        return true; // Event consumed by child
      }
    }
    return false;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (!isVisible() || !isEnabled()) {
      return false;
    }

    ensureInitialized();

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.mouseReleased(mouseX, mouseY, button)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    if (!isVisible() || !isEnabled()) {
      return false;
    }

    ensureInitialized();

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.mouseScrolled(mouseX, mouseY, delta)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (!isVisible() || !isEnabled()) {
      return false;
    }

    ensureInitialized();

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.keyPressed(keyCode, scanCode, modifiers)) {
        return true;
      }
    }
    return false;
  }
}
