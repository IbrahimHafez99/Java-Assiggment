package processing;

import alignment.strategy.TextAlignmentStrategy;
import java.util.List;

/**
 * Processes text by applying an alignment strategy to paragraphs.
 */
public class TextProcessor {

    /**
     * The alignment strategy to use for processing.
     * we hold a reference to the interface, not a concrete class, so we can swap
     * strategies at runtime.
     */
    private final TextAlignmentStrategy strategy;

    /**
     * Creates a text processor with the specified alignment strategy.
     *
     * @param strategy The alignment strategy to use
     */
    public TextProcessor(TextAlignmentStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    /**
     * Processes all paragraphs using the configured strategy.
     *
     * Each paragraph is processed independently and then joined with a blank line
     * between them. The output is formatted as a single string ready for printing.
     *
     * @param paragraphs Array of paragraph strings to process
     * @param lineLength Maximum characters per line
     * @return Formatted text ready for output
     */
    public String processText(String[] paragraphs, int lineLength) {
        if (paragraphs == null || paragraphs.length == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();

        // Process each paragraph
        for (int i = 0; i < paragraphs.length; i++) {
            String paragraph = paragraphs[i];

            // Skip empty paragraphs
            if (paragraph == null || paragraph.trim().isEmpty()) {
                continue;
            }

            // Use the strategy to align this paragraph
            List<String> alignedLines = strategy.align(paragraph, lineLength);

            // Add each line to the result
            for (String line : alignedLines) {
                result.append(line).append("\n");
            }

            // Add blank line between paragraphs (but not after the last one)
            if (i < paragraphs.length - 1) {
                result.append("\n");
            }
        }

        return result.toString();
    }
}
