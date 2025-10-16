import java.util.Arrays;

import alignment.factory.AlignmentStrategyFactory;
import alignment.strategy.TextAlignmentStrategy;
import exception.InvalidArgumentException;
import processing.TextProcessor;
import util.FileUtil;

/**
 * Main class for the text alignment program.
 * Reads text from a file and formats it according to the specified alignment
 * type.
 * 
 * @author Ibrahim Hafez - 250023098
 */
public class TextAlignment {

    /**
     * Validates arguments, reads the file, applies alignment, and outputs the
     * result.
     *
     * @param args command line arguments: [filename, alignmentType, lineLength]
     */
    public static void main(String[] args) {
        try {
            // Check if the arguments are valid before continuing
            validateArgs(args);

            // Extract arguments after validation
            String filename = args[0];
            String alignArg = args[1].toLowerCase();
            int lineLength = Integer.parseInt(args[2]);

            // Read the file content into an array of paragraphs
            String[] paragraphs = FileUtil.readFile(filename);

            // Create the appropriate alignment strategy based on user input
            TextAlignmentStrategy strategy = AlignmentStrategyFactory.createStrategy(alignArg);

            // Process the paragraphs using the selected strategy
            TextProcessor processor = new TextProcessor(strategy);
            String out = processor.process(paragraphs, lineLength);

            System.out.print(out);

        } catch (InvalidArgumentException e) {
            // Display usage message if arguments are invalid
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
        }
    }

    /**
     * Validates the command line arguments.
     * Ensures correct number of arguments and valid values for alignment type and
     * line length.
     *
     * @param args the command line arguments to validate
     * @throws InvalidArgumentException if any argument is missing or invalid
     */
    private static void validateArgs(String[] args) throws InvalidArgumentException {
        // Check we have exactly three arguments
        if (args.length != 3) {
            throw new InvalidArgumentException("bad arg count");
        }

        // Check the alignment type is one we support
        String align = args[1].toLowerCase();
        if (!Arrays.asList("left", "right", "center", "centre", "justify").contains(align)) {
            throw new InvalidArgumentException("bad align");
        }

        // Check the line length is a positive integer
        try {
            int n = Integer.parseInt(args[2]);
            if (n <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("bad length");
        }
    }
}
