package net.evermod.client.gui.api.style;

import net.evermod.client.graphics.style.Border;
import net.evermod.client.graphics.style.BorderColor;

/**
 * Interface defining CSS-like border styling capabilities using {@link Border} and {@link BorderColor}.
 * Uses default implementations that delegate to canonical methods to minimize boilerplate.
 *
 * @param <T> The self type for fluent chaining.
 * @author Wipodev
 */
public interface Borderable<T extends Borderable<T>> {

  /* ======================================================================== */
  /*  CANONICAL / PRIMARY METHOD                                             */
  /* ======================================================================== */

  /**
   * Primary method that sets both border thickness and color.
   * All other full-border overloads delegate to this method.
   *
   * @param border {@link Border} configuration.
   * @param color  {@link BorderColor} configuration.
   * @return This element instance for method chaining.
   */
  T border(Border border, BorderColor color);

  /* ======================================================================== */
  /*  FULL BORDER DEFAULT OVERLOADS                                           */
  /* ======================================================================== */

  default T border(BorderColor color) {
    return border(getBorder(), color);
  }

  default T border(Border border) {
    return border(border, getBorderColor());
  }

  default T border(int thickness, int argbColor) {
    return border(Border.all(thickness), BorderColor.all(argbColor));
  }

  default T border(int thickness, BorderColor color) {
    return border(Border.all(thickness), color);
  }

  default T border(Border border, int argbColor) {
    return border(border, BorderColor.all(argbColor));
  }

  default T border(int thickness) {
    return border(Border.all(thickness), getBorderColor());
  }

  default T borderColor(int argbColor) {
    return border(getBorder(), BorderColor.all(argbColor));
  }

  /* ======================================================================== */
  /*  SIDE-SPECIFIC OVERLOADS                                                 */
  /* ======================================================================== */

  default T borderTop(int thickness, int argbColor) {
    Border currentB = getBorder();
    BorderColor currentC = getBorderColor();
    return border(
        new Border(thickness, currentB.right(), currentB.bottom(), currentB.left()),
        new BorderColor(argbColor, currentC.right(), currentC.bottom(), currentC.left()));
  }

  default T borderTop(int thickness) {
    Border currentB = getBorder();
    return border(new Border(thickness, currentB.right(), currentB.bottom(), currentB.left()));
  }

  default T borderTopColor(int argbColor) {
    BorderColor currentC = getBorderColor();
    return border(new BorderColor(argbColor, currentC.right(), currentC.bottom(), currentC.left()));
  }

  default T borderRight(int thickness, int argbColor) {
    Border currentB = getBorder();
    BorderColor currentC = getBorderColor();
    return border(
        new Border(currentB.top(), thickness, currentB.bottom(), currentB.left()),
        new BorderColor(currentC.top(), argbColor, currentC.bottom(), currentC.left()));
  }

  default T borderRight(int thickness) {
    Border currentB = getBorder();
    return border(new Border(currentB.top(), thickness, currentB.bottom(), currentB.left()));
  }

  default T borderRightColor(int argbColor) {
    BorderColor currentC = getBorderColor();
    return border(new BorderColor(currentC.top(), argbColor, currentC.bottom(), currentC.left()));
  }

  default T borderBottom(int thickness, int argbColor) {
    Border currentB = getBorder();
    BorderColor currentC = getBorderColor();
    return border(
        new Border(currentB.top(), currentB.right(), thickness, currentB.left()),
        new BorderColor(currentC.top(), currentC.right(), argbColor, currentC.left()));
  }

  default T borderBottom(int thickness) {
    Border currentB = getBorder();
    return border(new Border(currentB.top(), currentB.right(), thickness, currentB.left()));
  }

  default T borderBottomColor(int argbColor) {
    BorderColor currentC = getBorderColor();
    return border(new BorderColor(currentC.top(), currentC.right(), argbColor, currentC.left()));
  }

  default T borderLeft(int thickness, int argbColor) {
    Border currentB = getBorder();
    BorderColor currentC = getBorderColor();
    return border(
        new Border(currentB.top(), currentB.right(), currentB.bottom(), thickness),
        new BorderColor(currentC.top(), currentC.right(), currentC.bottom(), argbColor));
  }

  default T borderLeft(int thickness) {
    Border currentB = getBorder();
    return border(new Border(currentB.top(), currentB.right(), currentB.bottom(), thickness));
  }

  default T borderLeftColor(int argbColor) {
    BorderColor currentC = getBorderColor();
    return border(new BorderColor(currentC.top(), currentC.right(), currentC.bottom(), argbColor));
  }

  /* ======================================================================== */
  /*  GETTERS                                                                 */
  /* ======================================================================== */

  Border getBorder();

  BorderColor getBorderColor();
}
