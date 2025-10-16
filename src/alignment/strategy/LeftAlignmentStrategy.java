package alignment.strategy;

import util.LineWrapper;
import java.util.List;

/**
 * Left alignment strategy - aligns text to the left margin.
 * where text starts at the beginning of each line
 * with no padding or special formatting. Words are wrapped at the line length.
 */
public final class LeftAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * @param paragraph  the text to align
     * @param lineLength maximum characters per line
     * @return list of left-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // simply wrap words without any padding
        return LineWrapper.wrapWords(paragraph, lineLength);
    }
}
