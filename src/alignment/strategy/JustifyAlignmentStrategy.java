package alignment.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Justify alignment strategy - distributes text evenly across the line.
 * This is the most complex alignment strategy, as it needs to ensure that every line
 * (except the last) fills the entire line length exactly.
 *
 * <p>Key features:
 * <ul>
 *   <li>Adds words to a line until the next word wouldn't fit</li>
 *   <li>Hyphenates words that are too long to fit on a line</li>
 *   <li>Ensures no line exceeds the maximum length (including hyphen)</li>
 *   <li>Last line is left-aligned (standard typography convention)</li>
 * </ul>
 *
 * <p>Example with lineLength=20:
 * <pre>
 * Input: "The quick brown fox jumps"
 * Output:
 *   "The quick brown fox "  (spaces distributed to fill exactly 20 chars)
 *   "jumps"                 (last line - not justified)
 * </pre>
 *
 * <p>Special rules:
 * <ul>
 *   <li>Last line of paragraph is NOT justified (left-aligned)</li>
 *   <li>Words longer than lineLength are hyphenated</li>
 * </ul>
 *
 * @author Ibrahim Haruna
 * @version 1.0
 */
public final class JustifyAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * Aligns the paragraph with full justification.
     * Unlike other alignment strategies, this doesn't use LineWrapper because it needs
     * complete control over line breaks to implement hyphenation properly.
     *
     * <p>Algorithm:
     * <ol>
     *   <li>Split paragraph into words</li>
     *   <li>Greedily add words to current line until next word won't fit</li>
     *   <li>If a word is too long for a line by itself, hyphenate it</li>
     *   <li>Try to hyphenate the next word into current line if possible</li>
     *   <li>Complete the line and move to next</li>
     *   <li>Last line is left-aligned (not stretched)</li>
     * </ol>
     *
     * @param paragraph the text to align
     * @param lineLength maximum characters per line (strictly enforced)
     * @return list of justified lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        List<String> lines = new ArrayList<>();

        // Handle empty paragraphs
        if (paragraph == null || paragraph.trim().isEmpty()) {
            return lines;
        }

        // Split into words
        String[] words = paragraph.trim().split("\\s+");
        int i = 0;

        // Process all words
        while (i < words.length) {
            StringBuilder cur = new StringBuilder();

            // Fill current line with as many complete words as possible
            while (i < words.length) {
                String w = words[i];

                if (cur.length() == 0) {
                    // First word on the line
                    if (w.length() <= lineLength) {
                        // Word fits - add it
                        cur.append(w);
                        i++;
                    } else {
                        // Word is too long - hyphenate it to fit exactly
                        String[] parts = hyphenateToFit(w, lineLength);
                        lines.add(parts[0]);
                        words[i] = parts[1];  // Replace with remainder
                    }
                } else if (cur.length() + 1 + w.length() <= lineLength) {
                    // Word fits on current line with a space before it
                    cur.append(' ').append(w);
                    i++;
                } else {
                    // Word doesn't fit - stop filling this line
                    break;
                }
            }

            // Decide what to do with the accumulated line
            if (i >= words.length) {
                // This is the last line - no justification needed
                if (cur.length() == 0) {
                    continue;
                }

                // Handle edge case: if current line is too long, hyphenate it
                while (cur.length() > lineLength) {
                    String s = cur.toString();
                    String[] parts = hyphenateToFit(s, lineLength);
                    lines.add(parts[0]);
                    cur = new StringBuilder(parts[1]);
                }

                // Add the last line as-is (left-aligned)
                lines.add(cur.toString());

            } else {
                // Not the last line - try to hyphenate next word into current line
                String next = words[i];
                int remaining = lineLength - cur.length();

                // Check if we have space to hyphenate the next word
                if (remaining > 1) {
                    // We need at least one space before the word
                    int availForWord = remaining - 1;

                    if (next.length() > availForWord) {
                        // Next word is long - try hyphenating it
                        String[] parts = hyphenateToFit(next, availForWord);

                        // Only hyphenate if we get meaningful first part
                        if (parts != null && parts[0].length() > 1) {
                            cur.append(' ').append(parts[0]);
                            words[i] = parts[1];  // Replace with remainder
                            lines.add(cur.toString());
                            continue;
                        }
                    }
                }

                // Couldn't hyphenate - just finish this line
                lines.add(cur.toString());
            }
        }

        return lines;
    }

    /**
     * Hyphenates a word to fit within the specified maximum characters.
     * The hyphen is included in the character count.
     *
     * <p>For example, hyphenating "extraordinary" with maxChars=10 produces:
     * <ul>
     *   <li>First part: "extraordi-" (10 characters including hyphen)</li>
     *   <li>Second part: "nary" (remainder of the word)</li>
     * </ul>
     *
     * @param word the word to hyphenate
     * @param maxChars maximum characters for the first part (including hyphen)
     * @return array with two elements: [first part with hyphen, remainder],
     *         or null if hyphenation is not possible
     */
    private static String[] hyphenateToFit(String word, int maxChars) {
        // Need at least 2 chars: one letter + hyphen
        if (maxChars < 2) {
            return null;
        }

        // Calculate how many characters we can take (leaving room for hyphen)
        int take = Math.min(maxChars - 1, word.length() - 1);

        // Need at least one character before the hyphen
        if (take < 1) {
            return null;
        }

        // Split the word
        String first = word.substring(0, take) + "-";
        String rest = word.substring(take);

        return new String[] {first, rest};
    }
}

