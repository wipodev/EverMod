package net.evermod.client.gui.api.style;

import net.minecraft.resources.ResourceLocation;

/**
 * Interface defining background styling capabilities (solid colors, state colors, and textures).
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface Backgroundable<T extends Backgroundable<T>> {

  // --- SOLID COLOR BACKGROUNDS ---

  /**
   * Sets a static background color for all states.
   *
   * @param color Primary background color in ARGB format.
   * @return This element instance for method chaining.
   */
  T background(int color);

  // --- TEXTURED BACKGROUNDS ---

  /**
   * Sets a texture as the background with a tint color.
   *
   * @param texture   The texture resource location.
   * @param tintColor ARGB tint color to apply to the texture.
   * @return This element instance for method chaining.
   */
  T background(ResourceLocation texture, int tintColor);

  /**
   * Sets a texture as the background without tinting (default white tint).
   *
   * @param texture The texture resource location.
   * @return This element instance for method chaining.
   */
  default T background(ResourceLocation texture) {
    return background(texture, 0xFFFFFFFF);
  }

  // --- GETTERS ---

  /**
   * Gets the primary background color.
   *
   * @return ARGB color integer.
   */
  int getBackgroundColor();

  /**
   * Gets the background texture resource location, if set.
   *
   * @return The texture resource location, or {@code null} if none is set.
   */
  ResourceLocation getBackgroundTexture();
}
