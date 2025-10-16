package alignment.strategy;

import util.LineWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Center alignment strategy - centers text within the line length.
 * Each line is padded with spaces on both sides to center the text.
 * If the total padding is odd, the extra space goes on the left side.
 *
 * <p>Example with lineLength=10:
 * <pre>
 * Input: "Hello world how are you"
 * Output:
 *   "  Hello   "  (2 left + "Hello" + 3 right = 10)
 *   " world how"  (1 left + "world how" = 10)
 *   " are you  "  (1 left + "are you" + 2 right = 10)
 * </pre>
 *
 * @author Ibrahim Haruna
 * @version 1.0
 */
public final class CenterAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with center alignment.
     * First wraps the text at word boundaries, then adds padding on both sides of each line.
     *
     * <p>Algorithm:
     * <ol>
     *   <li>Wrap the text into lines using LineWrapper</li>
     *   <li>For each line, calculate total padding needed</li>
     *   <li>Split padding between left and right sides</li>
     *   <li>If padding is odd, the left side gets the extra space</li>
     *   <li>Build line: left padding + text + right padding</li>
     * </ol>
     *
     * @param paragraph the text to align
     * @param lineLength maximum characters per line
     * @return list of center-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // First, wrap the text at word boundaries
        List<String> base = LineWrapper.wrapWords(paragraph, lineLength);
        List<String> out = new ArrayList<>(base.size());

        // Add padding to both sides of each line to center it
        for (String line : base) {
            // Calculate total spaces needed
            int total = Math.max(0, lineLength - line.length());

            // Distribute padding: right side gets floor, left side gets ceiling
            int right = total / 2;
            int left = total - right;  // Left gets the extra space if total is odd

            // Build the centered line with padding on both sides
            out.add(repeat(' ', left) + line + repeat(' ', right));
        }

        return out;
    }

    /**
     * Helper method to create a string of repeated characters.
     * Used to generate the padding spaces for center alignment.
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

