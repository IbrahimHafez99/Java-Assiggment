import alignment.factory.AlignmentStrategyFactory;
import alignment.strategy.TextAlignmentStrategy;
import exception.InvalidArgumentException;
import io.FileTextReader;
import io.TextReader;
import processing.TextProcessor;

import java.io.FileNotFoundException;
import java.util.Arrays;

public class TextAlignment {

    /**
     * Main entry point for the application.
     *
     * @param args Command-line arguments: [filename, alignmentType, lineLength]
     */
    public static void main(String[] args) {
        try {
            // 1. Validate arguments
            validateArguments(args);

            // 2. Parse arguments
            String filename = args[0];
            String alignmentType = args[1];
            int lineLength = Integer.parseInt(args[2]);

            // 3. Create components (demonstrating Dependency Injection)
            TextReader reader = new FileTextReader();
            TextAlignmentStrategy strategy = AlignmentStrategyFactory.createStrategy(alignmentType);
            TextProcessor processor = new TextProcessor(strategy);

            // Process the text
            String[] paragraphs = reader.readText(filename);
            String result = processor.processText(paragraphs, lineLength);

            // Output
            System.out.print(result);
            

        } catch (InvalidArgumentException e) {
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + args[0]);
        }
    }

    /**
     * Validates that command-line arguments are correct.
     *
     * Checks:
     * - Exactly 3 arguments provided
     * - Alignment type is valid (left, right, center, justify)
     * - Line length is a positive integer
     *
     * @param args The command-line arguments to validate
     * @throws InvalidArgumentException If any validation fails
     */
    private static void validateArguments(String[] args) throws InvalidArgumentException {
        // Check argument count
        if (args.length != 3) {
            throw new InvalidArgumentException("Expected 3 arguments, got " + args.length);
        }

        // Check alignment type
        String alignmentType = args[1].toLowerCase();
        if (!Arrays.asList("left", "right", "center", "centre", "justify").contains(alignmentType)) {
            throw new InvalidArgumentException("Invalid alignment type: " + args[1]);
        }

        // Check line length
        try {
            int length = Integer.parseInt(args[2]);
            if (length <= 0) {
                throw new InvalidArgumentException("Line length must be positive");
            }
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Line length must be a valid integer");
        }
    }
}