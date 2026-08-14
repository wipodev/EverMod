package net.evermod.client.gui.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;

/**
 * Version-agnostic font rendering interface.
 * Abstracts font measurements and text drawing pipelines across Minecraft versions.
 *
 * @author Wipodev
 */
public interface IEverFont {

  /**
   * Draws a plain string at the specified coordinates.
   */
  void drawString(PoseStack poseStack, String text, float x, float y, int color, boolean shadow);

  /**
   * Draws a formatted Component at the specified coordinates.
   */
  void drawString(PoseStack poseStack, Component component, float x, float y, int color,
      boolean shadow);

  /**
   * Calculates the width in pixels of a given plain string.
   */
  int width(String text);

  /**
   * Calculates the width in pixels of a formatted Component.
   */
  int width(Component component);

  /**
   * Gets the line height of the font in pixels (typically 9 in vanilla Minecraft).
   */
  int fontHeight();

  /**
   * Truncates a string so that its total rendered width does not exceed the maximum allowed width.
   *
   * @param text     Target string to trim.
   * @param maxWidth Maximum width in pixels.
   * @return Substring that fits within specified pixel constraint.
   */
  String plainSubstrByWidth(String text, int maxWidth);

  /**
   * Truncates a string so that its total rendered width does not exceed the maximum allowed width.
   *
   * @param text     Target string to trim.
   * @param maxWidth Maximum width in pixels.
   * @param reverse  If true, trims from right-to-left.
   * @return Substring that fits within specified pixel constraint.
   */
  String plainSubstrByWidth(String text, int maxWidth, boolean reverse);
}
