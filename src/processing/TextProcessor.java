package processing;

import alignment.strategy.TextAlignmentStrategy;
import java.util.List;

/**
 * Processes text by applying an alignment strategy to paragraphs.
 * This class acts as the context in the Strategy pattern, delegating the actual
 * alignment work to the configured strategy object.
 *
 * <p>The processor handles the overall structure of the output, including
 * adding line breaks and blank lines between paragraphs, while the strategy
 * handles the specific alignment logic for each paragraph.
 *
 */
public class TextProcessor {

    /**
     * The alignment strategy to use for processing.
     * We hold a reference to the interface, not a concrete class, which allows
     * us to swap strategies at runtime without changing this class.
     */
    private final TextAlignmentStrategy strategy;

    /**
     * Creates a text processor with the specified alignment strategy.
     *
     * @param strategy the alignment strategy to use for processing text
     * @throws IllegalArgumentException if strategy is null
     */
    public TextProcessor(TextAlignmentStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }

    /**
     * Processes all paragraphs using the configured strategy.
     * Each paragraph is processed independently and separated by a blank line.
     *
     * <p>The method iterates through each paragraph, applies the alignment strategy,
     * and builds the final output string with proper formatting. Each line ends with
     * a newline character, and paragraphs are separated by an additional blank line.
     *
     * @param paragraphs array of paragraph strings to process
     * @param lineLength maximum number of characters allowed per line
     * @return formatted text as a single string, ready for output
     */
    public String process(String[] paragraphs, int lineLength) {
        StringBuilder sb = new StringBuilder();

        // Process each paragraph individually
        for (int i = 0; i < paragraphs.length; i++) {
            // Apply the alignment strategy to get formatted lines
            List<String> lines = strategy.align(paragraphs[i], lineLength);

            // Add each line to the output with a newline
            for (String line : lines) {
                sb.append(line).append('\n');
            }

            // Add a blank line between paragraphs (but not after the last one)
            if (i < paragraphs.length - 1) {
                sb.append('\n');
            }
        }

        return sb.toString();
    }
}

