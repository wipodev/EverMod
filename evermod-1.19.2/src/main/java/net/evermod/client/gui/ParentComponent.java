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

  /**
   * Rendering modes supported for background texture rendering.
   */
  public enum BackgroundMode {
    /** Stretches the texture to cover the container's full dimensions. */
    STRETCH,
    /** Centers the texture within the container using its natural dimensions. */
    CENTER,
    /** Repeats the texture horizontally and vertically to tile the container. */
    TILE
  }

  /** The list of managed child UI components. */
  protected final List<UIComponent> children = new ArrayList<>();

  /** Background color represented as an ARGB hex integer. Defaults to fully transparent. */
  protected int backgroundColor = 0x00000000;

  /** Optional background texture location. */
  protected ResourceLocation backgroundImage = null;

  /** Mode used to render the background texture. Defaults to STRETCH. */
  protected BackgroundMode backgroundMode = BackgroundMode.STRETCH;

  /** Source texture width in pixels. */
  protected int textureWidth = 256;

  /** Source texture height in pixels. */
  protected int textureHeight = 256;

  /** Border configuration structure. Defaults to no border. */
  protected Border border = Border.NONE;

  /** ARGB hex color code for the border. Defaults to fully transparent. */
  protected int borderColor = 0x00000000;

  /** Flag tracking whether {@link #build()} has executed. */
  private boolean initialized = false;

  /**
   * Constructs a parent container with explicit position and dimensions.
   *
   * @param x      Initial X position in pixels.
   * @param y      Initial Y position in pixels.
   * @param width  Container width in pixels.
   * @param height Container height in pixels.
   */
  public ParentComponent(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  /**
   * Constructs a parent container at origin (0, 0) with zero dimensions.
   */
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

  // --- BORDER FLUENT SETTERS ---

  /**
   * Sets custom border configuration and border color.
   *
   * @param border Custom border structure containing edge thicknesses.
   * @param borderColor ARGB hex color code for the border.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(Border border, int borderColor) {
    this.border = border;
    this.borderColor = borderColor;
    return this;
  }

  /**
   * Sets a uniform border of the specified thickness and color.
   *
   * @param thickness Uniform thickness in pixels for all sides.
   * @param borderColor ARGB hex color code for the border.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(int thickness, int borderColor) {
    return border(Border.all(thickness), borderColor);
  }

  /**
   * Sets a standard 1px border with the specified color.
   *
   * @param borderColor ARGB hex color code for the border.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(int borderColor) {
    return border(Border.DEFAULT, borderColor);
  }

  /**
   * Gets the active border structure.
   *
   * @return Active {@link Border}.
   */
  public Border getBorder() {
    return this.border;
  }

  /**
   * Gets the active border ARGB color.
   *
   * @return ARGB color code.
   */
  public int getBorderColor() {
    return this.borderColor;
  }

  // --- BACKGROUND FLUENT SETTERS ---

  /**
   * Gets the current ARGB background color.
   *
   * @return ARGB hex color code.
   */
  public int getBackgroundColor() {
    return this.backgroundColor;
  }

  /**
   * Gets the active background rendering mode.
   *
   * @return Current {@link BackgroundMode}.
   */
  public BackgroundMode getBackgroundMode() {
    return this.backgroundMode;
  }

  /**
   * Configures full background properties including texture, dimensions, mode, and color.
   *
   * @param texture       ResourceLocation of the texture.
   * @param textureWidth  Original width of the texture image.
   * @param textureHeight Original height of the texture image.
   * @param mode          Scaling or tiling mode.
   * @param ARGBColor     Solid background ARGB tint or color.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture, int textureWidth, int textureHeight,
      BackgroundMode mode, int ARGBColor) {
    this.backgroundImage = texture;
    this.textureWidth = textureWidth;
    this.textureHeight = textureHeight;
    this.backgroundMode = mode;
    this.backgroundColor = ARGBColor;
    return this;
  }

  /**
   * Configures background texture with explicit dimensions and rendering mode.
   *
   * @param texture       ResourceLocation of the texture.
   * @param textureWidth  Original width of the texture image.
   * @param textureHeight Original height of the texture image.
   * @param mode          Scaling or tiling mode.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture, int textureWidth, int textureHeight,
      BackgroundMode mode) {
    return background(texture, textureWidth, textureHeight, mode, 0x00000000);
  }

  /**
   * Configures background texture with explicit dimensions using STRETCH mode.
   *
   * @param texture       ResourceLocation of the texture.
   * @param textureWidth  Original width of the texture image.
   * @param textureHeight Original height of the texture image.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture, int textureWidth, int textureHeight) {
    return background(texture, textureWidth, textureHeight, BackgroundMode.STRETCH);
  }

  /**
   * Configures background texture using default 256x256 dimensions, specified mode, and ARGB color.
   *
   * @param texture   ResourceLocation of the texture.
   * @param mode      Scaling or tiling mode.
   * @param ARGBColor Solid background ARGB tint or color.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture, BackgroundMode mode, int ARGBColor) {
    return background(texture, 256, 256, mode, ARGBColor);
  }

  /**
   * Configures background texture using default 256x256 dimensions and specified mode.
   *
   * @param texture ResourceLocation of the texture.
   * @param mode    Scaling or tiling mode.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture, BackgroundMode mode) {
    return background(texture, 256, 256, mode);
  }

  /**
   * Configures background texture using default 256x256 dimensions and STRETCH mode.
   *
   * @param texture ResourceLocation of the texture.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture) {
    return background(texture, 256, 256, BackgroundMode.STRETCH);
  }

  /**
   * Sets a solid background ARGB color.
   *
   * @param ARGBColor ARGB hex color code.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(int ARGBColor) {
    this.backgroundColor = ARGBColor;
    return this;
  }

  /**
   * Alias for {@link #background(int)}.
   *
   * @param ARGBColor ARGB hex color code.
   * @return This container instance for method chaining.
   */
  public ParentComponent backgroundColor(int ARGBColor) {
    return background(ARGBColor);
  }

  // --- CHILD MANAGEMENT ---

  /**
   * Adds a child component to this container if not already present.
   *
   * @param child Component to add.
   * @return This container instance for method chaining.
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

  /**
   * {@inheritDoc}
   */
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
   * Renders container background color, texture image, and overlay borders using EverGraphics.
   *
   * @param graphics Custom graphic context.
   * @param mouseX Current mouse cursor X.
   * @param mouseY Current mouse cursor Y.
   * @param partialTicks Render partial tick delta.
   */
  protected void renderBackground(EverGraphics graphics, int mouseX, int mouseY,
      float partialTicks) {
    boolean hasBorder = (this.borderColor >> 24 & 0xFF) > 0 && this.border != Border.NONE;

    // 1. Render solid color background
    if ((this.backgroundColor >> 24 & 0xFF) > 0) {
      if (hasBorder) {
        graphics.drawBorderedRect(this.x, this.y, this.width, this.height,
            this.backgroundColor, this.border, this.borderColor);
      } else {
        graphics.drawRect(this.x, this.y, this.x + this.width, this.y + this.height,
            this.backgroundColor);
      }
    }

    // 2. Render background image if set
    if (this.backgroundImage != null) {
      switch (this.backgroundMode) {
        case CENTER:
          int centerX = this.x + (this.width - this.textureWidth) / 2;
          int centerY = this.y + (this.height - this.textureHeight) / 2;
          graphics.drawTexture(this.backgroundImage, centerX, centerY, this.textureWidth,
              this.textureHeight, this.textureWidth, this.textureHeight);
          break;

        case TILE:
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
          if (hasBorder) {
            graphics.drawBorderTexture(this.backgroundImage, this.x, this.y, this.width,
                this.height,
                this.textureWidth, this.textureHeight, this.border, this.borderColor);
          } else {
            graphics.drawTexture(this.backgroundImage, this.x, this.y, this.width, this.height,
                this.textureWidth, this.textureHeight);
          }
          break;
      }
    }

    // 3. Render standalone border outline if there was no solid background color or stretched image
    if (hasBorder && (this.backgroundColor >> 24 & 0xFF) == 0
        && (this.backgroundImage == null || this.backgroundMode != BackgroundMode.STRETCH)) {
      graphics.drawOutlineRect(this.x, this.y, this.width, this.height, this.border,
          this.borderColor);
    }
  }

  // --- PROPAGATED INPUT EVENTS ---

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
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

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (!isVisible() || !isEnabled()) {
      return false;
    }

    ensureInitialized();

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.charTyped(codePoint, modifiers)) {
        return true;
      }
    }
    return false;
  }
}
