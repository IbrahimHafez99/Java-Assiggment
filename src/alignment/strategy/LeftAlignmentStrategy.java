package alignment.strategy;

import java.util.List;

public class LeftAlignmentStrategy implements TextAlignmentStrategy {
    @Override
    public List<String> align(String paragraph, int lineLength) {
        // Simple: Add words until line full, then new line
        return List.of();
    }
}
