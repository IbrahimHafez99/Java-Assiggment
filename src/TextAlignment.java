import java.util.Arrays;

import alignment.factory.AlignmentStrategyFactory;
import alignment.strategy.TextAlignmentStrategy;
import exception.InvalidArgumentException;
import processing.TextProcessor;
import util.FileUtil;

public class TextAlignment {
    public static void main(String[] args) {
        try {
            validateArgs(args);
            String filename = args[0];
            String alignArg = args[1].toLowerCase();
            int lineLength = Integer.parseInt(args[2]);

            String[] paragraphs = FileUtil.readFile(filename);

            TextAlignmentStrategy strategy = AlignmentStrategyFactory.createStrategy(alignArg);
            TextProcessor processor = new TextProcessor(strategy);
            String out = processor.process(paragraphs, lineLength);
            System.out.print(out);
        } catch (InvalidArgumentException e) {
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
        }
    }

    private static void validateArgs(String[] args) throws InvalidArgumentException {
        if (args.length != 3) {
            throw new InvalidArgumentException("bad arg count");
        }
        String align = args[1].toLowerCase();
        if (!Arrays.asList("left", "right", "center", "centre", "justify").contains(align)) {
            throw new InvalidArgumentException("bad align");
        }
        try {
            int n = Integer.parseInt(args[2]);
            if (n <= 0)
                throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            throw new InvalidArgumentException("bad length");
        }
    }
}