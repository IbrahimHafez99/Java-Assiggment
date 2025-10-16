package alignment.strategy;

import util.LineWrapper;
import java.util.ArrayList;
import java.util.List;

/**
 * Right alignment strategy - aligns text to the right margin.
 *
 * Each line is padded with spaces on the left to push the text to the right edge.
 * The text ends at position lineLength (or just before it for shorter final lines).
 *
 * Example with lineLength=10:
 * Input: "Hello world how are you"
 * Output:
 * "     Hello"  (5 spaces + "Hello")
 * " world how"  (1 space + "world how")
 * "   are you"  (3 spaces + "are you")
 */
public class RightAlignmentStrategy implements TextAlignmentStrategy {

    @Override
    public List<String> align(String paragraph, int lineLength) {
        List<String> base = LineWrapper.wrapWords(paragraph, lineLength);
        List<String> out = new ArrayList<>(base.size());
        for (String line : base) {
            int pad = Math.max(0, lineLength - line.length());
            out.add(repeat(' ', pad) + line);
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
