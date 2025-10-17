# Text Alignment Program - OOP Design

**Author:** Ibrahim Hafez - 250023098
**Module:** CS5001 Object-Oriented Modelling, Design and Programming

---

## Overview

A text alignment system that demonstrates OOP design through the implementation of a text alignment program.

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
  - [JustifyAlignmentStrategy.java](src/alignment/strategy/JustifyAlignmentStrategy.java)
- **Context:** [TextProcessor.java](src/processing/TextProcessor.java) - Delegates alignment to injected strategy

**Benefits:**

- Runtime algorithm selection through polymorphism
- Easy to add new alignment types without modifying existing code
- Each strategy can be tested independently

### Factory Pattern

[AlignmentStrategyFactory.java](src/alignment/factory/AlignmentStrategyFactory.java) encapsulates strategy creation logic:

- Handles case-insensitive input
- Centralizes object creation

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
