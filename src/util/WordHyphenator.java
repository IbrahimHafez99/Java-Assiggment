package util;

/**
 * Utility class for hyphenating words that are too long for a line.
 *
 * This is specifically used by the JustifyAlignmentStrategy when a word
 * is longer than the available space on a line. The word is split at
 * the appropriate position with a hyphen added.
 *
 * Example: hyphenate("extraordinarily", 8) returns ["extraor-", "dinarily"]
 */
public final class WordHyphenator {

    /**
     * A simple container to hold the two parts of a hyphenated word.
     */
    public static class HyphenatedWord {
        private final String firstPart;
        private final String secondPart;

        public HyphenatedWord(String firstPart, String secondPart) {
            this.firstPart = firstPart;
            this.secondPart = secondPart;
        }

        public String getFirstPart() {
            return firstPart;
        }

        public String getSecondPart() {
            return secondPart;
        }
    }

    /**
     * Hyphenates a word to fit within the specified maximum length.
     *
     * The word is split such that the first part (including hyphen) fits
     * within maxLength characters.
     *
     * @param word The word to hyphenate
     * @param maxLength The maximum length for the first part (including hyphen)
     * @return A HyphenatedWord containing both parts, or null if word is too short
     *
     * Example: hyphenate("wonderful", 6) returns HyphenatedWord("wonde-", "rful")
     */
    public static HyphenatedWord hyphenate(String word, int maxLength) {
        // Can't hyphenate if there's no room for at least one character + hyphen
        if (maxLength < 2 || word.length() < TextConstants.MIN_WORD_LENGTH_FOR_HYPHENATION) {
            return null;
        }

        // Calculate where to split (leaving room for the hyphen)
        int splitPosition = maxLength - 1; // -1 for the hyphen

        // Make sure we don't split beyond the word
        if (splitPosition >= word.length()) {
            return null;
        }

        String firstPart = word.substring(0, splitPosition) + TextConstants.HYPHEN;
        String secondPart = word.substring(splitPosition);

        return new HyphenatedWord(firstPart, secondPart);
    }

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static methods.
     */
    private WordHyphenator() {
        throw new UnsupportedOperationException("WordHyphenator is a utility class and should not be instantiated");
    }
}
