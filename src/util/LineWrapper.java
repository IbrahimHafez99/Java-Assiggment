package util;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for wrapping text at word boundaries.
 * This class handles the common task of breaking a paragraph into multiple
 * lines
 * while respecting word boundaries, which prevents splitting words in the
 * middle.
 */
public class LineWrapper {

    /**
     * The method uses a greedy algorithm: it keeps adding words to the current
     * line as long as they fit. When a word doesn't fit, the current line is saved
     * and a new line is started with that word.
     *
     * @param text      the paragraph text to wrap (should not contain line breaks)
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
                // First word on the line, always add it
                cur.append(w);
            } else if (cur.length() + 1 + w.length() <= maxLength) {
                // Word fits on current line (including space before it)
                cur.append(' ').append(w);
            } else {
                // Word doesn't fit so save current line and start new one
                lines.add(cur.toString());
                cur.setLength(0);
                // Start new line with this word (even if word itself exceeds maxLength)
                cur.append(w);
            }
        }

        // add the last line if it has content
        if (cur.length() > 0) {
            lines.add(cur.toString());
        }

        return lines;
    }
}
