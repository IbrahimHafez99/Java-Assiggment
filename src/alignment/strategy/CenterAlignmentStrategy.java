package alignment.strategy;

import util.LineWrapper;
import util.TextConstants;
import java.util.ArrayList;
import java.util.List;

/**
 * Center alignment strategy - centers text within the line length.
 *
 * Each line is padded with spaces on both sides to center the text.
 * If padding is odd, the extra space goes on the right side.
 *
 * Example with lineLength=10:
 * Input: "Hello world how are you"
 * Output:
 * "  Hello   "  (2 left + "Hello" + 3 right = 10)
 * " world how"  (1 left + "world how" = 10)
 * " are you  "  (1 left + "are you" + 2 right = 10)
 */
public class CenterAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with center alignment.
     *
     * ALGORITHM OVERVIEW:
     * 1. Wrap the text into lines at word boundaries (using LineWrapper)
     * 2. For each line, calculate total padding needed
     * 3. Split padding evenly between left and right sides
     * 4. If padding is odd, put the extra space on the right
     * 5. Build line: left padding + text + right padding
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
     *   Line 1: 20 - 19 = 1 total padding
     *           leftPadding = 1 / 2 = 0 (integer division)
     *           rightPadding = 1 - 0 = 1 (gets the extra)
     *
     *   Line 2: 20 - 6 = 14 total padding
     *           leftPadding = 14 / 2 = 7
     *           rightPadding = 14 - 7 = 7
     *
     * Step 3 - Build lines:
     *   Line 1: "" + "Hello world this is" + " " = "Hello world this is "
     *   Line 2: "       " + "a test" + "       " = "       a test       "
     *
     * @param paragraph The text to align
     * @param lineLength Maximum characters per line
     * @return List of center-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // STEP 1: Wrap the text into lines at word boundaries
        // This gives us lines that fit within lineLength but aren't centered yet
        List<String> wrappedLines = LineWrapper.wrapWords(paragraph, lineLength);

        List<String> centerAlignedLines = new ArrayList<>();

        // STEP 2-4: For each line, add padding on both sides to center it
        for (String line : wrappedLines) {
            // Calculate total padding needed
            // Example: lineLength=20, line="test" (4 chars) → 16 total padding
            int totalPadding = lineLength - line.length();

            // Split padding in half using integer division
            // Example: 16 / 2 = 8 spaces on each side
            int leftPadding = totalPadding / 2;

            // Calculate right padding to handle odd padding cases
            // Example: totalPadding=15 → left=7, right=8 (extra goes right)
            int rightPadding = totalPadding - leftPadding;

            // STEP 5: Build the centered line
            StringBuilder alignedLine = new StringBuilder();

            // Add left padding (pushing text toward center from left)
            for (int i = 0; i < leftPadding; i++) {
                alignedLine.append(TextConstants.SPACE);
            }

            // Add the actual text (now in the center)
            alignedLine.append(line);

            // Add right padding (balancing from the right)
            for (int i = 0; i < rightPadding; i++) {
                alignedLine.append(TextConstants.SPACE);
            }

            centerAlignedLines.add(alignedLine.toString());
        }

        return centerAlignedLines;
    }
}
