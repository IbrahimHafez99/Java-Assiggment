# Text Alignment Algorithms - Detailed Explanation

This document provides an in-depth explanation of each text alignment algorithm implemented in the Text Alignment application.

---

## Table of Contents
1. [Overview](#overview)
2. [Left Alignment](#left-alignment)
3. [Right Alignment](#right-alignment)
4. [Center Alignment](#center-alignment)
5. [Justify Alignment](#justify-alignment)
6. [Supporting Utilities](#supporting-utilities)

---

## Overview

All alignment strategies follow the **Strategy Pattern**, allowing different alignment algorithms to be swapped at runtime. Each strategy implements the `TextAlignmentStrategy` interface with a single method:

```java
List<String> align(String paragraph, int lineLength)
```

### Common Process Flow

1. **Input**: A paragraph (continuous text) and a line length (maximum characters per line)
2. **Word Wrapping**: Text is broken into lines that don't exceed the line length
3. **Alignment**: Each line is formatted according to the specific alignment rules
4. **Output**: A list of formatted lines

---

## Left Alignment

**Implementation**: `LeftAlignmentStrategy.java`

### Description
Left alignment is the simplest alignment type. Text begins at the left margin (position 0) with no padding or additional formatting. This is how most plain text files appear by default.

### Algorithm Steps

1. **Word Wrapping**: Use `LineWrapper.wrapWords()` to break the paragraph into lines
   - Words are added to a line until the next word would exceed `lineLength`
   - Each line starts a new line when the limit is reached
2. **No Additional Formatting**: Lines are returned as-is with no padding

### Example

**Input**:
```
Paragraph: "Hello world this is a test of the alignment program"
Line Length: 20
```

**Algorithm Execution**:
```
Line 1: "Hello world this is" (19 chars - fits)
Line 2: "a test of the"       (13 chars - "alignment" won't fit)
Line 3: "alignment program"   (17 chars)
```

**Output**:
```
Hello world this is
a test of the
alignment program
```

### Code Flow
```
align(paragraph, lineLength)
  └─> LineWrapper.wrapWords(paragraph, lineLength)
      └─> Returns wrapped lines (no padding needed)
```

### Characteristics
- **Simplest** algorithm
- **No padding** required
- **Jagged right edge** (lines have different lengths)
- **Fastest** to compute

---

## Right Alignment

**Implementation**: `RightAlignmentStrategy.java`

### Description
Right alignment pushes text to the right margin by adding spaces on the left side of each line. The text ends at position `lineLength` (or just before it for the last line).

### Algorithm Steps

1. **Word Wrapping**: Use `LineWrapper.wrapWords()` to break paragraph into lines
2. **Calculate Padding**: For each line:
   - Calculate: `paddingNeeded = lineLength - line.length()`
3. **Add Left Padding**:
   - Insert `paddingNeeded` spaces before the text
   - Append the original line text
4. **Build Result**: Return list of padded lines

### Example

**Input**:
```
Paragraph: "Hello world this is a test"
Line Length: 20
```

**Algorithm Execution**:
```
Step 1 - Word Wrapping:
  Line 1: "Hello world this is" (19 chars)
  Line 2: "a test"              (6 chars)

Step 2 - Calculate Padding:
  Line 1: 20 - 19 = 1 space needed
  Line 2: 20 - 6 = 14 spaces needed

Step 3 - Add Padding:
  Line 1: " " + "Hello world this is" = " Hello world this is"
  Line 2: "              " + "a test"  = "              a test"
```

**Output**:
```
 Hello world this is
              a test
```

### Code Flow
```
align(paragraph, lineLength)
  ├─> LineWrapper.wrapWords(paragraph, lineLength)
  └─> For each line:
      ├─> Calculate: paddingNeeded = lineLength - line.length()
      ├─> Add paddingNeeded spaces to the left
      └─> Append original line text
```

### Characteristics
- **Right-aligned edge** (all lines end at same position)
- **Jagged left edge** (varying amounts of left padding)
- **More processing** than left alignment
- Used in headers, signatures, and formal documents

---

## Center Alignment

**Implementation**: `CenterAlignmentStrategy.java`

### Description
Center alignment places text in the middle of the line by adding equal padding to both sides. If the total padding is odd, the extra space goes on the right side.

### Algorithm Steps

1. **Word Wrapping**: Use `LineWrapper.wrapWords()` to break paragraph into lines
2. **Calculate Total Padding**: For each line:
   - `totalPadding = lineLength - line.length()`
3. **Distribute Padding**:
   - `leftPadding = totalPadding / 2` (integer division)
   - `rightPadding = totalPadding - leftPadding` (handles odd padding)
4. **Build Line**:
   - Add `leftPadding` spaces
   - Add the line text
   - Add `rightPadding` spaces

### Example

**Input**:
```
Paragraph: "Hello world this is a test"
Line Length: 20
```

**Algorithm Execution**:
```
Step 1 - Word Wrapping:
  Line 1: "Hello world this is" (19 chars)
  Line 2: "a test"              (6 chars)

Step 2 - Calculate Padding:
  Line 1: totalPadding = 20 - 19 = 1
          leftPadding = 1 / 2 = 0
          rightPadding = 1 - 0 = 1

  Line 2: totalPadding = 20 - 6 = 14
          leftPadding = 14 / 2 = 7
          rightPadding = 14 - 7 = 7

Step 3 - Build Lines:
  Line 1: "" + "Hello world this is" + " " = "Hello world this is "
  Line 2: "       " + "a test" + "       " = "       a test       "
```

**Output**:
```
Hello world this is
       a test
```

### Code Flow
```
align(paragraph, lineLength)
  ├─> LineWrapper.wrapWords(paragraph, lineLength)
  └─> For each line:
      ├─> totalPadding = lineLength - line.length()
      ├─> leftPadding = totalPadding / 2
      ├─> rightPadding = totalPadding - leftPadding
      ├─> Add leftPadding spaces
      ├─> Add line text
      └─> Add rightPadding spaces
```

### Characteristics
- **Centered text** within the line length
- **Both edges vary** (padding on both sides)
- **Handles odd padding** by adding extra space to the right
- Used in titles, headers, and poetry

---

## Justify Alignment

**Implementation**: `JustifyAlignmentStrategy.java`

### Description
Justify alignment is the **most complex** algorithm. It distributes text evenly across each line by adding extra spaces between words so that both the left and right edges are aligned. This creates a clean, professional appearance similar to newspapers and books.

### Key Rules

1. **Last line is NOT justified** - left-aligned instead
2. **Single-word lines are NOT justified** - left-aligned
3. **Long words are hyphenated** if they don't fit on a line
4. **Spaces are distributed evenly** between words
5. **Extra spaces go to leftmost gaps** when distribution isn't perfectly even

### Algorithm Steps

#### Phase 1: Line Building

1. **Initialize**: Empty current line, word index = 0
2. **For each word**:
   - Calculate length if word is added:
     ```
     lengthWithWord = currentLineLength + (needsSpace ? 1 : 0) + word.length()
     ```
   - **If word fits** (`lengthWithWord <= lineLength`):
     - Add word to current line
     - Increment word index
   - **If word doesn't fit**:
     - **Case A - Line is empty** (word is too long):
       - Hyphenate the word using `WordHyphenator`
       - Add first part with hyphen as complete line
       - Process second part in next iteration
     - **Case B - Line has words**:
       - Justify the current line (see Phase 2)
       - Start new line with current word
3. **Handle last line**: Add remaining words as left-aligned line

#### Phase 2: Line Justification

Given a list of words and target line length:

1. **Check if justification needed**:
   - Skip if last line → return left-aligned
   - Skip if single word → return left-aligned

2. **Calculate space distribution**:
   ```
   totalWordLength = sum of all word lengths
   totalSpacesNeeded = lineLength - totalWordLength
   gapCount = number of words - 1
   spacesPerGap = totalSpacesNeeded / gapCount
   extraSpaces = totalSpacesNeeded % gapCount
   ```

3. **Distribute spaces**:
   - Each gap gets `spacesPerGap` spaces
   - First `extraSpaces` gaps get 1 additional space
   - This ensures even distribution with leftmost bias

4. **Build justified line**: Concatenate words with calculated spaces

### Detailed Example

**Input**:
```
Paragraph: "The quick brown fox jumps over the lazy dog"
Line Length: 20
```

**Algorithm Execution**:

```
=== ITERATION 1 ===
Current Line: []
Word: "The"
Length with word: 0 + 0 + 3 = 3 ≤ 20 ✓ FITS
Action: Add "The" to line
Current Line: ["The"] (length: 3)

=== ITERATION 2 ===
Current Line: ["The"]
Word: "quick"
Length with word: 3 + 1 + 5 = 9 ≤ 20 ✓ FITS
Action: Add "quick" to line
Current Line: ["The", "quick"] (length: 9)

=== ITERATION 3 ===
Current Line: ["The", "quick"]
Word: "brown"
Length with word: 9 + 1 + 5 = 15 ≤ 20 ✓ FITS
Action: Add "brown" to line
Current Line: ["The", "quick", "brown"] (length: 15)

=== ITERATION 4 ===
Current Line: ["The", "quick", "brown"]
Word: "fox"
Length with word: 15 + 1 + 3 = 19 ≤ 20 ✓ FITS
Action: Add "fox" to line
Current Line: ["The", "quick", "brown", "fox"] (length: 19)

=== ITERATION 5 ===
Current Line: ["The", "quick", "brown", "fox"]
Word: "jumps"
Length with word: 19 + 1 + 5 = 25 > 20 ✗ DOESN'T FIT
Action: Justify current line and output

--- JUSTIFYING LINE 1 ---
Words: ["The", "quick", "brown", "fox"]
Target Length: 20
Current Length: 19 (3 + 5 + 5 + 3 + 3 spaces)

Calculation:
  totalWordLength = 3 + 5 + 5 + 3 = 16
  totalSpacesNeeded = 20 - 16 = 4
  gapCount = 4 - 1 = 3 gaps
  spacesPerGap = 4 / 3 = 1 space per gap
  extraSpaces = 4 % 3 = 1 extra space

Distribution:
  Gap 1 (after "The"):    1 + 1 = 2 spaces (gets extra)
  Gap 2 (after "quick"):  1 space
  Gap 3 (after "brown"):  1 space

Building line:
  "The" + "  " + "quick" + " " + "brown" + " " + "fox"
  = "The  quick brown fox"

LINE 1 OUTPUT: "The  quick brown fox" (exactly 20 chars) ✓

Reset: Current Line = []
Continue with word "jumps"

=== ITERATION 6 ===
Current Line: []
Word: "jumps"
Length with word: 0 + 0 + 5 = 5 ≤ 20 ✓ FITS
Action: Add "jumps" to line
Current Line: ["jumps"] (length: 5)

=== ITERATION 7 ===
Current Line: ["jumps"]
Word: "over"
Length with word: 5 + 1 + 4 = 10 ≤ 20 ✓ FITS
Action: Add "over" to line
Current Line: ["jumps", "over"] (length: 10)

=== ITERATION 8 ===
Current Line: ["jumps", "over"]
Word: "the"
Length with word: 10 + 1 + 3 = 14 ≤ 20 ✓ FITS
Action: Add "the" to line
Current Line: ["jumps", "over", "the"] (length: 14)

=== ITERATION 9 ===
Current Line: ["jumps", "over", "the"]
Word: "lazy"
Length with word: 14 + 1 + 4 = 19 ≤ 20 ✓ FITS
Action: Add "lazy" to line
Current Line: ["jumps", "over", "the", "lazy"] (length: 19)

=== ITERATION 10 ===
Current Line: ["jumps", "over", "the", "lazy"]
Word: "dog"
Length with word: 19 + 1 + 3 = 23 > 20 ✗ DOESN'T FIT
Action: Justify current line and output

--- JUSTIFYING LINE 2 ---
Words: ["jumps", "over", "the", "lazy"]
Target Length: 20

Calculation:
  totalWordLength = 5 + 4 + 3 + 4 = 16
  totalSpacesNeeded = 20 - 16 = 4
  gapCount = 4 - 1 = 3 gaps
  spacesPerGap = 4 / 3 = 1
  extraSpaces = 4 % 3 = 1

Distribution:
  Gap 1 (after "jumps"): 1 + 1 = 2 spaces
  Gap 2 (after "over"):  1 space
  Gap 3 (after "the"):   1 space

Building line:
  "jumps" + "  " + "over" + " " + "the" + " " + "lazy"
  = "jumps  over the lazy"

LINE 2 OUTPUT: "jumps  over the lazy" (exactly 20 chars) ✓

Reset: Current Line = []
Continue with word "dog"

=== ITERATION 11 ===
Current Line: []
Word: "dog"
Length with word: 0 + 0 + 3 = 3 ≤ 20 ✓ FITS
Action: Add "dog" to line
Current Line: ["dog"] (length: 3)

=== END OF WORDS ===
Current Line: ["dog"]
Action: Output as LAST LINE (no justification)

LINE 3 OUTPUT: "dog" (left-aligned, no padding)
```

**Final Output**:
```
The  quick brown fox
jumps  over the lazy
dog
```

### Hyphenation Example

**Input**:
```
Paragraph: "This is an extraordinary situation"
Line Length: 15
```

**Algorithm Execution**:
```
=== Building Line 1 ===
Words tried: "This" (4) + "is" (2) + "an" (2) = "This is an" (10 chars) ✓
Next word: "extraordinary" (13)
Length with word: 10 + 1 + 13 = 24 > 15 ✗ DOESN'T FIT

Justify and output: "This  is   an" (15 chars)

=== Building Line 2 ===
Word: "extraordinary" (13 chars)
Line is empty AND word is too long → HYPHENATE

WordHyphenator.hyphenate("extraordinary", 15):
  Max space: 15 - 1 (for hyphen) = 14 chars for first part
  Split: "extraordinar-" (13 + hyphen = 14) + "y"

Output: "extraordinar-" (14 chars)
Replace word with: "y"

=== Building Line 3 ===
Word: "y"
Next word: "situation"
Line: "y situation" (11 chars) ✓
No more words → LAST LINE (no justification)

Output: "y situation" (left-aligned)
```

**Final Output**:
```
This  is    an
extraordinar-
y situation
```

### Code Flow
```
align(paragraph, lineLength)
  ├─> Split paragraph into words
  ├─> Initialize: currentLineWords = [], currentLineLength = 0
  └─> For each word:
      ├─> Calculate: lengthWithWord = currentLineLength + spaceIfNeeded + word.length()
      ├─> IF lengthWithWord <= lineLength:
      │   ├─> Add word to currentLineWords
      │   └─> Update currentLineLength
      ├─> ELSE (word doesn't fit):
      │   ├─> IF currentLineWords is empty:
      │   │   ├─> Hyphenate word
      │   │   ├─> Output first part with hyphen
      │   │   └─> Replace word with second part (retry)
      │   └─> ELSE:
      │       ├─> justifyLine(currentLineWords, lineLength, false)
      │       └─> Reset currentLineWords
      └─> After all words: justifyLine(remaining, lineLength, true)

justifyLine(words, lineLength, isLastLine)
  ├─> IF isLastLine OR words.size() == 1:
  │   └─> Return words joined with single spaces (left-aligned)
  └─> ELSE:
      ├─> totalWordLength = sum of word lengths
      ├─> totalSpacesNeeded = lineLength - totalWordLength
      ├─> gapCount = words.size() - 1
      ├─> spacesPerGap = totalSpacesNeeded / gapCount
      ├─> extraSpaces = totalSpacesNeeded % gapCount
      └─> For each word:
          ├─> Append word
          └─> If not last word:
              ├─> spaces = spacesPerGap
              ├─> IF gap index < extraSpaces: spaces++
              └─> Append spaces
```

### Characteristics
- **Most complex** algorithm
- **Both edges aligned** (except last line)
- **Professional appearance** like published books
- **Requires space distribution** calculation
- **Handles word hyphenation** for long words
- **Slower** than other alignments due to complexity

---

## Supporting Utilities

### LineWrapper

**Purpose**: Wraps text into lines without exceeding the line length

**Method**: `wrapWords(String text, int maxLength)`

**Algorithm**:
1. Split text into words by whitespace
2. Add words to current line while length permits
3. Start new line when next word won't fit
4. Handle single long words by placing them alone on a line

**Example**:
```java
LineWrapper.wrapWords("Hello world how are you", 10)
// Returns: ["Hello", "world how", "are you"]
```

### WordHyphenator

**Purpose**: Hyphenates words that are too long to fit on a line

**Method**: `hyphenate(String word, int maxLength)`

**Algorithm**:
1. Check if word needs hyphenation: `word.length() > maxLength`
2. Calculate split point: `maxLength - 1` (reserve space for hyphen)
3. Split word into first and second parts
4. Return `HyphenatedWord` object with both parts

**Example**:
```java
WordHyphenator.hyphenate("extraordinary", 10)
// Returns: HyphenatedWord("extraordi-", "nary")
```

### TextConstants

**Purpose**: Centralized constants for text processing

**Constants**:
- `SPACE`: Single space character (" ")
- `HYPHEN`: Hyphen character ("-")
- `WORD_SPLIT_PATTERN`: Regex for splitting text into words ("\\s+")

---

## Comparison Summary

| Algorithm | Complexity | Left Edge | Right Edge | Use Case |
|-----------|-----------|-----------|------------|----------|
| **Left** | Simplest | Aligned | Jagged | Default text, code |
| **Right** | Simple | Jagged | Aligned | Headers, signatures |
| **Center** | Moderate | Jagged | Jagged | Titles, poetry |
| **Justify** | Complex | Aligned | Aligned | Books, newspapers |

---

## Architecture Benefits

### Strategy Pattern Advantages
1. **Easy to add new algorithms** - just implement the interface
2. **Switch at runtime** - no code changes needed
3. **Each algorithm is independent** - changes don't affect others
4. **Testable** - each strategy can be tested in isolation

### SOLID Principles Applied
- **Single Responsibility**: Each strategy handles one alignment type
- **Open/Closed**: Open for extension (new strategies), closed for modification
- **Liskov Substitution**: Any strategy can replace another
- **Interface Segregation**: Single focused method in interface
- **Dependency Inversion**: High-level code depends on abstraction (interface)

---

## Performance Considerations

**Time Complexity** (for paragraph of length n with m words):
- **Left**: O(m) - simple word wrapping
- **Right**: O(m) - wrapping + padding
- **Center**: O(m) - wrapping + padding
- **Justify**: O(m²) in worst case (due to hyphenation and space distribution)

**Space Complexity**: O(m) for all algorithms (storing list of lines)

---

## Testing Strategies

### Test Cases to Consider
1. **Empty text** - should return empty list
2. **Single word** - should return single line
3. **Word longer than line length** - test hyphenation
4. **Exact fit** - word(s) exactly match line length
5. **Multiple paragraphs** - ensure proper separation
6. **Special characters** - ensure they're handled correctly
7. **Various line lengths** - from very short to very long

### Example Test
```java
@Test
public void testJustifyAlignment() {
    TextAlignmentStrategy strategy = new JustifyAlignmentStrategy();
    List<String> result = strategy.align("The quick brown fox", 20);

    // All lines except last should be exactly lineLength
    for (int i = 0; i < result.size() - 1; i++) {
        assertEquals(20, result.get(i).length());
    }
}
```
