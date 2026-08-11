package net.evermod.client.gui.widget;

import net.evermod.client.gui.Border;
import net.evermod.client.gui.BorderColor;
import net.evermod.client.gui.EverGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

/**
 * Texture-based implementation of {@link AbstractButton} using Minecraft {@link ResourceLocation} assets.
 * Supports individual side border colors via {@link BorderColor}.
 *
 * @author Wipodev
 */
public class Button extends AbstractButton {

  private ResourceLocation backgroundImage;
  private ResourceLocation hoverBackgroundImage;
  private ResourceLocation disabledBackgroundImage;
  private int textureWidth = 198;
  private int textureHeight = 18;

  /**
   * Constructs a Button with a primary background texture at origin (0, 0) and default size (150x20).
   *
   * @param image ResourceLocation pointing to the default background texture.
   */
  public Button(ResourceLocation image) {
    super();
    this.backgroundImage = image;
  }

  /**
   * Constructs a Button with a primary background texture and plain text label.
   *
   * @param image ResourceLocation pointing to the default background texture.
   * @param text  Text string to display on the button label.
   */
  public Button(ResourceLocation image, String text) {
    this(image);
    setText(text);
  }

  /**
   * Constructs a Button with a primary background texture and a styled Component label.
   *
   * @param image     ResourceLocation pointing to the default background texture.
   * @param component Text Component to display on the button label.
   */
  public Button(ResourceLocation image, Component component) {
    this(image);
    setComponent(component);
  }

  /**
   * Sets alternative background textures for hover and disabled component states.
   *
   * @param hover    Texture resource location used when mouse cursor hovers over the button.
   * @param disabled Texture resource location used when the button is disabled.
   * @return This button instance for method chaining.
   */
  public Button setBackgroundImages(ResourceLocation hover, ResourceLocation disabled) {
    this.hoverBackgroundImage = hover;
    this.disabledBackgroundImage = disabled;
    return this;
  }

  /**
   * Fluent API alias for {@link #setBackgroundImages(ResourceLocation, ResourceLocation)}.
   *
   * @param hover    Texture resource location used when mouse cursor hovers over the button.
   * @param disabled Texture resource location used when the button is disabled.
   * @return This button instance for method chaining.
   */
  public Button backgroundImages(ResourceLocation hover, ResourceLocation disabled) {
    return setBackgroundImages(hover, disabled);
  }

  /**
   * Sets the source texture dimensions in pixels for UV mapping operations.
   *
   * @param width  Source texture sheet width in pixels.
   * @param height Source texture sheet height in pixels.
   * @return This button instance for method chaining.
   */
  public Button setTextureSize(int width, int height) {
    this.textureWidth = width;
    this.textureHeight = height;
    return this;
  }

  /**
   * Fluent API alias for {@link #setTextureSize(int, int)}.
   *
   * @param width  Source texture sheet width in pixels.
   * @param height Source texture sheet height in pixels.
   * @return This button instance for method chaining.
   */
  public Button textureSize(int width, int height) {
    return setTextureSize(width, height);
  }

  @Override
  protected void renderBackground(EverGraphics graphics, int mouseX, int mouseY, boolean hovered) {
    ResourceLocation activeTexture = !this.enabled ? this.disabledBackgroundImage
        : (hovered && this.hoverBackgroundImage != null ? this.hoverBackgroundImage
            : this.backgroundImage);

    if (activeTexture == null) {
      return;
    }

    BorderColor activeBorderColor = getActiveBorderColor(hovered);

    if (this.border != null && this.border != Border.NONE && activeBorderColor != null) {
      graphics.drawBorderTexture(activeTexture, this.x, this.y, this.width, this.height,
          this.textureWidth, this.textureHeight, this.border, activeBorderColor);
    } else {
      graphics.drawTexture(activeTexture, this.x, this.y, this.width, this.height,
          this.textureWidth, this.textureHeight);
    }
  }
}
