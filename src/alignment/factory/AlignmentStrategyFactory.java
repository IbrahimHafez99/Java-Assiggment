package alignment.factory;

import alignment.strategy.CenterAlignmentStrategy;
import alignment.strategy.JustifyAlignmentStrategy;
import alignment.strategy.LeftAlignmentStrategy;
import alignment.strategy.RightAlignmentStrategy;
import alignment.strategy.TextAlignmentStrategy;

/**
 * Factory class for creating text alignment strategy instances.
 * This class implements the Factory Pattern to encapsulate the creation
 * of strategy objects, keeping the object creation logic separate from the code
 * that uses * them.
 */
public final class AlignmentStrategyFactory {

    /**
     * Creates an alignment strategy based on the given type string.
     * 
     * @param alignmentType the type of alignment (case-insensitive)
     * @return the corresponding strategy instance
     * @throws IllegalArgumentException if the alignment type is not recognized
     */
    public static TextAlignmentStrategy createStrategy(String alignmentType) {
        // Normalize to lowercase for case-insensitive comparison
        String normalizedType = alignmentType.toLowerCase();

        // Create and return the appropriate strategy based on the type
        switch (normalizedType) {
            case "left":
                return new LeftAlignmentStrategy();

            case "right":
                return new RightAlignmentStrategy();

            case "centre":
                return new CenterAlignmentStrategy();

            case "justify":
                return new JustifyAlignmentStrategy();

            default:
                // Unknown type
                throw new IllegalArgumentException(
                        "Unknown alignment type: " + alignmentType
                                + ". Valid types are: left, right, center, justify");
        }
    }
}
