package util;

/**
 * Constants used throughout the text alignment application.
 *
 * This class centralizes all the magic strings and numbers we use across different
 * parts of the application. This makes the code more maintainable because:
 * - If we want to change the hyphen character, we only change it here
 * - It's clear what values are configurable vs. hard-coded
 * - It prevents typos in string literals scattered across the code
 */
public class TextConstants {

    /**
     * The character used to hyphenate words when they need to be split.
     * Used by the justify alignment strategy when a word is too long for a line.
     */
    public static final String HYPHEN = "-";

    /**
     * A single space character.
     * Used for padding and word separation throughout the alignment strategies.
     */
    public static final String SPACE = " ";

    /**
     * Minimum word length that can be hyphenated.
     * Words shorter than this will not be split, even if they don't fit on a line.
     * This prevents silly hyphenations like "a-" or "th-e".
     */
    public static final int MIN_WORD_LENGTH_FOR_HYPHENATION = 4;

    /**
     * Regular expression pattern to split text into words.
     * This splits on any sequence of whitespace characters (spaces, tabs, newlines).
     */
    public static final String WORD_SPLIT_PATTERN = "\\s+";

    /**
     * Private constructor to prevent instantiation.
     * This is a utility class with only static members, so no instances should be created.
     */
    private TextConstants() {
        throw new UnsupportedOperationException("TextConstants is a utility class and should not be instantiated");
    }
}
