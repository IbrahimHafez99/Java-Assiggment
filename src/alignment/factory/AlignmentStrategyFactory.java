package alignment.factory;

import alignment.strategy.CenterAlignmentStrategy;
import alignment.strategy.JustifyAlignmentStrategy;
import alignment.strategy.LeftAlignmentStrategy;
import alignment.strategy.RightAlignmentStrategy;
import alignment.strategy.TextAlignmentStrategy;

/**
 * Factory class for creating text alignment strategy instances.
 * This class implements the Factory Pattern to encapsulate the creation
 * of strategy objects, keeping the object creation logic separate from the code that uses them.
 *
 * <p>This provides several benefits:
 * <ol>
 *   <li>Centralized Creation: All strategy instantiation logic is in one place</li>
 *   <li>Easy Extension: Adding new alignment types only requires changes here</li>
 *   <li>Clean Client Code: Main class doesn't need complex if-else chains for creation</li>
 *   <li>Type Safety: Returns the interface type, enforcing proper abstraction</li>
 * </ol>
 *
 * <p>Why use the Factory Pattern here?
 * <ul>
 *   <li>We need to create different strategy objects based on a string input</li>
 *   <li>The creation logic (mapping strings to classes) is the same everywhere</li>
 *   <li>We might add more complex creation logic later (e.g., caching instances)</li>
 * </ul>
 *
 * @author Ibrahim Haruna
 * @version 1.0
 */
public final class AlignmentStrategyFactory {

    /**
     * Creates an alignment strategy based on the given type string.
     * The type string is case-insensitive and supports both US and UK spellings.
     *
     * <p>Supported alignment types:
     * <ul>
     *   <li>"left" - creates a LeftAlignmentStrategy</li>
     *   <li>"right" - creates a RightAlignmentStrategy</li>
     *   <li>"center" or "centre" - creates a CenterAlignmentStrategy</li>
     *   <li>"justify" - creates a JustifyAlignmentStrategy</li>
     * </ul>
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

            case "center":
            case "centre":  // Support both US and UK spelling
                return new CenterAlignmentStrategy();

            case "justify":
                return new JustifyAlignmentStrategy();

            default:
                // Unknown type - throw exception with helpful message
                throw new IllegalArgumentException(
                    "Unknown alignment type: " + alignmentType
                    + ". Valid types are: left, right, center, justify"
                );
        }
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods, so it should never be instantiated.
     */
    private AlignmentStrategyFactory() {
        throw new UnsupportedOperationException(
            "AlignmentStrategyFactory is a utility class and should not be instantiated"
        );
    }
}

