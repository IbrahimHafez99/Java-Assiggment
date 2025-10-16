# Text Alignment Program

A Java application that reads text from a file and formats it according to different alignment styles.

## Author
Ibrahim Haruna

## Overview
This program takes a text file and applies one of four alignment types (left, right, center, or justify) to format the text within a specified line length.

## Usage
```bash
java TextAlignment <filename> <alignmentType> <lineLength>
```

**Arguments:**
- `filename` - Path to the text file to process
- `alignmentType` - One of: `left`, `right`, `center`, `centre`, or `justify`
- `lineLength` - Maximum characters per line (must be positive)

**Example:**
```bash
java TextAlignment sample.txt justify 80
```

## Architecture

The program follows object-oriented design principles using two main design patterns and dependency injection:

### 1. Strategy Pattern
The Strategy pattern allows the program to switch between different alignment algorithms at runtime without changing the core processing logic.

**Components:**
- `TextAlignmentStrategy` (interface) - Defines the contract for all alignment strategies
- `LeftAlignmentStrategy` - Aligns text to the left margin
- `RightAlignmentStrategy` - Aligns text to the right margin
- `CenterAlignmentStrategy` - Centers text within the line length
- `JustifyAlignmentStrategy` - Stretches text to fill the entire line

### 2. Factory Pattern
The Factory pattern centralizes the creation of strategy objects based on user input.

**Component:**
- `AlignmentStrategyFactory` - Creates the appropriate strategy based on the alignment type string

### 3. Dependency Injection
The `TextProcessor` class uses Constructor Dependency Injection. Instead of creating its own strategy object, it receives the strategy through its constructor:

```java
public TextProcessor(TextAlignmentStrategy strategy) {
    this.strategy = strategy;
}
```

**Benefits:**
- **Loose Coupling** - TextProcessor depends on the interface, not concrete classes
- **Flexibility** - We can inject any strategy implementation at runtime
- **Testability** - Easy to inject mock strategies for unit testing

### Project Structure
```
src/
├── TextAlignment.java              # Main entry point
├── alignment/
│   ├── factory/
│   │   └── AlignmentStrategyFactory.java
│   └── strategy/
│       ├── TextAlignmentStrategy.java
│       ├── LeftAlignmentStrategy.java
│       ├── RightAlignmentStrategy.java
│       ├── CenterAlignmentStrategy.java
│       └── JustifyAlignmentStrategy.java
├── processing/
│   └── TextProcessor.java          # Applies strategy to paragraphs
├── util/
│   ├── FileUtil.java               # Handles file reading
│   └── LineWrapper.java            # Wraps text at word boundaries
└── exception/
    ├── InvalidArgumentException.java
    └── TextProcessingException.java
```

## How It Works

1. **Input Validation** - The main class validates command line arguments
2. **File Reading** - FileUtil reads the file into an array of paragraphs
3. **Strategy Selection** - AlignmentStrategyFactory creates the appropriate strategy
4. **Text Processing** - TextProcessor applies the strategy to each paragraph
5. **Output** - Formatted text is printed to standard output

## Key Features

### Line Wrapping
All alignment types use word wrapping to ensure words aren't split in the middle (except for justify mode with hyphenation).

### Justification with Hyphenation
The justify alignment is the most complex:
- Words that are too long for a line are hyphenated
- The hyphen is included in the line length calculation
- The last line of each paragraph is left-aligned (standard typography)

### Paragraph Handling
- Each paragraph is processed independently
- Paragraphs are separated by blank lines in the output
- Empty paragraphs are handled gracefully

## Design Benefits

**Maintainability** - Each alignment algorithm is in its own class, making the code easy to understand and modify.

**Extensibility** - Adding a new alignment type only requires:
1. Creating a new strategy class that implements TextAlignmentStrategy
2. Adding a case to the factory's switch statement

**Testability** - Each strategy can be tested independently without affecting others.

**Separation of Concerns** - The main class handles input/output, the factory handles object creation, the processor handles iteration, and strategies handle alignment logic.

## Compiling and Running

```bash
# Compile
javac -d bin src/**/*.java

# Run
java -cp bin TextAlignment input.txt left 80
```
