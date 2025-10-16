package alignment.strategy;

import util.LineWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Center alignment strategy - centers text within the line length.
 *
 * Each line is padded with spaces on both sides to center the text.
 * If padding is odd, the extra space goes on the right side.
 *
 * Example with lineLength=10:
 * Input: "Hello world how are you"
 * Output:
 * "  Hello   "  (2 left + "Hello" + 3 right = 10)
 * " world how"  (1 left + "world how" = 10)
 * " are you  "  (1 left + "are you" + 2 right = 10)
 */
public class CenterAlignmentStrategy implements TextAlignmentStrategy {

    @Override
    public List<String> align(String paragraph, int lineLength) {
        List<String> base = LineWrapper.wrapWords(paragraph, lineLength);
        List<String> out = new ArrayList<>(base.size());
        for (String line : base) {
            int total = Math.max(0, lineLength - line.length());
            int right = total / 2; // floor on right
            int left = total - right; // ceiling on left (gets the extra)
            out.add(repeat(' ', left) + line + repeat(' ', right));
        }
        return out;
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++)
            sb.append(c);
        return sb.toString();
    }
}
