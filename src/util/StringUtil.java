package util;

/**
 * Utility class for common string operations.
 */
public final class StringUtil {

    /**
     * Repeats a character n times and returns the resulting string.
     *
     * @param c the character to repeat
     * @param n the number of times to repeat the character (must be non-negative)
     * @return a string containing the character repeated n times
     * @throws IllegalArgumentException if n is negative
     */
    public static String repeat(char c, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Repeat count cannot be negative: " + n);
        }

        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
