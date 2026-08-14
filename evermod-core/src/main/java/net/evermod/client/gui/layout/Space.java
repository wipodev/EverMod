package net.evermod.client.gui.layout;

import net.evermod.client.gui.AbstractComponent;
import net.evermod.client.gui.EverGraphics;

/**
 * A non-rendering layout spacer component used to introduce rigid spaces
 * between components inside containers like Row or Column.
 *
 * @author Wipodev
 */
public class Space extends AbstractComponent {

  /**
   * Constructs a Space component with specified dimensions.
   *
   * @param width  Spacer width in pixels.
   * @param height Spacer height in pixels.
   */
  public Space(int width, int height) {
    super(0, 0, width, height);
  }

  /**
   * Factory method to create a square Space component.
   *
   * @param size Size for both width and height in pixels.
   * @return A new Space instance.
   */
  public static Space of(int size) {
    return new Space(size, size);
  }

  /**
   * Factory method to create a horizontal Space component.
   *
   * @param width Width in pixels.
   * @return A new Space instance with height 0.
   */
  public static Space width(int width) {
    return new Space(width, 0);
  }

  /**
   * Factory method to create a vertical Space component.
   *
   * @param height Height in pixels.
   * @return A new Space instance with width 0.
   */
  public static Space height(int height) {
    return new Space(0, height);
  }

  /**
   * {@inheritDoc}
   * <p>
   * Intentional NO-OP: Space is invisible and only occupies layout bounds.
   * </p>
   */
  @Override
  public void render(EverGraphics graphics, int mouseX, int mouseY, float partialTicks) {
    // Intentional NO-OP: Space is invisible and only occupies layout bounds.
  }
}
