package net.evermod.client.gui.screens.inventory;

import com.mojang.blaze3d.vertex.PoseStack;
import net.evermod.client.graphics.EverGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Native Minecraft 1.19.2 AbstractContainerScreen implementation of IEverContainerScreen.
 * Bridges Mojang's container rendering and slot handling pipeline to EverUI.
 *
 * @param <T> Container menu type.
 * @author Wipodev
 */
public abstract class EverContainerScreen<T extends AbstractContainerMenu>
    extends AbstractContainerScreen<T> implements IEverContainerScreen<T> {

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
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(poseStack);
    super.render(poseStack, mouseX, mouseY, partialTick);

    EverGraphics graphics = EverGraphics.of(poseStack);
    this.renderEverScreen(graphics, mouseX, mouseY, partialTick);

    this.renderTooltip(poseStack, mouseX, mouseY);
  }

  @Override
  protected void renderBg(PoseStack poseStack, float partialTick, int mouseX, int mouseY) {
    EverGraphics graphics = EverGraphics.of(poseStack);
    this.renderContainerBackground(graphics, partialTick, mouseX, mouseY);
  }

  @Override
  protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
    EverGraphics graphics = EverGraphics.of(poseStack);
    this.renderContainerLabels(graphics, mouseX, mouseY);
  }

  @Override
  public void renderEntityInInventory(int x, int y, int scale, float mouseX, float mouseY,
      LivingEntity entity) {
    float eyeOffsetY = entity.getEyeHeight() * scale;
    float focalY = y - eyeOffsetY;

    InventoryScreen.renderEntityInInventory(x, y, scale, (float) x - mouseX,
        focalY - mouseY, entity);
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
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    if (this.handleMouseScrolled(mouseX, mouseY, 0.0D, delta)) {
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, delta);
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
