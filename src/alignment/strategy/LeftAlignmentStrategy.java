package alignment.strategy;

import util.LineWrapper;
import java.util.List;

/**
 * Left alignment strategy - aligns text to the left margin.
 *
 * This is the simplest alignment type. Text starts at the beginning of each
 * line
 * with no padding or special formatting. Words are wrapped at the line length.
 *
 * Example with lineLength=10:
 * Input: "Hello world how are you"
 * Output:
 * "Hello"
 * "world how"
 * "are you"
 */
public class LeftAlignmentStrategy implements TextAlignmentStrategy {

    @Override
    public List<String> align(String paragraph, int lineLength) {
        return LineWrapper.wrapWords(paragraph, lineLength);
    }
}
