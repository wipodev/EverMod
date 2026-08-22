package net.evermod.client.graphics.style;

/**
 * Immutable representation of quadrilateral vertex color mappings for solid and gradient rendering.
 * Pre-calculates RGBA float components to eliminate bitwise operations and branching during vertex submission.
 * <p>
 * Vertex Submission Order:
 * <ul>
 *   <li>V1: Bottom-Left</li>
 *   <li>V2: Bottom-Right</li>
 *   <li>V3: Top-Right</li>
 *   <li>V4: Top-Left</li>
 * </ul>
 * </p>
 *
 * @author Wipodev
 */
public record GradientStyle(
    float r1, float g1, float b1, float a1, // Bottom-Left (V1)
    float r2, float g2, float b2, float a2, // Bottom-Right (V2)
    float r3, float g3, float b3, float a3, // Top-Right (V3)
    float r4, float g4, float b4, float a4 // Top-Left (V4)
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

    float rMid = lerp(r1, r2, 0.5F);
    float gMid = lerp(g1, g2, 0.5F);
    float bMid = lerp(b1, b2, 0.5F);
    float aMid = lerp(a1, a2, 0.5F);

    return switch (direction) {
      case VERTICAL -> new GradientStyle(
          r2, g2, b2, a2, // V1: Bottom-Left  (To)
          r2, g2, b2, a2, // V2: Bottom-Right (To)
          r1, g1, b1, a1, // V3: Top-Right    (From)
          r1, g1, b1, a1 // V4: Top-Left     (From)
        );
      case VERTICAL_REVERSE -> new GradientStyle(
          r1, g1, b1, a1, // V1: Bottom-Left  (From)
          r1, g1, b1, a1, // V2: Bottom-Right (From)
          r2, g2, b2, a2, // V3: Top-Right    (To)
          r2, g2, b2, a2 // V4: Top-Left     (To)
        );
      case HORIZONTAL -> new GradientStyle(
          r1, g1, b1, a1, // V1: Bottom-Left  (From)
          r2, g2, b2, a2, // V2: Bottom-Right (To)
          r2, g2, b2, a2, // V3: Top-Right    (To)
          r1, g1, b1, a1 // V4: Top-Left     (From)
        );
      case HORIZONTAL_REVERSE -> new GradientStyle(
          r2, g2, b2, a2, // V1: Bottom-Left  (To)
          r1, g1, b1, a1, // V2: Bottom-Right (From)
          r1, g1, b1, a1, // V3: Top-Right    (From)
          r2, g2, b2, a2 // V4: Top-Left     (To)
        );
      case DIAGONAL_TOP_LEFT -> new GradientStyle(
          rMid, gMid, bMid, aMid, // V1: Bottom-Left  (Midpoint)
          r2, g2, b2, a2, // V2: Bottom-Right (To)
          rMid, gMid, bMid, aMid, // V3: Top-Right    (Midpoint)
          r1, g1, b1, a1 // V4: Top-Left     (From)
        );
      case DIAGONAL_TOP_RIGHT -> new GradientStyle(
          r2, g2, b2, a2, // V1: Bottom-Left  (To)
          rMid, gMid, bMid, aMid, // V2: Bottom-Right (Midpoint)
          r1, g1, b1, a1, // V3: Top-Right    (From)
          rMid, gMid, bMid, aMid // V4: Top-Left     (Midpoint)
        );
      case DIAGONAL_BOTTOM_LEFT -> new GradientStyle(
          r1, g1, b1, a1, // V1: Bottom-Left  (From)
          rMid, gMid, bMid, aMid, // V2: Bottom-Right (Midpoint)
          r2, g2, b2, a2, // V3: Top-Right    (To)
          rMid, gMid, bMid, aMid // V4: Top-Left     (Midpoint)
        );
      case DIAGONAL_BOTTOM_RIGHT -> new GradientStyle(
          rMid, gMid, bMid, aMid, // V1: Bottom-Left  (Midpoint)
          r1, g1, b1, a1, // V2: Bottom-Right (From)
          rMid, gMid, bMid, aMid, // V3: Top-Right    (Midpoint)
          r2, g2, b2, a2 // V4: Top-Left     (To)
        );
    };
  }

  /**
   * Creates a custom 4-corner color mapping in ARGB format.
   *
   * @param bottomLeft ARGB bottom-left color (V1)
   * @param bottomRight ARGB bottom-right color (V2)
   * @param topRight ARGB top-right color (V3)
   * @param topLeft ARGB top-left color (V4)
   * @return custom GradientStyle
   */
  public static GradientStyle corners(int bottomLeft, int bottomRight, int topRight, int topLeft) {
    return new GradientStyle(
        (bottomLeft >> 16 & 255) / 255.0F,
        (bottomLeft >> 8 & 255) / 255.0F,
        (bottomLeft & 255) / 255.0F,
        (bottomLeft >> 24 & 255) / 255.0F,

        (bottomRight >> 16 & 255) / 255.0F,
        (bottomRight >> 8 & 255) / 255.0F,
        (bottomRight & 255) / 255.0F,
        (bottomRight >> 24 & 255) / 255.0F,

        (topRight >> 16 & 255) / 255.0F,
        (topRight >> 8 & 255) / 255.0F,
        (topRight & 255) / 255.0F,
        (topRight >> 24 & 255) / 255.0F,

        (topLeft >> 16 & 255) / 255.0F,
        (topLeft >> 8 & 255) / 255.0F,
        (topLeft & 255) / 255.0F,
        (topLeft >> 24 & 255) / 255.0F);
  }

  /**
   * Performs linear interpolation between two float color components.
   *
   * @param start starting value
   * @param end target value
   * @param delta interpolation factor [0.0 - 1.0]
   * @return interpolated float value
   */
  private static float lerp(float start, float end, float delta) {
    return start + delta * (end - start);
  }

  public enum Direction {
    VERTICAL, VERTICAL_REVERSE, HORIZONTAL, HORIZONTAL_REVERSE, DIAGONAL_TOP_LEFT, DIAGONAL_TOP_RIGHT, DIAGONAL_BOTTOM_LEFT, DIAGONAL_BOTTOM_RIGHT
  }
}
