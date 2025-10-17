# Text Alignment System - OOP Design

**Author:** Ibrahim Hafez - 250023098
**Module:** CS5001 Object-Oriented Modelling, Design and Programming

---

## Overview

A text alignment system that demonstrates enterprise-grade OOP design through the implementation of a flexible, extensible architecture for formatting text paragraphs.

---

## Design Patterns

### Strategy Pattern (Primary)

The core architectural pattern enabling runtime selection of alignment algorithms.

**Structure:**
- **Interface:** [TextAlignmentStrategy.java](src/alignment/strategy/TextAlignmentStrategy.java) - Defines the alignment contract
- **Implementations:**
  - [LeftAlignmentStrategy.java](src/alignment/strategy/LeftAlignmentStrategy.java)
  - [RightAlignmentStrategy.java](src/alignment/strategy/RightAlignmentStrategy.java)
  - [CenterAlignmentStrategy.java](src/alignment/strategy/CenterAlignmentStrategy.java)
  - [JustifyAlignmentStrategy.java](src/alignment/strategy/JustifyAlignmentStrategy.java) - Advanced implementation with word hyphenation
- **Context:** [TextProcessor.java](src/processing/TextProcessor.java) - Delegates alignment to injected strategy

**Benefits:**
- Runtime algorithm selection through polymorphism
- Easy to add new alignment types without modifying existing code
- Each strategy can be tested independently

### Factory Pattern

[AlignmentStrategyFactory.java](src/alignment/factory/AlignmentStrategyFactory.java) encapsulates strategy creation logic:
- Handles case-insensitive input normalization
- Centralizes object creation
- Supports both "centre" and "center" spellings

### Dependency Injection

Constructor-based DI in [TextProcessor.java](src/processing/TextProcessor.java:9):

```java
public class TextProcessor {
    private final TextAlignmentStrategy strategy;  // Injected dependency

    public TextProcessor(TextAlignmentStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Strategy cannot be null");
        }
        this.strategy = strategy;
    }
}
```

**Achieved:**
- Loose coupling - depends on abstraction, not concrete implementations
- Immutability through `final` fields
- Testability through dependency injection
- Null safety validation

---

## SOLID Principles

### Single Responsibility Principle
Each class has one reason to change:
- `LeftAlignmentStrategy` - Left alignment logic only
- `FileUtil` - File I/O operations only
- `LineWrapper` - Word wrapping logic only
- `TextProcessor` - Orchestration only

### Open/Closed Principle
Open for extension (add new strategies), closed for modification (existing code unchanged):
- New alignment types added by implementing `TextAlignmentStrategy`
- Factory handles registration

### Liskov Substitution Principle
All strategies are interchangeable:
```java
TextAlignmentStrategy strategy = /* any implementation */;
TextProcessor processor = new TextProcessor(strategy);
```

### Interface Segregation Principle
Minimal, focused interface with single method:
```java
public interface TextAlignmentStrategy {
    List<String> align(String paragraph, int lineLength);
}
```

### Dependency Inversion Principle
High-level modules depend on abstractions:
```java
private final TextAlignmentStrategy strategy;  // Interface, not concrete class
```

---

## Architecture

### Package Structure
```
src/
├── TextAlignment.java              # Entry point
├── alignment/
│   ├── factory/
│   │   └── AlignmentStrategyFactory.java
│   └── strategy/
│       ├── TextAlignmentStrategy.java (interface)
│       └── [4 concrete implementations]
├── processing/
│   └── TextProcessor.java          # Strategy context
├── util/
│   ├── FileUtil.java               # File I/O
│   ├── LineWrapper.java            # Word wrapping
│   ├── StringUtil.java             # String utilities
│   ├── TextConstants.java          # Constants
│   └── WordHyphenator.java         # Hyphenation logic
└── exception/
    ├── InvalidArgumentException.java
    └── TextProcessingException.java
```

### Separation of Concerns
- **Alignment logic:** Strategy classes
- **Line wrapping:** `LineWrapper`
- **String operations:** `StringUtil`
- **File I/O:** `FileUtil`
- **Hyphenation:** `WordHyphenator`
- **Orchestration:** `TextProcessor`

---

## Key OOP Features

### Polymorphism
Runtime selection of alignment algorithm through interface:
```java
TextAlignmentStrategy strategy = AlignmentStrategyFactory.createStrategy(userInput);
List<String> result = strategy.align(text, 80);  // Polymorphic call
```

### Encapsulation
- Private fields with controlled access
- Implementation details hidden behind interfaces
- Inner class `WordHyphenator.HyphenatedWord` encapsulates hyphenation data

### Immutability
- `final` classes prevent subclassing: `StringUtil`, `TextConstants`, `WordHyphenator`, `AlignmentStrategyFactory`
- `final` fields prevent reassignment
- All concrete strategies are `final`

### Abstraction
Complex algorithms (like justification with hyphenation) hidden behind simple interface

---

## Advanced Features

### Justification Algorithm
[JustifyAlignmentStrategy.java](src/alignment/strategy/JustifyAlignmentStrategy.java) implements:
- Word hyphenation for long words
- Greedy line-filling algorithm
- Last-line special handling (left-aligned)
- Edge case management (empty paragraphs, single words)

### Exception Hierarchy
- `InvalidArgumentException` (checked) - Validation errors
- `TextProcessingException` (unchecked) - Runtime processing errors

### Defensive Programming
- Null validation in constructors
- Boundary checks in utility methods
- Comprehensive input validation

---

## Extensibility

Adding a new alignment type requires:

1. **Create strategy:**
   ```java
   public final class DiagonalAlignmentStrategy implements TextAlignmentStrategy {
       @Override
       public List<String> align(String paragraph, int lineLength) {
           // Implementation
       }
   }
   ```

2. **Register in factory:**
   ```java
   case "diagonal":
       return new DiagonalAlignmentStrategy();
   ```

**No modification to existing strategies or processor needed!**

---

## Usage

```bash
java TextAlignment <filename> <alignmentType> <lineLength>

# Examples:
java TextAlignment input.txt justify 80
java TextAlignment input.txt centre 60
java TextAlignment input.txt left 100
```

---

## Design Summary

| Aspect | Implementation |
|--------|----------------|
| **Primary Pattern** | Strategy Pattern |
| **Creation Pattern** | Factory Pattern |
| **DI Approach** | Constructor-based |
| **SOLID Compliance** | 5/5 principles |
| **Classes** | 15 |
| **Interfaces** | 1 |
| **Packages** | 5 |

This architecture demonstrates professional OOP design: extensible, maintainable, testable, and adhering to industry best practices.
