package net.evermod.client.gui.api.style;

import net.minecraft.resources.ResourceLocation;

/**
 * Interface defining typography and text-specific styling capabilities
 * (color, font size, custom font family/texture, and text alignment).
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface TextStyleable<T extends TextStyleable<T>> {

  // --- TEXT COLOR ---

  /**
   * Sets the primary text color (like CSS color property).
   *
   * @param argbColor ARGB color integer.
   * @return This element instance for method chaining.
   */
  T color(int argbColor);

  // --- TEXT SHADOW ---
  T fontShadow(boolean shadow);

  // --- FONT SIZE ---

  /**
   * Sets the font scale or size in pixels/units.
   *
   * @param size Target font size.
   * @return This element instance for method chaining.
   */
  T fontSize(float size);

  // --- FONT FAMILY / TYPEFACE ---

  /**
   * Sets a custom font family or font texture location.
   *
   * @param fontId ResourceLocation pointing to the custom font asset.
   * @return This element instance for method chaining.
   */
  T fontFamily(ResourceLocation fontId);

  // --- TEXT ALIGNMENT ---

  /**
   * Sets the alignment of the text relative to the component bounds (e.g., LEFT, CENTER, RIGHT).
   *
   * @param alignment Text alignment enum value.
   * @return This element instance for method chaining.
   */
  T textAlign(TextAlignment alignment);

  // --- GETTERS ---

  /**
   * Gets the primary text color.
   *
   * @return ARGB color integer.
   */
  int getColor();

  boolean getTextShadow();

  /**
   * Gets the current font size/scale.
   *
   * @return Font size value.
   */
  float getFontSize();

  /**
   * Gets the custom font resource location, or {@code null} if default Minecraft font is used.
   *
   * @return Font resource location.
   */
  ResourceLocation getFontFamily();

  /**
   * Gets the configured text alignment.
   *
   * @return Text alignment enum value.
   */
  TextAlignment getTextAlign();
}
