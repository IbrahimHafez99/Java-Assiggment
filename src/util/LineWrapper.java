package util;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the common task of breaking a paragraph into multiple
 * lines
 * while respecting word boundaries. It's used by most alignment strategies:
 * - Left, Right, and Center alignment all use this for basic line breaking
 * - Justify alignment has its own special logic and doesn't use this
 *
 * The wrapping algorithm:
 * 1. Split the text into individual words
 * 2. Add words to the current line until adding the next word would exceed
 * maxLength
 * 3. When a word doesn't fit, start a new line
 * 4. Handle edge cases like words longer than maxLength
 */
public class LineWrapper {

    /**
     * This method breaks text at word boundaries
     *
     * @param text      The paragraph text to wrap (should not contain line breaks)
     * @param maxLength The maximum number of characters allowed per line
     * @return A list of strings, each representing one line of wrapped text
     */
    public static List<String> wrapWords(String text, int maxLength) {
        List<String> lines = new ArrayList<>();
        if (text == null || text.trim().isEmpty())
            return lines;
        String[] words = text.trim().split("\\s+");
        StringBuilder cur = new StringBuilder();
        for (String w : words) {
            if (cur.length() == 0) {
                cur.append(w);
            } else if (cur.length() + 1 + w.length() <= maxLength) {
                cur.append(' ').append(w);
            } else {
                lines.add(cur.toString());
                cur.setLength(0);
                // If single word longer than maxLen, put alone (allowed for non-justify)
                cur.append(w);
            }
        }
        if (cur.length() > 0)
            lines.add(cur.toString());
        return lines;
    }
}
