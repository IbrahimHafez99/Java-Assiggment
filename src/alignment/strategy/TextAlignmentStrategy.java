package alignment.strategy;

import java.util.List;

/**
 * Strategy interface for different text alignment algorithms.
 * This interface defines the contract that all text alignment strategies must follow,
 * implementing the Strategy design pattern for text alignment.
 */
public interface TextAlignmentStrategy {

    /**
     * @param paragraph the paragraph text to be aligned (single paragraph, no line breaks)
     * @param lineLength the maximum number of characters allowed per line
     * @return a list of strings, where each string represents one formatted line
     */
    List<String> align(String paragraph, int lineLength);
}
