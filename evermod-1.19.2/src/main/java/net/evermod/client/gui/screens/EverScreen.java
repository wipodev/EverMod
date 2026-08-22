package net.evermod.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.evermod.client.graphics.EverGraphics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Native Minecraft 1.19.2 Screen implementation of IEverScreen.
 * Adapts Mojang's PoseStack rendering pipeline and event listeners to EverUI.
 *
 * @author Wipodev
 */
public abstract class EverScreen extends Screen implements IEverScreen {

  protected final Screen parentScreen;
  protected final RootContainer rootContainer;

  protected EverScreen(Screen parentScreen, Component title) {
    super(title);
    this.parentScreen = parentScreen;
    this.rootContainer = new RootContainer();
  }

  protected EverScreen(Screen parentScreen, String title) {
    this(parentScreen, Component.literal(title));
  }

  protected EverScreen(Component title) {
    this(null, title);
  }

  protected EverScreen(String title) {
    this(null, Component.literal(title));
  }

  protected EverScreen() {
    this(Minecraft.getInstance().screen, Component.empty());
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
  protected void init() {
    super.init();
    this.initEverScreen(this.width, this.height);
  }

  @Override
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    this.renderBackground(poseStack);

    EverGraphics graphics = EverGraphics.of(poseStack);
    this.renderEverScreen(graphics, mouseX, mouseY, partialTick);

    super.render(poseStack, mouseX, mouseY, partialTick);
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

  @Override
  public void onClose() {
    if (this.parentScreen != null && this.minecraft != null) {
      this.minecraft.setScreen(this.parentScreen);
    } else {
      super.onClose();
    }
  }

  public int getCenterX() {
    return this.width / 2;
  }

  public int getCenterY() {
    return this.height / 2;
  }
}
