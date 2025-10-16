package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for wrapping text at word boundaries.
 * This class handles the common task of breaking a paragraph into multiple lines
 * while respecting word boundaries, which prevents splitting words in the middle.
 *
 * <p>This wrapper is used by most alignment strategies:
 * <ul>
 *   <li>Left, Right, and Center alignment all use this for basic line breaking</li>
 *   <li>Justify alignment has its own special logic and doesn't use this</li>
 * </ul>
 *
 * <p>The wrapping algorithm follows a greedy approach:
 * <ol>
 *   <li>Split the text into individual words</li>
 *   <li>Add words to the current line until adding the next word would exceed maxLength</li>
 *   <li>When a word doesn't fit, start a new line</li>
 *   <li>Handle edge cases like words longer than maxLength</li>
 * </ol>
 *
 * @author Ibrahim Haruna
 * @version 1.0
 */
public class LineWrapper {

    /**
     * Wraps text at word boundaries to fit within the specified maximum length.
     * Words are never split unless they exceed the maximum length on their own.
     *
     * <p>The method uses a greedy algorithm: it keeps adding words to the current
     * line as long as they fit. When a word doesn't fit, the current line is saved
     * and a new line is started with that word.
     *
     * @param text the paragraph text to wrap (should not contain line breaks)
     * @param maxLength the maximum number of characters allowed per line
     * @return a list of strings, each representing one line of wrapped text
     */
    public static List<String> wrapWords(String text, int maxLength) {
        List<String> lines = new ArrayList<>();

        // Handle empty input by returning an empty list
        if (text == null || text.trim().isEmpty()) {
            return lines;
        }

        // Split text into words using whitespace as delimiter
        String[] words = text.trim().split("\\s+");
        StringBuilder cur = new StringBuilder();

        // Process each word
        for (String w : words) {
            if (cur.length() == 0) {
                // First word on the line - always add it
                cur.append(w);
            } else if (cur.length() + 1 + w.length() <= maxLength) {
                // Word fits on current line (including space before it)
                cur.append(' ').append(w);
            } else {
                // Word doesn't fit - save current line and start new one
                lines.add(cur.toString());
                cur.setLength(0);
                // Start new line with this word (even if word itself exceeds maxLength)
                cur.append(w);
            }
        }

        // Don't forget to add the last line if it has content
        if (cur.length() > 0) {
            lines.add(cur.toString());
        }

        return lines;
    }
}

