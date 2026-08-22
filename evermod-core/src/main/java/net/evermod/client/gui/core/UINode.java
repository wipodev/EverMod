package net.evermod.client.gui.core;

import net.evermod.client.gui.api.Interactive;

/**
 * Base spatial node representing positioning, hierarchy, bounds, and point hit testing.
 * Free from styling and rendering logic.
 * 
 * @author Wipodev
 */
public abstract class UINode {

  protected int x; // Relative X coordinate to parent
  protected int y; // Relative Y coordinate to parent
  protected int width;
  protected int height;
  protected boolean visible = true;
  protected boolean enabled = true;
  protected UINode parent;

  public UINode(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public UINode() {
    this(0, 0, 0, 0);
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  public void setBounds(int x, int y, int width, int height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  public int getGlobalX() {
    return (parent != null) ? parent.getGlobalX() + this.x : this.x;
  }

  public int getGlobalY() {
    return (parent != null) ? parent.getGlobalY() + this.y : this.y;
  }

  public UINode getParent() {
    return parent;
  }

  public void setParent(UINode parent) {
    this.parent = parent;
  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean containsPoint(double pointX, double pointY) {
    int gx = getGlobalX();
    int gy = getGlobalY();
    return pointX >= gx && pointX < gx + this.width &&
        pointY >= gy && pointY < gy + this.height;
  }

  public boolean canInteract() {
    return this.visible && this.enabled && this instanceof Interactive;
  }
}
