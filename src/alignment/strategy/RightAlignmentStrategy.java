package alignment.strategy;

import util.LineWrapper;
import util.TextConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * Right alignment strategy - aligns text to the right margin.
 *
 * Each line is padded with spaces on the left to push the text to the right edge.
 * The text ends at position lineLength (or just before it for shorter final lines).
 *
 * Example with lineLength=10:
 * Input: "Hello world how are you"
 * Output:
 * "     Hello"  (5 spaces + "Hello")
 * " world how"  (1 space + "world how")
 * "   are you"  (3 spaces + "are you")
 */
public class RightAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with right alignment.
     *
     * ALGORITHM OVERVIEW:
     * 1. Wrap the text into lines at word boundaries (using LineWrapper)
     * 2. For each line, calculate how much padding is needed on the left
     * 3. Add spaces to the left of the line to push text to the right edge
     * 4. Result: All lines end at the same position (lineLength)
     *
     * EXAMPLE:
     * Input: "Hello world this is a test"
     * Line Length: 20
     *
     * Step 1 - Wrapping:
     *   Line 1: "Hello world this is" (19 chars)
     *   Line 2: "a test"              (6 chars)
     *
     * Step 2 - Calculate padding:
     *   Line 1: 20 - 19 = 1 space needed
     *   Line 2: 20 - 6 = 14 spaces needed
     *
     * Step 3 - Apply padding:
     *   Line 1: " Hello world this is"  (1 space + text)
     *   Line 2: "              a test"  (14 spaces + text)
     *
     * @param paragraph The text to align
     * @param lineLength Maximum characters per line
     * @return List of right-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // STEP 1: Wrap the text into lines at word boundaries
        // This gives us lines that fit within lineLength but aren't padded yet
        List<String> wrappedLines = LineWrapper.wrapWords(paragraph, lineLength);

        List<String> rightAlignedLines = new ArrayList<>();

        // STEP 2 & 3: For each line, add padding to the left to right-align it
        for (String line : wrappedLines) {
            // Calculate how many spaces we need to add on the left
            // Formula: lineLength - current line length = spaces needed
            // Example: lineLength=20, line="test" (4 chars) → 16 spaces needed
            int paddingNeeded = lineLength - line.length();

            // Build the right-aligned line
            StringBuilder alignedLine = new StringBuilder();

            // Add left padding (pushing text to the right)
            for (int i = 0; i < paddingNeeded; i++) {
                alignedLine.append(TextConstants.SPACE);
            }

            // Add the actual text (now at the right edge)
            alignedLine.append(line);

            rightAlignedLines.add(alignedLine.toString());
        }

        return rightAlignedLines;
    }
}
