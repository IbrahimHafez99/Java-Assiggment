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

        // Handle empty or null input
        if (text == null || text.trim().isEmpty()) {
            return lines;
        }

        // Split text into words, removing extra whitespace
        String[] words = text.trim().split(TextConstants.WORD_SPLIT_PATTERN);

        StringBuilder currentLine = new StringBuilder();

        for (String word : words) {
            // Check if adding this word (plus a space) would exceed maxLength
            boolean isFirstWordInLine = currentLine.length() == 0;
            int lengthWithThisWord = currentLine.length() +
                    (isFirstWordInLine ? 0 : 1) + // space before word (if not first)
                    word.length();

            if (lengthWithThisWord <= maxLength) {
                // Word fits on current line
                if (!isFirstWordInLine) {
                    currentLine.append(TextConstants.SPACE);
                }
                currentLine.append(word);
            } else {
                // Word doesn't fit, need to start a new line

                // If current line has content, save it
                if (currentLine.length() > 0) {
                    lines.add(currentLine.toString());
                    currentLine = new StringBuilder();
                }

                // Handle the case where a single word is longer than maxLength
                if (word.length() > maxLength) {
                    // For now, just put the long word on its own line
                    // The justify strategy will handle hyphenation separately
                    lines.add(word);
                } else {
                    // Start new line with this word
                    currentLine.append(word);
                }
            }
        }

        // Don't forget the last line if it has content
        if (currentLine.length() > 0) {
            lines.add(currentLine.toString());
        }

        return lines;
    }
}
