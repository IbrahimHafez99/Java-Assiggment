import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

class TextAlignment {
    public static void main(String[] args) {
        try {
            validateArgs(args);
            String filename = args[0];
            String alignArg = args[1].toLowerCase();
            int lineLength = Integer.parseInt(args[2]);

            String[] paragraphs = FileUtil.readFile(filename);

            TextAlignmentStrategy strategy = AlignmentStrategyFactory.create(alignArg);
            TextProcessor processor = new TextProcessor(strategy);
            String out = processor.process(paragraphs, lineLength);
            System.out.print(out);
        } catch (InvalidArgumentException e) {
            System.out.println("usage: java TextAlignment <filename> <alignmentType> <lineLength>");
        } catch (FileNotFoundException e) {
            // Mimic expected format: "File not found: <path> (No such file or directory)"
            System.out.println("File not found: " + e.getMessage());
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

class InvalidArgumentException extends Exception {
    InvalidArgumentException(String msg) {
        super(msg);
    }
}

final class FileUtil {
    private FileUtil() {
    }

    public static String[] readFile(String filename) throws FileNotFoundException {
        File f = new File(filename);
        if (!f.exists()) {
            // Throw with the standard Java-style message that tests expect
            throw new FileNotFoundException(filename + " (No such file or directory)");
        }
        try (Scanner sc = new Scanner(f)) {
            StringBuilder current = new StringBuilder();
            java.util.List<String> paras = new java.util.ArrayList<>();
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.trim().isEmpty()) {
                    if (current.length() > 0) {
                        paras.add(current.toString());
                        current.setLength(0);
                    }
                } else {
                    if (current.length() > 0)
                        current.append(' ');
                    current.append(line.trim());
                }
            }
            if (current.length() > 0)
                paras.add(current.toString());
            return paras.toArray(new String[0]);
        }
    }
}

class TextProcessor {
    private final TextAlignmentStrategy strategy;

    TextProcessor(TextAlignmentStrategy strategy) {
        this.strategy = strategy;
    }

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

interface TextAlignmentStrategy {
    List<String> align(String paragraph, int lineLength);
}

final class AlignmentStrategyFactory {
    private AlignmentStrategyFactory() {
    }

    public static TextAlignmentStrategy create(String alignArg) {
        switch (alignArg) {
            case "left":
                return new LeftAlignment();
            case "right":
                return new RightAlignment();
            case "center":
            case "centre":
                return new CenterAlignment();
            case "justify":
                return new JustifyAlignment();
            default:
                throw new IllegalArgumentException("unknown");
        }
    }
}

final class Wrap {
    private Wrap() {
    }

    public static java.util.List<String> words(String paragraph, int maxLen) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (paragraph == null || paragraph.trim().isEmpty())
            return lines;
        String[] words = paragraph.trim().split("\\s+");
        StringBuilder cur = new StringBuilder();
        for (String w : words) {
            if (cur.length() == 0) {
                cur.append(w);
            } else if (cur.length() + 1 + w.length() <= maxLen) {
                cur.append(' ').append(w);
            } else {
                lines.add(cur.toString());
                cur.setLength(0);
                // If single word longer than maxLen, put alone (allowed for non-justify)
                cur.append(w);
            }
        }
        if (cur.length() > 0)
            lines.add(cur.toString());
        return lines;
    }
}

final class LeftAlignment implements TextAlignmentStrategy {
    public java.util.List<String> align(String paragraph, int lineLength) {
        return Wrap.words(paragraph, lineLength);
    }
}

final class RightAlignment implements TextAlignmentStrategy {
    public java.util.List<String> align(String paragraph, int lineLength) {
        java.util.List<String> base = Wrap.words(paragraph, lineLength);
        java.util.List<String> out = new java.util.ArrayList<>(base.size());
        for (String line : base) {
            int pad = Math.max(0, lineLength - line.length());
            out.add(repeat(' ', pad) + line);
        }
        return out;
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++)
            sb.append(c);
        return sb.toString();
    }
}

final class CenterAlignment implements TextAlignmentStrategy {
    public java.util.List<String> align(String paragraph, int lineLength) {
        java.util.List<String> base = Wrap.words(paragraph, lineLength);
        java.util.List<String> out = new java.util.ArrayList<>(base.size());
        for (String line : base) {
            int total = Math.max(0, lineLength - line.length());
            int left = total / 2; // floor on left
            int right = total - left;
            out.add(repeat(' ', left) + line + repeat(' ', right));
        }
        return out;
    }

    private static String repeat(char c, int n) {
        StringBuilder sb = new StringBuilder(n);
        for (int i = 0; i < n; i++)
            sb.append(c);
        return sb.toString();
    }
}

final class JustifyAlignment implements TextAlignmentStrategy {
    public java.util.List<String> align(String paragraph, int lineLength) {
        java.util.List<String> lines = new java.util.ArrayList<>();
        if (paragraph == null || paragraph.trim().isEmpty())
            return lines;
        String[] words = paragraph.trim().split("\\s+");
        int i = 0;
        while (i < words.length) {
            StringBuilder cur = new StringBuilder();
            // fill as many whole words as will fit
            while (i < words.length) {
                String w = words[i];
                if (cur.length() == 0) {
                    if (w.length() <= lineLength) {
                        cur.append(w);
                        i++;
                    } else {
                        // word longer than line, hyphenate to exact fit
                        String[] parts = hyphenateToFit(w, lineLength);
                        lines.add(parts[0]);
                        words[i] = parts[1];
                    }
                } else if (cur.length() + 1 + w.length() <= lineLength) {
                    cur.append(' ').append(w);
                    i++;
                } else {
                    break;
                }
            }
            if (i >= words.length) {
                // last line: as-is (no justification, but must not exceed length)
                if (cur.length() == 0)
                    continue;
                // If a single word is longer than lineLength (rare here), hyphenate in chunks
                while (cur.length() > lineLength) {
                    String s = cur.toString();
                    String[] parts = hyphenateToFit(s, lineLength);
                    lines.add(parts[0]);
                    cur = new StringBuilder(parts[1]);
                }
                lines.add(cur.toString());
            } else {
                // Next word didn't fit. Decide if we should hyphenate it into the current line.
                String next = words[i];
                int remaining = lineLength - cur.length();
                // there's at least 1 space needed if we want to add any part of next
                if (remaining > 1) {
                    int availForWord = remaining - 1; // leave one for space before it
                    if (next.length() > availForWord) {
                        String[] parts = hyphenateToFit(next, availForWord);
                        // Only add if split yields at least one character before hyphen
                        if (parts != null && parts[0].length() > 1) {
                            cur.append(' ').append(parts[0]);
                            words[i] = parts[1];
                            lines.add(cur.toString());
                            continue;
                        }
                    }
                }
                // Otherwise, finish this line and continue with next
                lines.add(cur.toString());
            }
        }
        return lines;
    }

    private static String[] hyphenateToFit(String word, int maxChars) {
        // maxChars is the maximum characters for the piece INCLUDING the trailing
        // hyphen
        if (maxChars < 2)
            return null;
        int take = Math.min(maxChars - 1, word.length() - 1);
        if (take < 1)
            return null;
        String first = word.substring(0, take) + "-";
        String rest = word.substring(take);
        return new String[] { first, rest };
    }
}