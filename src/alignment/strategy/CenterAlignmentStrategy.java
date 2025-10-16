package alignment.strategy;

import util.LineWrapper;
import util.StringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Center alignment strategy.
 * Each line is padded with spaces on both sides to center the text.
 * If the total padding is odd, the extra space goes on the left side.
 */
public final class CenterAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with center alignment.
     * First wraps the text at word boundaries, then adds padding on both sides of each line.
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
            out.add(StringUtil.repeat(' ', left) + line + StringUtil.repeat(' ', right));
        }

        return out;
    }
}

