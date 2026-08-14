package net.evermod.client.gui.screens.inventory;

import net.evermod.client.gui.EverGraphics;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Native Minecraft 1.20+ AbstractContainerScreen implementation of IEverContainerScreen.
 * Bridges Mojang's GuiGraphics pipeline and slot handling to EverUI.
 *
 * @param <T> Container menu type.
 * @author Wipodev
 */
public abstract class EverContainerScreen<T extends AbstractContainerMenu>
    extends AbstractContainerScreen<T> implements IEverContainerScreen<T> {

  private GuiGraphics currentGuiGraphics;
  protected final RootContainer rootContainer;

  public EverContainerScreen(T menu, Inventory playerInventory, Component title) {
    super(menu, playerInventory, title);
    this.rootContainer = new RootContainer();
  }

  @Override
  public RootContainer getRootContainer() {
    return this.rootContainer;
  }

  @Override
  public Component getTitleComponent() {
    return this.getTitle();
  }

  @Override
  public T getMenuInstance() {
    return this.menu;
  }

  @Override
  protected void init() {
    super.init();
    this.initEverScreen(this.width, this.height);
  }

  @Override
  public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
    this.currentGuiGraphics = guiGraphics;
    super.render(guiGraphics, mouseX, mouseY, partialTick);
    this.currentGuiGraphics = null;

    EverGraphics graphics = EverGraphics.of(guiGraphics.pose());
    this.renderEverScreen(graphics, mouseX, mouseY, partialTick);

    this.renderTooltip(guiGraphics, mouseX, mouseY);
  }

  @Override
  protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
    EverGraphics graphics = EverGraphics.of(guiGraphics.pose());
    this.renderContainerBackground(graphics, partialTick, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
    EverGraphics graphics = EverGraphics.of(guiGraphics.pose());
    this.renderContainerLabels(graphics, mouseX, mouseY);
  }

  @Override
  public void renderEntityInInventory(int x, int y, int scale, float mouseX, float mouseY,
      LivingEntity entity) {
    if (this.currentGuiGraphics == null) {
      return;
    }
    int boxWidth = scale * 2;
    int boxHeight = scale * 3;
    int x1 = x - (boxWidth / 2);
    int y1 = y - boxHeight;
    int x2 = x + (boxWidth / 2);
    int y2 = y;
    float yOffset = 0.0F;

    InventoryScreen.renderEntityInInventoryFollowsMouse(this.currentGuiGraphics, x1, y1, x2, y2,
        scale, yOffset, mouseX, mouseY, entity);
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.handleMouseClicked(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (this.handleMouseReleased(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    if (this.handleMouseDragged(mouseX, mouseY, button, dragX, dragY)) {
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
    if (this.handleMouseScrolled(mouseX, mouseY, scrollX, scrollY)) {
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (this.handleKeyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (this.handleCharTyped(codePoint, modifiers)) {
      return true;
    }
    return super.charTyped(codePoint, modifiers);
  }
}
