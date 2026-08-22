package net.evermod.client.gui.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import net.evermod.client.graphics.EverGraphics;
import net.evermod.client.gui.api.Container;
import net.evermod.client.gui.api.Interactive;
import net.evermod.client.gui.api.OverlayProvider;
import net.evermod.client.gui.api.Renderable;
import net.evermod.client.gui.api.TooltipProvider;
import net.evermod.client.gui.api.style.Alignable;
import net.evermod.client.gui.layout.LayoutAlignment;

/**
 * Abstract base class for UI containers.
 * Manages child hierarchy, layouts, event propagation, and local space rendering.
 *
 * @param <T> Concrete container subtype for fluent method chaining.
 */
public abstract class AbstractContainer<T extends AbstractContainer<T>>
    extends UIElement<T>
    implements Container, Renderable, Interactive, Alignable<T> {

  // ==========================================
  // Fields: Children & Focus
  // ==========================================
  protected final List<UINode> children = new ArrayList<>();
  protected UINode focusedChild = null;
  protected boolean initialized = false;

  protected LayoutAlignment alignment = LayoutAlignment.START;
  protected int gap = 0;

  public AbstractContainer(int x, int y, int width, int height) {
    super(x, y, width, height);
  }

  public AbstractContainer() {
    super(0, 0, 0, 0);
  }

  @SuppressWarnings("unchecked")
  protected T self() {
    return (T) this;
  }

  // ==========================================
  // Lifecycle & Container Management
  // ==========================================
  /**
   * Called during initialization to populate or configure child elements.
   */
  protected void build() {}

  /**
   * Ensures that build() is invoked prior to performing rendering or input operations.
   */
  protected void ensureInitialized() {
    if (!this.initialized) {
      this.initialized = true;
      build();
    }
  }

  @Override
  public <C extends UINode> C addChild(C child) {
    if (child != null && !this.children.contains(child)) {
      child.setParent(this);
      this.children.add(child);
    }
    return child;
  }

  @Override
  public boolean removeChild(UINode child) {
    if (child == null) {
      return false;
    }
    if (this.focusedChild == child) {
      this.focusedChild = null;
    }
    boolean removed = this.children.remove(child);
    if (removed) {
      child.setParent(null);
    }
    return removed;
  }

  @Override
  public void clearChildren() {
    for (UINode child : this.children) {
      child.setParent(null);
    }
    this.children.clear();
    this.focusedChild = null;
  }

  @Override
  public List<UINode> getChildren() {
    return Collections.unmodifiableList(this.children);
  }

  @Override
  public Optional<UINode> findChildById(String id) {
    return Optional.empty();
  }

  // ==========================================
  // Rendering Pipeline
  // ==========================================
  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    if (!this.visible) {
      return;
    }
    ensureInitialized();

    graphics.push();
    graphics.translate(this.x, this.y, 0.0F);

    this.renderBackground(graphics, mouseX, mouseY, partialTicks);
    this.renderChildren(graphics, mouseX, mouseY, partialTicks);

    graphics.pop();

    this.renderOverlayPass(graphics, mouseX, mouseY);
    this.renderTooltipPass(graphics, mouseX, mouseY);
  }

  protected void renderChildren(
      EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    for (UINode child : this.children) {
      if (child.isVisible() && child instanceof Renderable renderable) {
        renderable.render(graphics, mouseX, mouseY, partialTicks);
      }
    }
  }

  public void renderOverlayPass(EverGraphics graphics, int mouseX, int mouseY) {
    for (UINode child : this.children) {
      if (child.isVisible()) {
        if (child instanceof OverlayProvider provider && provider.isOverlayActive()) {
          provider.renderOverlay(graphics, mouseX, mouseY);
        }
        if (child instanceof AbstractContainer<?> parentChild) {
          parentChild.renderOverlayPass(graphics, mouseX, mouseY);
        }
      }
    }
  }

  public void renderTooltipPass(EverGraphics graphics, int mouseX, int mouseY) {
    for (UINode child : this.children) {
      if (child.isVisible()) {
        if (child instanceof TooltipProvider provider
            && provider.isTooltipActive(mouseX, mouseY)) {
          provider.renderTooltip(graphics, mouseX, mouseY);
        }
        if (child instanceof AbstractContainer<?> parentChild) {
          parentChild.renderTooltipPass(graphics, mouseX, mouseY);
        }
      }
    }
  }

  // ==========================================
  // Interface: Interactive
  // ==========================================
  @Override
  public boolean isHovered(double pointX, double pointY) {
    return this.canInteract() && this.containsPoint(pointX, pointY);
  }

  @Override
  public boolean isFocused() {
    return this.focusedChild != null;
  }

  @Override
  public void setFocused(boolean focused) {
    if (!focused) {
      this.focusedChild = null;
    }
  }

  @Override
  public void mouseMoved(double mouseX, double mouseY) {
    if (!this.visible || !this.enabled) {
      return;
    }
    ensureInitialized();

    double localX = mouseX - this.x;
    double localY = mouseY - this.y;

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        ((Interactive) child).mouseMoved(localX, localY);
      }
    }
  }

  @Override
  public boolean mouseClicked(double mouseX, double mouseY, int button) {
    if (!this.visible || !this.enabled) {
      return false;
    }
    ensureInitialized();

    double localX = mouseX - this.x;
    double localY = mouseY - this.y;

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        Interactive interactive = (Interactive) child;
        if (interactive.mouseClicked(localX, localY, button)) {
          this.focusedChild = child;
          return true;
        }
      }
    }
    this.focusedChild = null;
    return false;
  }

  @Override
  public boolean mouseReleased(double mouseX, double mouseY, int button) {
    if (!this.visible || !this.enabled) {
      return false;
    }
    ensureInitialized();

    double localX = mouseX - this.x;
    double localY = mouseY - this.y;
    boolean handled = false;

    if (this.focusedChild instanceof Interactive interactive) {
      handled = interactive.mouseReleased(localX, localY, button);
      this.focusedChild = null;
    }

    if (handled) {
      return true;
    }

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        if (((Interactive) child).mouseReleased(localX, localY, button)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean mouseDragged(
      double mouseX, double mouseY, int button, double dragX, double dragY) {
    if (!this.visible || !this.enabled) {
      return false;
    }
    ensureInitialized();

    double localX = mouseX - this.x;
    double localY = mouseY - this.y;

    if (this.focusedChild instanceof Interactive interactive) {
      if (interactive.mouseDragged(localX, localY, button, dragX, dragY)) {
        return true;
      }
    }

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        if (((Interactive) child).mouseDragged(localX, localY, button, dragX, dragY)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
    return mouseScrolled(mouseX, mouseY, 0.0D, delta);
  }

  @Override
  public boolean mouseScrolled(double mouseX, double mouseY, double deltaX, double deltaY) {
    if (!this.visible || !this.enabled) {
      return false;
    }
    ensureInitialized();

    double localX = mouseX - this.x;
    double localY = mouseY - this.y;

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        if (((Interactive) child).mouseScrolled(localX, localY, deltaX, deltaY)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
    if (!this.visible || !this.enabled) {
      return false;
    }
    ensureInitialized();

    if (this.focusedChild instanceof Interactive interactive) {
      if (interactive.keyPressed(keyCode, scanCode, modifiers)) {
        return true;
      }
    }

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        if (((Interactive) child).keyPressed(keyCode, scanCode, modifiers)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
    if (!this.visible || !this.enabled) {
      return false;
    }
    ensureInitialized();

    if (this.focusedChild instanceof Interactive interactive) {
      if (interactive.keyReleased(keyCode, scanCode, modifiers)) {
        return true;
      }
    }

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        if (((Interactive) child).keyReleased(keyCode, scanCode, modifiers)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public boolean charTyped(char codePoint, int modifiers) {
    if (!this.visible || !this.enabled) {
      return false;
    }
    ensureInitialized();

    if (this.focusedChild instanceof Interactive interactive) {
      if (interactive.charTyped(codePoint, modifiers)) {
        return true;
      }
    }

    for (int i = this.children.size() - 1; i >= 0; i--) {
      UINode child = this.children.get(i);
      if (child.canInteract()) {
        if (((Interactive) child).charTyped(codePoint, modifiers)) {
          return true;
        }
      }
    }
    return false;
  }

  @Override
  public T align(LayoutAlignment alignment) {
    this.alignment = alignment;
    return self();
  }

  @Override
  public T gap(int pixels) {
    this.gap = pixels;
    return self();
  }

  @Override
  public LayoutAlignment getAlignment() {
    return alignment;
  }

  @Override
  public int getGap() {
    return gap;
  }

  public void updateLayout() {}
}
