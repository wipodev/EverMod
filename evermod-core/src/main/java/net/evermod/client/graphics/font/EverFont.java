package net.evermod.client.graphics.font;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

/**
 * Version-agnostic font rendering interface.
 * Abstracts font measurements and text drawing pipelines across Minecraft versions.
 *
 * @author Wipodev
 */
public interface EverFont {

  /**
   * Draws a plain string at the specified coordinates.
   *
   * @param poseStack matrix stack for rendering transformations
   * @param text string content to render
   * @param x X screen position
   * @param y Y screen position
   * @param color ARGB color code
   * @param shadow whether to render text shadow
   */
  void drawString(PoseStack poseStack, String text, float x, float y, int color, boolean shadow);

  /**
   * Draws a formatted Component at the specified coordinates.
   *
   * @param poseStack matrix stack for rendering transformations
   * @param component text component to render
   * @param x X screen position
   * @param y Y screen position
   * @param color ARGB color code
   * @param shadow whether to render text shadow
   */
  void drawString(PoseStack poseStack, Component component, float x, float y, int color,
      boolean shadow);

  /**
   * Calculates the width in pixels of a given plain string.
   *
   * @param text input string
   * @return pixel width of the rendered string
   */
  int width(String text);

  /**
   * Calculates the width in pixels of a formatted Component.
   *
   * @param component input text component
   * @return pixel width of the rendered component
   */
  int width(Component component);

  /**
   * Gets the line height of the font in pixels (typically 9 in vanilla Minecraft).
   *
   * @return font height in pixels
   */
  int fontHeight();

  /**
   * Truncates a string so that its total rendered width does not exceed the maximum allowed width.
   *
   * @param text target string to trim
   * @param maxWidth maximum width in pixels
   * @return substring fitting within specified pixel constraint
   */
  String plainSubstrByWidth(String text, int maxWidth);

  /**
   * Truncates a string so that its total rendered width does not exceed the maximum allowed width.
   *
   * @param text target string to trim
   * @param maxWidth maximum width in pixels
   * @param reverse if true, trims from right-to-left
   * @return substring fitting within specified pixel constraint
   */
  String plainSubstrByWidth(String text, int maxWidth, boolean reverse);
}
