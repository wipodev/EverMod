package net.evermod.client.graphics.style;

/**
 * Immutable representation of quadrilateral vertex color mappings for solid and gradient rendering.
 * Pre-calculates RGBA float components to eliminate bitwise operations and branching during vertex submission.
 *
 * @author Wipodev
 */
public record GradientStyle(
    float r1, float g1, float b1, float a1, // Top-Left (V1)
    float r2, float g2, float b2, float a2, // Top-Right (V2)
    float r3, float g3, float b3, float a3, // Bottom-Right (V3)
    float r4, float g4, float b4, float a4 // Bottom-Left (V4)
) {

  /**
   * Creates a solid color mapping for all 4 vertices.
   *
   * @param color ARGB color integer
   * @return GradientStyle mapping for a solid quad
   */
  public static GradientStyle solid(int color) {
    return gradient(color, color, Direction.VERTICAL);
  }

  /**
   * Creates a directional gradient mapping.
   *
   * @param colorFrom ARGB starting color
   * @param colorTo ARGB target color
   * @param direction direction of the gradient
   * @return calculated GradientStyle
   */
  public static GradientStyle gradient(int colorFrom, int colorTo, Direction direction) {
    float a1 = (colorFrom >> 24 & 255) / 255.0F;
    float r1 = (colorFrom >> 16 & 255) / 255.0F;
    float g1 = (colorFrom >> 8 & 255) / 255.0F;
    float b1 = (colorFrom & 255) / 255.0F;

    float a2 = (colorTo >> 24 & 255) / 255.0F;
    float r2 = (colorTo >> 16 & 255) / 255.0F;
    float g2 = (colorTo >> 8 & 255) / 255.0F;
    float b2 = (colorTo & 255) / 255.0F;

    return switch (direction) {
      case VERTICAL -> new GradientStyle(
          r1, g1, b1, a1, // Top-Left (From)
          r1, g1, b1, a1, // Top-Right (From)
          r2, g2, b2, a2, // Bottom-Right (To)
          r2, g2, b2, a2 // Bottom-Left (To)
        );
      case HORIZONTAL -> new GradientStyle(
          r1, g1, b1, a1, // Top-Left (From)
          r2, g2, b2, a2, // Top-Right (To)
          r2, g2, b2, a2, // Bottom-Right (To)
          r1, g1, b1, a1 // Bottom-Left (From)
        );
      case DIAGONAL -> new GradientStyle(
          r1, g1, b1, a1, // Top-Left (From)
          r1, g1, b1, a1, // Top-Right
          r2, g2, b2, a2, // Bottom-Right (To)
          r1, g1, b1, a1 // Bottom-Left
        );
      case DIAGONAL_REVERSE -> new GradientStyle(
          r1, g1, b1, a1, // Top-Left
          r2, g2, b2, a2, // Top-Right (From -> To)
          r1, g1, b1, a1, // Bottom-Right
          r2, g2, b2, a2 // Bottom-Left (To)
        );
    };
  }

  /**
   * Creates a custom 4-corner color mapping (useful for freeform or radial approximation).
   *
   * @param topLeft ARGB top-left color
   * @param topRight ARGB top-right color
   * @param bottomRight ARGB bottom-right color
   * @param bottomLeft ARGB bottom-left color
   * @return custom GradientStyle
   */
  public static GradientStyle corners(int topLeft, int topRight, int bottomRight, int bottomLeft) {
    return new GradientStyle(
        (topLeft >> 16 & 255) / 255.0F,
        (topLeft >> 8 & 255) / 255.0F,
        (topLeft & 255) / 255.0F,
        (topLeft >> 24 & 255) / 255.0F,
        (topRight >> 16 & 255) / 255.0F,
        (topRight >> 8 & 255) / 255.0F,
        (topRight & 255) / 255.0F,
        (topRight >> 24 & 255) / 255.0F,
        (bottomRight >> 16 & 255) / 255.0F,
        (bottomRight >> 8 & 255) / 255.0F,
        (bottomRight & 255) / 255.0F,
        (bottomRight >> 24 & 255) / 255.0F,
        (bottomLeft >> 16 & 255) / 255.0F,
        (bottomLeft >> 8 & 255) / 255.0F,
        (bottomLeft & 255) / 255.0F,
        (bottomLeft >> 24 & 255) / 255.0F);
  }

  public enum Direction {
    VERTICAL, HORIZONTAL, DIAGONAL, DIAGONAL_REVERSE
  }
}
