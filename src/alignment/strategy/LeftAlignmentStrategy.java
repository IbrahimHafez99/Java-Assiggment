package alignment.strategy;

import util.LineWrapper;
import java.util.List;

/**
 * Left alignment strategy - aligns text to the left margin.
 * This is the simplest alignment type, where text starts at the beginning of each line
 * with no padding or special formatting. Words are wrapped at the line length.
 *
 * <p>Example with lineLength=10:
 * <pre>
 * Input: "Hello world how are you"
 * Output:
 *   "Hello"
 *   "world how"
 *   "are you"
 * </pre>
 *
 * @author Ibrahim Haruna
 * @version 1.0
 */
public final class LeftAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with left alignment.
     * Simply wraps the text at word boundaries without adding any padding.
     *
     * <p>This is the most straightforward alignment - we just break the text
     * into lines that fit within the specified length, and the natural position
     * of text (starting from the left) gives us left alignment.
     *
     * @param paragraph the text to align
     * @param lineLength maximum characters per line
     * @return list of left-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // For left alignment, we simply wrap words without any padding
        return LineWrapper.wrapWords(paragraph, lineLength);
    }
}

