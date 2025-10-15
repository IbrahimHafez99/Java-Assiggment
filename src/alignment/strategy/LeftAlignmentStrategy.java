package alignment.strategy;

import util.LineWrapper;
import java.util.List;

/**
 * Left alignment strategy - aligns text to the left margin.
 *
 * This is the simplest alignment type. Text starts at the beginning of each
 * line
 * with no padding or special formatting. Words are wrapped at the line length.
 *
 * Example with lineLength=10:
 * Input: "Hello world how are you"
 * Output:
 * "Hello"
 * "world how"
 * "are you"
 */
public class LeftAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with left alignment.
     *
     * ALGORITHM OVERVIEW:
     * 1. Use LineWrapper to break text into lines at word boundaries
     * 2. No additional formatting or padding needed
     * 3. Return the wrapped lines as-is
     *
     * @param paragraph  The text to align
     * @param lineLength Maximum characters per line
     * @return List of left-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // For left alignment, we just wrap words at the line length
        // LineWrapper handles all the work:
        // - Splits text into words
        // - Greedily adds words to lines until next word won't fit
        // - Starts new line when necessary
        // - Returns list of wrapped lines
        //
        // No padding or special formatting needed - text naturally
        // aligns to the left when no spaces are added.
        return LineWrapper.wrapWords(paragraph, lineLength);
    }
}
