package net.evermod.client.gui.screens.inventory;

import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.gui.screens.IEverScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Pure version-agnostic interface for EverMod container screen implementations.
 * Extends IEverScreen contract to support container menus and slot interactions.
 *
 * @param <T> Menu type bound to this screen.
 * @author Wipodev
 */
public interface IEverContainerScreen<T extends AbstractContainerMenu> extends IEverScreen {

  /**
   * Gets the underlying container menu instance.
   *
   * @return Menu instance.
   */
  T getMenuInstance();

  /**
   * Renders container-specific background elements (slots, textures).
   *
   * @param graphics Standardized EverGraphics context.
   * @param partialTick Render tick delta time.
   * @param mouseX Current mouse cursor X position.
   * @param mouseY Current mouse cursor Y position.
   */
  void renderContainerBackground(EverGraphics graphics, float partialTick, int mouseX, int mouseY);

  /**
   * Renders container-specific foreground labels and text overlays.
   *
   * @param graphics Standardized EverGraphics context.
   * @param mouseX Current mouse cursor X position.
   * @param mouseY Current mouse cursor Y position.
   */
  void renderContainerLabels(EverGraphics graphics, int mouseX, int mouseY);

  /**
   * Delegates native entity inventory rendering to version implementations.
   *
   * @param x Entity origin X position.
   * @param y Entity origin Y position.
   * @param scale Entity scale factor.
   * @param mouseX Cursor X for tracking angle calculations.
   * @param mouseY Cursor Y for tracking angle calculations.
   * @param entity Living entity instance to draw.
   */
  void renderEntityInInventory(int x, int y, int scale, float mouseX, float mouseY,
      LivingEntity entity);
}
