package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day9 {
  private static final int BLANK_SPACE = -1; // Using -1 to represent blank space

  public static void main(String[] args) throws IOException {
    Day9 day9 = new Day9();
    String input = day9.readInputFile();
    List<Integer> numbers = day9.parseInput(input);
    List<Integer> initialSequence = day9.processSequence(numbers);
    List<Integer> finalSequence = day9.moveLastNumberToFirstSpace(initialSequence);

    System.out.println("Initial sequence: " + formatSequence(initialSequence));
    System.out.println("Final sequence: " + formatSequence(finalSequence));
  }

  private String readInputFile() throws IOException {
    return Files.readString(Path.of("src/main/resources/day9.example"));
  }

  private List<Integer> parseInput(String input) {
    return Arrays.stream(input.trim().split(""))
        .map(Integer::parseInt)
        .toList();
  }

  private List<Integer> processSequence(List<Integer> numbers) {
    List<Integer> result = new ArrayList<>();
    for (int i = 0; i < numbers.size(); i += 2) {
      addBlock(result, i/2, numbers.get(i));

      if (i + 1 < numbers.size()) {
        addBlankSpace(result, numbers.get(i + 1));
      }
    }
    return result;
  }

  private void addBlock(List<Integer> result, int index, int length) {
    for (int i = 0; i < length; i++) {
      result.add(index);
    }
  }

  private void addBlankSpace(List<Integer> result, int length) {
    for (int i = 0; i < length; i++) {
      result.add(BLANK_SPACE);
    }
  }

  private List<Integer> moveLastNumberToFirstSpace(List<Integer> sequence) {
    // Find the last number in the sequence
    int lastIndex = sequence.size() - 1;
    while (lastIndex >= 0 && sequence.get(lastIndex) == BLANK_SPACE) {
      lastIndex--;
    }

    if (lastIndex < 0) {
      return sequence; // No numbers found
    }

    // Get the last number
    int lastNumber = sequence.get(lastIndex);

    // Find first blank space
    int firstSpaceIndex = sequence.indexOf(BLANK_SPACE);
    if (firstSpaceIndex == -1) {
      return sequence; // No spaces found
    }

    // Create new sequence with the last number moved
    List<Integer> result = new ArrayList<>(sequence);
    result.set(firstSpaceIndex, lastNumber);
    result.set(lastIndex, BLANK_SPACE);

    return result;
  }

  private static String formatSequence(List<Integer> sequence) {
    return sequence.stream()
        .map(n -> n == BLANK_SPACE ? "." : String.valueOf(n))
        .reduce("", (a, b) -> a + b);
  }
}
