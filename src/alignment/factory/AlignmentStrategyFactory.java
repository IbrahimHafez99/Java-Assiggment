package alignment.factory;

import alignment.strategy.*;

/**
 * Factory class for creating text alignment strategy instances.
 *
 * This class implements the Factory Pattern to encapsulate the creation
 * of strategy objects. This provides several benefits:
 *
 * 1. Centralized Creation: All strategy instantiation logic is in one place
 * 2. Easy Extension: Adding new alignment types only requires changes here
 * 3. Clean Client Code: Main class doesn't need if-else chains for creation
 * 4. Type Safety: Returns the interface type, enforcing proper abstraction
 *
 * Why use Factory Pattern here?
 * - We need to create different strategy objects based on a string input
 * - The creation logic (mapping strings to classes) is the same everywhere
 * - We might add more complex creation logic later (e.g., caching instances)
 */
public class AlignmentStrategyFactory {

    /**
     * Creates an alignment strategy based on the given type.
     *
     * @param alignmentType The type of alignment ("left", "right", "center", "justify")
     * @return The corresponding strategy instance
     * @throws IllegalArgumentException If the alignment type is not recognized
     */
    public static TextAlignmentStrategy createStrategy(String alignmentType) {
        // Normalize to lowercase for case-insensitive comparison
        String normalizedType = alignmentType.toLowerCase();

        switch (normalizedType) {
            case "left":
                return new LeftAlignmentStrategy();

            case "right":
                return new RightAlignmentStrategy();

            case "center":
            case "centre":  // Support both US and UK spelling
                return new CenterAlignmentStrategy();

            case "justify":
                return new JustifyAlignmentStrategy();

            default:
                throw new IllegalArgumentException(
                    "Unknown alignment type: " + alignmentType +
                    ". Valid types are: left, right, center, justify"
                );
        }
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private AlignmentStrategyFactory() {
        throw new UnsupportedOperationException(
            "AlignmentStrategyFactory is a utility class and should not be instantiated"
        );
    }
}
