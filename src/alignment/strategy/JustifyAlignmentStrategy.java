package alignment.strategy;

import util.TextConstants;
import util.WordHyphenator;
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

    /**
     * Aligns the paragraph with full justification.
     *
     * ALGORITHM OVERVIEW:
     * 1. Split paragraph into words
     * 2. Greedily add words to a line until the next word won't fit
     * 3. When a line is full, distribute spaces evenly between words
     * 4. If a word is too long for a line, hyphenate it
     * 5. Leave the last line left-aligned (standard typography rule)
     *
     * @param paragraph The text to align
     * @param lineLength Maximum characters per line
     * @return List of justified lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // Handle empty paragraphs
        if (paragraph == null || paragraph.trim().isEmpty()) {
            return new ArrayList<>();
        }

        // Split into words using whitespace as delimiter
        String[] words = paragraph.trim().split(TextConstants.WORD_SPLIT_PATTERN);

        List<String> lines = new ArrayList<>();          // Final output lines
        List<String> currentLineWords = new ArrayList<>(); // Words being accumulated for current line
        int currentLineLength = 0;                        // Running total of characters on current line

        // Process each word
        int wordIndex = 0;
        while (wordIndex < words.length) {
            String word = words[wordIndex];

            // Calculate what the line length would be if we add this word
            // Formula: current length + (space if not first word) + word length
            int lengthWithWord = currentLineLength +
                               (currentLineWords.isEmpty() ? 0 : 1) +
                               word.length();

            if (lengthWithWord <= lineLength) {
                // ✓ Word fits on current line
                currentLineWords.add(word);
                currentLineLength = lengthWithWord;
                wordIndex++;
            } else {
                // ✗ Word doesn't fit on current line

                if (currentLineWords.isEmpty()) {
                    // SPECIAL CASE: This is the first word on the line and it's too long
                    // Solution: Hyphenate the word
                    // Example: "extraordinary" with lineLength=10 → "extraordi-" + "nary"
                    WordHyphenator.HyphenatedWord hyphenated =
                        WordHyphenator.hyphenate(word, lineLength);

                    if (hyphenated != null) {
                        // Successfully hyphenated
                        // Add the first part (with hyphen) as a complete line
                        lines.add(hyphenated.getFirstPart());

                        // Replace the current word with its second part for next iteration
                        // This allows us to continue processing the remainder
                        words[wordIndex] = hyphenated.getSecondPart();

                        // Don't increment wordIndex - we'll process the second part next
                    } else {
                        // Word can't be hyphenated (probably too short)
                        // Just add it as is, even if it exceeds line length
                        lines.add(word);
                        wordIndex++;
                    }
                } else {
                    // NORMAL CASE: We have words accumulated on the current line
                    // Complete the current line by justifying it
                    String justifiedLine = justifyLine(currentLineWords, lineLength, false);
                    lines.add(justifiedLine);

                    // Reset for next line
                    currentLineWords.clear();
                    currentLineLength = 0;

                    // Don't increment wordIndex - we'll try to add this word to the next line
                }
            }
        }

        // Handle any remaining words (this is the last line)
        // Important: Last line should NOT be justified - keep it left-aligned
        if (!currentLineWords.isEmpty()) {
            String lastLine = justifyLine(currentLineWords, lineLength, true);
            lines.add(lastLine);
        }

        return lines;
    }

    /**
     * Justifies a line by distributing spaces evenly between words.
     *
     * ALGORITHM STEPS:
     * 1. Check if justification is needed (skip for last line or single word)
     * 2. Calculate total space that needs to be distributed
     * 3. Divide spaces evenly among gaps between words
     * 4. Distribute any extra spaces to leftmost gaps
     * 5. Build the final line with calculated spacing
     *
     * EXAMPLE:
     * Words: ["The", "quick", "fox"]
     * Line Length: 20
     * Word lengths: 3 + 5 + 3 = 11
     * Spaces needed: 20 - 11 = 9
     * Gaps: 2 (between The-quick and quick-fox)
     * Spaces per gap: 9 / 2 = 4 with remainder 1
     * Gap 1 gets: 4 + 1 = 5 spaces (gets the extra)
     * Gap 2 gets: 4 spaces
     * Result: "The     quick    fox" (exactly 20 chars)
     *
     * @param words The words to justify
     * @param lineLength The target length for the line
     * @param isLastLine If true, don't justify (just left-align)
     * @return The justified line
     */
    private String justifyLine(List<String> words, int lineLength, boolean isLastLine) {
        // Don't justify if it's the last line or if there's only one word
        // Typography rule: Last lines and single-word lines look better left-aligned
        if (isLastLine || words.size() == 1) {
            return String.join(TextConstants.SPACE, words);
        }

        // STEP 1: Calculate total length of all words (without any spaces)
        int totalWordLength = 0;
        for (String word : words) {
            totalWordLength += word.length();
        }

        // STEP 2: Calculate how many spaces we need to distribute
        // This is the "leftover" space after placing all words
        int totalSpacesNeeded = lineLength - totalWordLength;

        // STEP 3: Determine how many gaps exist between words
        // For n words, there are n-1 gaps
        // Example: "The quick fox" has 2 gaps: The_quick and quick_fox
        int gapCount = words.size() - 1;

        // STEP 4: Distribute spaces evenly across gaps
        // Integer division gives us the base number of spaces per gap
        int spacesPerGap = totalSpacesNeeded / gapCount;

        // The remainder tells us how many gaps need one extra space
        // Example: 9 spaces across 2 gaps = 4 per gap with 1 extra
        int extraSpaces = totalSpacesNeeded % gapCount;

        // STEP 5: Build the justified line
        StringBuilder line = new StringBuilder();
        for (int i = 0; i < words.size(); i++) {
            // Add the word
            line.append(words.get(i));

            // Add spaces after each word except the last
            if (i < words.size() - 1) {
                // Start with base spaces for this gap
                int spacesForThisGap = spacesPerGap;

                // Add one extra space to the first 'extraSpaces' gaps
                // This ensures extra spaces are distributed left-to-right
                // Example: If extraSpaces = 2, gaps 0 and 1 get an extra space
                if (i < extraSpaces) {
                    spacesForThisGap++;
                }

                // Append the calculated number of spaces
                for (int j = 0; j < spacesForThisGap; j++) {
                    line.append(TextConstants.SPACE);
                }
            }
        }

        return line.toString();
    }
}
