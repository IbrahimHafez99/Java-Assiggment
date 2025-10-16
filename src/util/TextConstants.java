package util;

/**
 * Constants used throughout the text alignment application.
 *
 * This class centralizes all the magic strings and numbers we use across
 * different
 */
public final class TextConstants {

    /**
     * The character used to hyphenate words when they need to be split.
     * Used by the justify alignment strategy when a word is too long for a line.
     */
    public static final String HYPHEN = "-";

    /**
     * Minimum word length that can be hyphenated.
     * Words shorter than this will not be split, even if they don't fit on a line.
     */
    public static final int MIN_WORD_LENGTH_FOR_HYPHENATION = 4;

}
