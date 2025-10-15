package alignment.strategy;

import java.util.List;

/**
 * Strategy interface for different text alignment algorithms.
 * This interface defines the contract that all text alignment strategies must follow.
 * Each implementation represents a different way to align text within a given line length:
 * - Left alignment: text starts at the beginning of each line
 * - Right alignment: text is pushed to the end of each line
 * - Center alignment: text is centered within each line
 * - Justify alignment: text fills the entire line with evenly distributed spaces
 * Why we use Strategy Pattern here:
 * - We have multiple algorithms (left, right, center, justify) that do the same task differently
 * - We want to be able to switch between algorithms at runtime
 * - Each algorithm is completely independent with no shared implementation
 * - Adding new alignment types should not require changing existing code
 */
public interface TextAlignmentStrategy {

    /**
     * Aligns the given paragraph according to this strategy's specific alignment rules.
     *
     * @param paragraph The paragraph text to be aligned (single paragraph, no line breaks)
     * @param lineLength The maximum number of characters allowed per line
     * @return A list of strings, where each string represents one formatted line
     * Example: align("Hello world how are you", 10) might return:
     * ["Hello    ", "world how ", "are you   "] for right alignment
     * ["Hello world", "how are you"] for left alignment
     */
    List<String> align(String paragraph, int lineLength);
}