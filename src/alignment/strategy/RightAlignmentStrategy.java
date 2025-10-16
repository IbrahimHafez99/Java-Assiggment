package alignment.strategy;

import util.LineWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Right alignment strategy - aligns text to the right margin.
 * Each line is padded with spaces on the left to push the text to the right edge,
 * so all lines end at the same position.
 *
 * <p>Example with lineLength=10:
 * <pre>
 * Input: "Hello world how are you"
 * Output:
 *   "     Hello"  (5 spaces + "Hello")
 *   " world how"  (1 space + "world how")
 *   "   are you"  (3 spaces + "are you")
 * </pre>
 *
 * @author Ibrahim Haruna
 * @version 1.0
 */
public final class RightAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with right alignment.
     * First wraps the text at word boundaries, then adds left padding to each line.
     *
     * <p>Algorithm:
     * <ol>
     *   <li>Wrap the text into lines using LineWrapper</li>
     *   <li>For each line, calculate how many spaces needed on the left</li>
     *   <li>Add those spaces before the text to push it to the right</li>
     * </ol>
     *
     * @param paragraph the text to align
     * @param lineLength maximum characters per line
     * @return list of right-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // First, wrap the text at word boundaries
        List<String> base = LineWrapper.wrapWords(paragraph, lineLength);
        List<String> out = new ArrayList<>(base.size());

        // Add left padding to each line to align text to the right
        for (String line : base) {
            // Calculate spaces needed: lineLength - actual line length
            int pad = Math.max(0, lineLength - line.length());
            // Add padding before the text
            out.add(repeat(' ', pad) + line);
        }

        return out;
    }

    /**
     * Helper method to create a string of repeated characters.
     * Used to generate the padding spaces for right alignment.
     *
     * @param c the character to repeat
     * @param n the number of times to repeat it
     * @return a string containing n copies of character c
     */
    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}

