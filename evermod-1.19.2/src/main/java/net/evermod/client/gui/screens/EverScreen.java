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
 * Serves as the high-level assembler and wrapper over Mojang's native {@link Screen} class.
 * Delegates layout rendering, sizing, and input handling to autonomous UI components.
 *
 * <p>To create a custom screen, extend this class and override the {@link #setupUI()} method
 * to compose layouts and widgets using {@link #add(UIComponent)}.</p>
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
   * Constructs an EverScreen with a parent screen and localized Component title.
   *
   * @param parentScreen Previous screen to return to upon exit or navigation back.
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
   * @param parentScreen Previous screen to return to upon exit or navigation back.
   * @param title Literal string title.
   */
  protected EverScreen(Screen parentScreen, String title) {
    this(parentScreen, Component.literal(title));
  }

  /**
   * Constructs an EverScreen without a parent screen using a localized Component title.
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
   * Default constructor using the currently open screen as the parent screen.
   */
  protected EverScreen() {
    this(Minecraft.getInstance().screen, Component.empty());
  }

  // --- LIFECYCLE METHOD ---

  /**
   * Initializes screen dimensions, resets the root container viewport, and invokes {@link #setupUI()}.
   * <p>This method is automatically called by Minecraft whenever the window is resized or opened.</p>
   */
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
   * Subclasses must override this method to declare the screen layout and widgets.
   */
  protected abstract void setupUI();

  /**
   * Mounts an autonomous UI component directly into the screen root container.
   *
   * @param component Component or layout to add to the screen hierarchy.
   */
  public void add(UIComponent component) {
    this.rootContainer.addChild(component);
  }

  // --- RENDERING PIPELINE ---

  /**
   * Renders the screen background, root container hierarchy, and vanilla Minecraft widgets.
   *
   * @param poseStack Current rendering matrix stack.
   * @param mouseX Current cursor X coordinate.
   * @param mouseY Current cursor Y coordinate.
   * @param partialTick Partial tick time for smooth animations.
   */
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

  /**
   * Propagates mouse click events to the root container.
   *
   * @param mouseX Cursor X coordinate.
   * @param mouseY Cursor Y coordinate.
   * @param button Mouse button index (0 for left, 1 for right, 2 for middle).
   * @return {@code true} if the event was consumed by a child component, {@code false} otherwise.
   */
  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (this.rootContainer.mouseClicked(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseClicked(mouseX, mouseY, button);
  }

  /**
   * Propagates mouse release events to the root container.
   *
   * @param mouseX Cursor X coordinate.
   * @param mouseY Cursor Y coordinate.
   * @param button Mouse button index.
   * @return {@code true} if the event was consumed by a child component, {@code false} otherwise.
   */
  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (this.rootContainer.mouseReleased(mouseX, mouseY, button)) {
      return true;
    }
    return super.mouseReleased(mouseX, mouseY, button);
  }

  /**
   * Propagates mouse drag events to the root container.
   *
   * @param mouseX Cursor X coordinate.
   * @param mouseY Cursor Y coordinate.
   * @param button Mouse button index being held.
   * @param dragX Delta movement along the X axis.
   * @param dragY Delta movement along the Y axis.
   * @return {@code true} if the event was consumed by a child component, {@code false} otherwise.
   */
  @Override
  public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX,
      double dragY) {
    if (this.rootContainer.mouseDragged(mouseX, mouseY, button, dragX, dragY)) {
      return true;
    }
    return super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
  }

  /**
   * Propagates mouse scroll wheel events to the root container.
   *
   * @param mouseX Cursor X coordinate.
   * @param mouseY Cursor Y coordinate.
   * @param delta Scroll movement magnitude and direction.
   * @return {@code true} if the event was consumed by a child component, {@code false} otherwise.
   */
  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    if (this.rootContainer.mouseScrolled(mouseX, mouseY, delta)) {
      return true;
    }
    return super.mouseScrolled(mouseX, mouseY, delta);
  }

  /**
   * Propagates key press events to the root container.
   *
   * @param keyCode Key code corresponding to the pressed key.
   * @param scanCode Platform-specific scan code.
   * @param modifiers Key modifier bitmask (e.g., Shift, Control, Alt).
   * @return {@code true} if the event was consumed by a child component, {@code false} otherwise.
   */
  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (this.rootContainer.keyPressed(keyCode, scanCode, modifiers)) {
      return true;
    }
    return super.keyPressed(keyCode, scanCode, modifiers);
  }

  /**
   * Propagates typed character input events to the root container.
   *
   * @param codePoint The typed character Unicode code point.
   * @param modifiers Key modifier bitmask.
   * @return {@code true} if the event was consumed by a child component, {@code false} otherwise.
   */
  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (this.rootContainer.charTyped(codePoint, modifiers)) {
      return true;
    }
    return super.charTyped(codePoint, modifiers);
  }

  // --- UTILITIES ---

  /**
   * Calculates the horizontal center coordinate of the screen viewport.
   *
   * @return Half of current screen width.
   */
  public int getCenterX() {
    return this.width / 2;
  }

  /**
   * Calculates the vertical center coordinate of the screen viewport.
   *
   * @return Half of current screen height.
   */
  public int getCenterY() {
    return this.height / 2;
  }

  /**
   * Navigates back to the registered {@link #parentScreen} if available.
   */
  public void back() {
    if (this.minecraft != null) {
      this.minecraft.setScreen(this.parentScreen);
    }
  }

  // --- INTERNAL ROOT CONTAINER ---

  /**
   * Concrete root container delegate responsible for holding and managing top-level components in an {@link EverScreen}.
   */
  protected static class RootContainer extends ParentComponent {

    /**
     * Constructs a default RootContainer instance.
     */
    public RootContainer() {
      super();
    }
  }
}
