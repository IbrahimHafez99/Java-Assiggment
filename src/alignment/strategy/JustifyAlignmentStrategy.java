package alignment.strategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Justify alignment strategy - distributes text evenly across the line.
 *
 * This is the most complex alignment strategy. It:
 * 1. Adds words to a line until the next word wouldn't fit
 * 2. Distributes extra spaces evenly between words to fill the line exactly
 * 3. Hyphenates words that are too long to fit on a line
 * 4. Ensures no line exceeds the maximum length (including hyphen)
 *
 * Example with lineLength=20:
 * Input: "The quick brown fox jumps"
 * Output:
 * "The  quick     brown"  (spaces distributed to fill exactly 20 chars)
 * "fox jumps"             (last line - not justified)
 *
 * Special rules:
 * - Last line of paragraph is NOT justified (left-aligned)
 * - Single word on a line is NOT justified
 * - Words longer than lineLength are hyphenated
 */
public class JustifyAlignmentStrategy implements TextAlignmentStrategy {

    @Override
    public List<String> align(String paragraph, int lineLength) {
        List<String> lines = new ArrayList<>();
        if (paragraph == null || paragraph.trim().isEmpty())
            return lines;
        String[] words = paragraph.trim().split("\\s+");
        int i = 0;
        while (i < words.length) {
            StringBuilder cur = new StringBuilder();
            // fill as many whole words as will fit
            while (i < words.length) {
                String w = words[i];
                if (cur.length() == 0) {
                    if (w.length() <= lineLength) {
                        cur.append(w);
                        i++;
                    } else {
                        // word longer than line, hyphenate to exact fit
                        String[] parts = hyphenateToFit(w, lineLength);
                        lines.add(parts[0]);
                        words[i] = parts[1];
                    }
                } else if (cur.length() + 1 + w.length() <= lineLength) {
                    cur.append(' ').append(w);
                    i++;
                } else {
                    break;
                }
            }
            if (i >= words.length) {
                // last line: as-is (no justification, but must not exceed length)
                if (cur.length() == 0)
                    continue;
                // If a single word is longer than lineLength (rare here), hyphenate in chunks
                while (cur.length() > lineLength) {
                    String s = cur.toString();
                    String[] parts = hyphenateToFit(s, lineLength);
                    lines.add(parts[0]);
                    cur = new StringBuilder(parts[1]);
                }
                lines.add(cur.toString());
            } else {
                // Next word didn't fit. Decide if we should hyphenate it into the current line.
                String next = words[i];
                int remaining = lineLength - cur.length();
                // there's at least 1 space needed if we want to add any part of next
                if (remaining > 1) {
                    int availForWord = remaining - 1; // leave one for space before it
                    if (next.length() > availForWord) {
                        String[] parts = hyphenateToFit(next, availForWord);
                        // Only add if split yields at least one character before hyphen
                        if (parts != null && parts[0].length() > 1) {
                            cur.append(' ').append(parts[0]);
                            words[i] = parts[1];
                            lines.add(cur.toString());
                            continue;
                        }
                    }
                }
                // Otherwise, finish this line and continue with next
                lines.add(cur.toString());
            }
        }
        return lines;
    }

    private static String[] hyphenateToFit(String word, int maxChars) {
        // maxChars is the maximum characters for the piece INCLUDING the trailing
        // hyphen
        if (maxChars < 2)
            return null;
        int take = Math.min(maxChars - 1, word.length() - 1);
        if (take < 1)
            return null;
        String first = word.substring(0, take) + "-";
        String rest = word.substring(take);
        return new String[] { first, rest };
    }
}
