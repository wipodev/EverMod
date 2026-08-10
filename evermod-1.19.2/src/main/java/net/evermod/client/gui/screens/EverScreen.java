package net.evermod.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import net.evermod.client.gui.EverGraphics;
import net.evermod.client.gui.ParentComponent;
import net.evermod.client.gui.UIComponent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

/**
 * Pure version-agnostic base screen assembler for EverMod GUIs in Minecraft.
 * Serves as the high-level assembler and wrapper over Mojang's native Screen class.
 * Delegates layout rendering, sizing, and input handling to autonomous UI components.
 *
 * @author Wipodev
 */
public abstract class EverScreen extends Screen {

  /** Parent screen instance for backwards navigation. */
  protected final Screen parentScreen;

  /** Root component container hosting all mounted screen modules. */
  protected final RootContainer rootContainer;

  // --- CONSTRUCTORS ---

  /**
   * Constructs an EverScreen with a parent screen and Component title.
   *
   * @param parentScreen Previous screen to return to upon exit.
   * @param title Localized Component title.
   */
  protected EverScreen(Screen parentScreen, Component title) {
    super(title);
    this.parentScreen = parentScreen;
    this.rootContainer = new RootContainer();
  }

  /**
   * Constructs an EverScreen with a parent screen and literal String title.
   *
   * @param parentScreen Previous screen to return to upon exit.
   * @param title Literal string title.
   */
  protected EverScreen(Screen parentScreen, String title) {
    this(parentScreen, Component.literal(title));
  }

  /**
   * Constructs an EverScreen without a parent screen.
   *
   * @param title Localized Component title.
   */
  protected EverScreen(Component title) {
    this(null, title);
  }

  /**
   * Constructs an EverScreen without a parent screen using a literal String title.
   *
   * @param title Literal string title.
   */
  protected EverScreen(String title) {
    this(null, Component.literal(title));
  }

  /**
   * Default constructor using the currently open screen as the parent.
   */
  protected EverScreen() {
    this(Minecraft.getInstance().screen, Component.empty());
  }

  // --- LIFECYCLE METHOD ---

  @Override
  protected final void init() {
    super.init();

    // Match root container dimensions with active screen viewport
    this.rootContainer.setX(0);
    this.rootContainer.setY(0);
    this.rootContainer.setWidth(this.width);
    this.rootContainer.setHeight(this.height);
    this.rootContainer.clearChildren();

    // Invoke user component mounting lifecycle
    this.setupUI();
  }

  /**
   * Setups and mounts autonomous components into the screen.
   * Subclasses must override this method to compose the screen structure.
   */
  protected abstract void setupUI();

  /**
   * Mounts an autonomous UI component directly into the screen root container.
   *
   * @param component Component or layout to add.
   */
  public void add(UIComponent component) {
    this.rootContainer.addChild(component);
  }

  // --- RENDERING PIPELINE ---

  @Override
  public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
    // 1. Wrap native context into EverGraphics engine wrapper
    EverGraphics graphics = EverGraphics.of(poseStack);

    // 2. Render standard Minecraft background overlay
    this.renderBackground(poseStack);

    // 3. Render mounted EverUI autonomous components hierarchy
    this.rootContainer.render(graphics, mouseX, mouseY, partialTick);

    // 4. Render native Minecraft widgets if attached
    super.render(poseStack, mouseX, mouseY, partialTick);
  }

  // --- INPUT PROPAGATION ---

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.rootContainer.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (this.rootContainer.mouseReleased(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    if (this.rootContainer.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    if (this.rootContainer.mouseScrolled(mouseX, mouseY, delta)) {
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, delta);
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (this.rootContainer.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (this.rootContainer.charTyped(codePoint, modifiers)) {
      return true;
    }
    return super.charTyped(codePoint, modifiers);
  }

  // --- UTILITIES ---

  /**
   * Calculates the horizontal center coordinate of the screen.
   *
   * @return Half of current screen width.
   */
  public int getCenterX() {
    return this.width / 2;
  }

  /**
   * Calculates the vertical center coordinate of the screen.
   *
   * @return Half of current screen height.
   */
  public int getCenterY() {
    return this.height / 2;
  }

  /**
   * Navigates back to the parent screen if available.
   */
  public void back() {
    if (this.minecraft != null) {
      this.minecraft.setScreen(this.parentScreen);
    }
  }

  // --- INTERNAL ROOT CONTAINER ---

  /**
   * Concrete root container delegate for EverScreen.
   */
  protected static class RootContainer extends ParentComponent {

    public RootContainer() {
      super();
    }
  }
}
