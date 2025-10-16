package alignment.strategy;

import util.LineWrapper;
import util.StringUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Each line is padded with spaces on the left to push the text to the right
 * edge,
 * so all lines end at the same position.
 */
public final class RightAlignmentStrategy implements TextAlignmentStrategy {

    /**
     * First wraps the text at word boundaries, then adds left padding to each line.
     * 
     * @param paragraph  the text to align
     * @param lineLength maximum characters per line
     * @return list of right-aligned lines
     */
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // First, wrap the text at word boundaries
        List<String> base = LineWrapper.wrapWords(paragraph, lineLength);
        List<String> out = new ArrayList<>(base.size());

        // Add left padding to each line to align text to the right
        for (String line : base) {
            // Calculate spaces needed: lineLength - actual line length
            int pad = Math.max(0, lineLength - line.length());
            // Add padding before the text
            out.add(StringUtil.repeat(' ', pad) + line);
        }

        return out;
    }
}
