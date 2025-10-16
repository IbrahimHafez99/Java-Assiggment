package alignment.strategy;

import java.util.List;

/**
 * Strategy interface for different text alignment algorithms.
 * This interface defines the contract that all text alignment strategies must follow,
 * implementing the Strategy design pattern for text alignment.
 *
 * <p>Each implementation represents a different way to align text within a given line length:
 * <ul>
 *   <li>Left alignment: text starts at the beginning of each line</li>
 *   <li>Right alignment: text is pushed to the end of each line</li>
 *   <li>Center alignment: text is centered within each line</li>
 *   <li>Justify alignment: text fills the entire line with evenly distributed spaces</li>
 * </ul>
 *
 * <p>Why we use the Strategy Pattern here:
 * <ul>
 *   <li>We have multiple algorithms that accomplish the same task in different ways</li>
 *   <li>We want to be able to switch between algorithms at runtime</li>
 *   <li>Each algorithm is completely independent with no shared implementation</li>
 *   <li>Adding new alignment types doesn't require changing existing code</li>
 * </ul>
 *
 * @author Ibrahim Haruna
 * @version 1.0
 */
public interface TextAlignmentStrategy {

    /**
     * Aligns the given paragraph according to this strategy's specific alignment rules.
     * The paragraph is broken into multiple lines that fit within the specified line length,
     * and each line is formatted according to the alignment type.
     *
     * <p>Example outputs for "Hello world how are you" with lineLength=10:
     * <ul>
     *   <li>Left: ["Hello", "world how", "are you"]</li>
     *   <li>Right: ["     Hello", " world how", "   are you"]</li>
     *   <li>Center: ["  Hello   ", " world how", " are you  "]</li>
     * </ul>
     *
     * @param paragraph the paragraph text to be aligned (single paragraph, no line breaks)
     * @param lineLength the maximum number of characters allowed per line
     * @return a list of strings, where each string represents one formatted line
     */
    List<String> align(String paragraph, int lineLength);
}
