package io;

import java.io.FileNotFoundException;

/**
 * Interface for reading text from various sources.
 *
 * This interface abstracts away the details of where text comes from.
 *
 * This is an example of the Dependency Inversion Principle - high-level
 * code depends on this abstraction, not on concrete file operations.
 */
public interface TextReader {

    /**
     * Reads text and returns it as an array of paragraphs.
     *
     * @param source The source to read from (filename)
     * @return Array of strings where each string is a paragraph
     * @throws FileNotFoundException If the source cannot be found or accessed
     */
    String[] readText(String source) throws FileNotFoundException;
}
