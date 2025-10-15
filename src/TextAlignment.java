public class TextAlignment {
    public static void main(String[] args) {
        try {
            //Validate arguments
            validateArguments(args);

            //Parse arguments
            String filename = args[0];
            String alignmentType = args[1];
            int lineLength = Integer.parseInt(args[2]);

            //Create components
            TextReader reader = new FileTextReader();
            TextAlignmentStrategy strategy = AlignmentStrategyFactory.createStrategy(alignmentType);
            TextProcessor processor = new TextProcessor(strategy);

            // 4. Process
            String[] paragraphs = reader.readText(filename);
            String result = processor.processText(paragraphs, lineLength);

            // 5. Output
            System.out.print(result);

        } catch (InvalidArgumentException e) {
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + args[0]);
        }
    }

    private static void validateArguments(String[] args) throws InvalidArgumentException {
        if (args.length != 3) throw new InvalidArgumentException("Need 3 arguments");

        if (!Arrays.asList("left", "right", "center", "justify").contains(args[1].toLowerCase())) {
            throw new InvalidArgumentException("Invalid alignment type");
        }

        try {
            int length = Integer.parseInt(args[2]);
            if (length <= 0) throw new InvalidArgumentException("Line length must be positive");
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Line length must be a number");
        }
    }
}