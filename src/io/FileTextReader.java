package io;

import util.FileUtil;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads text from files and organizes it into paragraphs.
 */
public class FileTextReader implements TextReader {

    /**
     * Reads a text file and returns its contents as an array of paragraphs.
     *
     * Uses FileUtil.readFile() to read the file line-by-line, then processes
     * the lines to group them into paragraphs based on blank line.
     *
     * @param filename The path to the file to read
     * @return Array of paragraphs (each paragraph is a single string)
     * @throws FileNotFoundException If the file doesn't exist at the specified path
     */
    @Override
    public String[] readText(String filename) throws FileNotFoundException {
        // it returns an array of lines
        String[] lines = FileUtil.readFile(filename);

        // Handle error case where FileUtil returns empty string array
        if (lines.length == 1 && lines[0].equals("")) {
            throw new FileNotFoundException(filename);
        }

        // Process lines into paragraphs
        List<String> paragraphs = new ArrayList<>();
        StringBuilder currentParagraph = new StringBuilder();

        for (String line : lines) {
            // Check if this is a blank line
            if (line.trim().isEmpty()) {
                // If we have accumulated text, save it as a paragraph
                if (currentParagraph.length() > 0) {
                    paragraphs.add(currentParagraph.toString());
                    currentParagraph = new StringBuilder();
                }
                // If currentParagraph is empty, we're seeing consecutive blank lines - just
                // skip
            } else {
                // Non-blank line - add to current paragraph
                if (currentParagraph.length() > 0) {
                    // Add space between lines within a paragraph
                    currentParagraph.append(" ");
                }
                currentParagraph.append(line.trim());
            }
        }

        // Don't forget the last paragraph if file doesn't end with blank line
        if (currentParagraph.length() > 0) {
            paragraphs.add(currentParagraph.toString());
        }

        return paragraphs.toArray(new String[0]);
    }
}
