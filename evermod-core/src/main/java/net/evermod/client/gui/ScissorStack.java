package net.evermod.client.gui;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Manages a stack of nested scissor rectangles to prevent child UI elements
 * from spilling outside parent boundaries.
 *
 * @author Wipodev
 */
public class ScissorStack {

  private final Deque<ScreenRectangle> stack = new ArrayDeque<>();

  public ScreenRectangle push(ScreenRectangle rectangle) {
    ScreenRectangle parent = this.stack.peekLast();
    ScreenRectangle effective = (parent != null) ? rectangle.intersection(parent) : rectangle;
    this.stack.addLast(effective);
    return effective;
  }

  public ScreenRectangle pop() {
    if (this.stack.isEmpty()) {
      return null;
    }
    this.stack.removeLast();
    return this.stack.peekLast();
  }

  public void clear() {
    this.stack.clear();
  }
}
