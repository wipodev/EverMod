package net.evermod.client.gui.layout;

import net.evermod.client.gui.UIComponent;

/**
 * Static factory utility class for creating layout containers and spatial utilities.
 * Provides a declarative DSL-style API for UI construction.
 *
 * @author Wipodev
 */
public final class Layouts {

  private Layouts() {
    // Utility class; instantiation strictly prevented
  }

  // --- COLUMN FACTORIES ---

  public static Column column() {
    return new Column();
  }

  public static Column column(int gap) {
    return new Column(gap);
  }

  public static Column column(int gap, int padding) {
    return new Column(0, 0, gap, padding);
  }

  public static Column column(int x, int y, int gap, int padding) {
    return new Column(x, y, gap, padding);
  }

  // --- ROW FACTORIES ---

  public static Row row() {
    return new Row();
  }

  public static Row row(int gap) {
    return new Row(gap);
  }

  public static Row row(int gap, int padding) {
    return new Row(0, 0, gap, padding);
  }

  public static Row row(int x, int y, int gap, int padding) {
    return new Row(x, y, gap, padding);
  }

  // --- BOX FACTORIES ---

  public static Box box() {
    return new Box();
  }

  public static Box box(int width, int height) {
    return new Box(width, height);
  }

  public static Box box(int x, int y, int width, int height) {
    return new Box(x, y, width, height);
  }

  // --- SCROLLABLE FACTORIES ---

  public static Scrollable scrollable() {
    return new Scrollable();
  }

  public static Scrollable scrollable(int width, int height) {
    return new Scrollable(width, height);
  }

  public static Scrollable scrollable(int x, int y, int width, int height) {
    return new Scrollable(x, y, width, height);
  }

  public static Scrollable scrollable(UIComponent content, int width, int height) {
    return new Scrollable(width, height).setContent(content);
  }

  // --- SPACE FACTORIES ---

  public static Space space(int width, int height) {
    return new Space(width, height);
  }

  public static Space space(int size) {
    return Space.of(size);
  }

  public static Space spaceWidth(int width) {
    return Space.width(width);
  }

  public static Space spaceHeight(int height) {
    return Space.height(height);
  }

  // --- DIVIDER FACTORIES ---

  public static Divider divider() {
    return Divider.horizontal();
  }

  public static Divider horizontalDivider() {
    return Divider.horizontal();
  }

  public static Divider horizontalDivider(int thickness, int color) {
    return Divider.horizontal(thickness, color);
  }

  public static Divider verticalDivider() {
    return Divider.vertical();
  }

  public static Divider verticalDivider(int thickness, int color) {
    return Divider.vertical(thickness, color);
  }
}
