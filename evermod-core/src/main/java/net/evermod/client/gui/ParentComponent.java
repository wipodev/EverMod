package net.evermod.client.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.resources.ResourceLocation;

/**
 * Abstract base class for UI components that can contain and manage child components.
 * Propagates rendering, bounds checks, and user input events down the component hierarchy.
 * Supports multi-colored border configurations using {@link BorderColor}.
 *
 * @author Wipodev
 */
public abstract class ParentComponent extends AbstractComponent {

  protected final List<UIComponent> children = new ArrayList<>();
  protected UIComponent focusedChild = null;

  protected int backgroundColor = 0x00000000;
  protected ResourceLocation backgroundImage = null;
  protected Border border = Border.NONE;
  protected BorderColor borderColor = BorderColor.DEFAULT;
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
   * Sets custom border configuration and multi-colored border.
   *
   * @param border      Custom border structure containing edge thicknesses.
   * @param borderColor {@link BorderColor} structure containing ARGB colors per side.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(Border border, BorderColor borderColor) {
    this.border = border;
    this.borderColor = borderColor;
    return this;
  }

  /**
   * Sets custom border configuration and uniform border color.
   *
   * @param border      Custom border structure containing edge thicknesses.
   * @param borderColor ARGB hex color code for the border.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(Border border, int borderColor) {
    return border(border, BorderColor.all(borderColor));
  }

  /**
   * Sets a uniform border of the specified thickness and side colors.
   *
   * @param thickness   Uniform thickness in pixels for all sides.
   * @param borderColor {@link BorderColor} structure containing ARGB colors per side.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(int thickness, BorderColor borderColor) {
    return border(Border.all(thickness), borderColor);
  }

  /**
   * Sets a uniform border of the specified thickness and color.
   *
   * @param thickness   Uniform thickness in pixels for all sides.
   * @param borderColor ARGB hex color code for the border.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(int thickness, int borderColor) {
    return border(Border.all(thickness), BorderColor.all(borderColor));
  }

  /**
   * Sets a standard 1px border with the specified side colors.
   *
   * @param borderColor {@link BorderColor} structure containing ARGB colors per side.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(BorderColor borderColor) {
    return border(Border.DEFAULT, borderColor);
  }

  /**
   * Sets a standard 1px border with the specified color.
   *
   * @param borderColor ARGB hex color code for the border.
   * @return This container instance for method chaining.
   */
  public ParentComponent border(int borderColor) {
    return border(Border.DEFAULT, BorderColor.all(borderColor));
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
   * Gets the active multi-colored border structure.
   *
   * @return Active {@link BorderColor}.
   */
  public BorderColor getBorderColor() {
    return this.borderColor;
  }

  // --- BACKGROUND FLUENT SETTERS ---

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

  /**
   * Configures background texture.
   *
   * @param texture ResourceLocation of the texture.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture) {
    this.backgroundImage = texture;
    return this;
  }

  /**
   * Alias for {@link #background(ResourceLocation)}.
   *
   * @param texture ResourceLocation of the texture.
   * @return This container instance for method chaining.
   */
  public ParentComponent backgroundImage(ResourceLocation texture) {
    return background(texture);
  }

  /**
   * Configures full background properties including texture, dimensions, mode, and color.
   *
   * @param texture       ResourceLocation of the texture.
   * @param ARGBColor     Solid background ARGB tint or color.
   * @return This container instance for method chaining.
   */
  public ParentComponent background(ResourceLocation texture, int ARGBColor) {
    this.backgroundImage = texture;
    this.backgroundColor = ARGBColor;
    return this;
  }

  /**
   * Gets the current ARGB background color.
   *
   * @return ARGB hex color code.
   */
  public int getBackgroundColor() {
    return this.backgroundColor;
  }

  public ResourceLocation getBackgroundImage() {
    return this.backgroundImage;
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
    if (this.focusedChild == child) {
      this.focusedChild = null;
    }
    return this.children.remove(child);
  }

  /**
   * Clears all child components from this container.
   */
  public void clearChildren() {
    this.children.clear();
    this.focusedChild = null;
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

    for (UIComponent child : this.children) {
      if (child.isVisible()) {
        child.render(graphics, mouseX, mouseY, partialTicks);
      }
    }

    renderOverlayPass(graphics, mouseX, mouseY);
  }

  /**
   * Propagates overlay rendering across the component tree.
   *
   * @param graphics Canvas graphics context.
   * @param mouseX   Current cursor X position.
   * @param mouseY   Current cursor Y position.
   */
  public void renderOverlayPass(EverGraphics graphics, int mouseX, int mouseY) {
    for (UIComponent child : this.children) {
      if (!child.isVisible()) {
        continue;
      }

      // Render overlay if the child supports it and is currently active
      if (child instanceof OverlayProvider provider && provider.isOverlayActive()) {
        provider.renderOverlay(graphics, mouseX, mouseY);
      }

      // Propagate pass down to nested containers
      if (child instanceof ParentComponent parentChild) {
        parentChild.renderOverlayPass(graphics, mouseX, mouseY);
      }
    }
  }

  /**
   * Renders container background color, texture image, and overlay borders using EverGraphics.
   *
   * @param graphics     Custom graphic context.
   * @param mouseX       Current mouse cursor X.
   * @param mouseY       Current mouse cursor Y.
   * @param partialTicks Render partial tick delta.
   */
  protected void renderBackground(EverGraphics graphics, int mouseX, int mouseY,
      float partialTicks) {
    boolean hasBorder =
        this.border != null && this.border != Border.NONE && this.borderColor != null;
    boolean hasSolidColor = (this.backgroundColor >> 24 & 0xFF) > 0;

    if (hasSolidColor) {
      if (hasBorder) {
        graphics.drawRect(this.x, this.y, this.width, this.height, this.backgroundColor,
            this.border, this.borderColor);
      } else {
        graphics.drawRect(this.x, this.y, this.width, this.height, this.backgroundColor);
      }
    }

    if (this.backgroundImage != null) {
      if (hasBorder) {
        graphics.drawTexture(this.backgroundImage, this.x, this.y, this.width, this.height,
            this.border, this.borderColor);
      } else {
        graphics.drawTexture(this.backgroundImage, this.x, this.y, this.width, this.height);
      }
    }

    if (hasBorder && !hasSolidColor && this.backgroundImage == null) {
      graphics.fillBorder(this.x, this.y, this.width, this.height, this.border, this.borderColor);
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

    // Iterate backwards so components drawn on top receive clicks first
    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.mouseClicked(mouseX, mouseY, button)) {
        this.focusedChild = child; // Track active child for subsequent drag events
        return true;
      }
    }

    this.focusedChild = null;
    return false;
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    if (!isVisible() || !isEnabled()) {
      return false;
    }

    ensureInitialized();

    // Direct drag events directly to active focused child first
    if (this.focusedChild != null) {
      if (this.focusedChild.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
        return true;
      }
    }

    // Fallback iteration through children in reverse rendering order
    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
        return true;
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

    boolean handled = false;

    // Release focused child first
    if (this.focusedChild != null) {
      handled = this.focusedChild.mouseReleased(mouseX, mouseY, button);
      this.focusedChild = null;
    }

    if (handled) {
      return true;
    }

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.mouseReleased(mouseX, mouseY, button)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Propagates dual-axis mouse scroll events to child components in reverse rendering order.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @param deltaX Horizontal scroll delta.
   * @param deltaY Vertical scroll delta.
   * @return {@code true} if consumed by any child component.
   */
  public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
    if (!isVisible() || !isEnabled()) {
      return false;
    }

    ensureInitialized();

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UIComponent child = this.children.get(i);
      if (child.mouseScrolled(mouseX, mouseY, deltaX, deltaY)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Legacy single-axis vertical scroll handler for backward compatibility.
   *
   * @param mouseX Cursor X position.
   * @param mouseY Cursor Y position.
   * @param delta Vertical scroll delta.
   * @return {@code true} if consumed by any child component.
   */
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    return this.mouseScrolled(mouseX, mouseY, 0.0D, delta);
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
