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
    public String process(String[] paragraphs, int lineLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < paragraphs.length; i++) {
            List<String> lines = strategy.align(paragraphs[i], lineLength);
            for (String line : lines) {
                sb.append(line).append('\n');
            }
            if (i < paragraphs.length - 1)
                sb.append('\n');
        }
        return sb.toString();
    }
}
