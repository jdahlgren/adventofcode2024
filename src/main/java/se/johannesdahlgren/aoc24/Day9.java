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
    List<Integer> finalSequence = day9.moveNumbersToSpaces(initialSequence);

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

  private List<Integer> moveNumbersToSpaces(List<Integer> sequence) {
    List<Integer> result = new ArrayList<>(sequence);

    // Find all spaces
    List<Integer> spaceIndices = new ArrayList<>();
    for (int i = 0; i < result.size(); i++) {
      if (result.get(i) == BLANK_SPACE) {
        spaceIndices.add(i);
      }
    }

    // If no spaces, return original sequence
    if (spaceIndices.isEmpty()) {
      return result;
    }

    // For each space, starting from first space
    for (int spaceIndex : spaceIndices) {
      // Find the last non-space number
      int lastNumberIndex = result.size() - 1;
      while (lastNumberIndex >= 0 && result.get(lastNumberIndex) == BLANK_SPACE) {
        lastNumberIndex--;
      }

      // If we've run out of numbers to move, break
      if (lastNumberIndex < 0 || lastNumberIndex <= spaceIndex) {
        break;
      }

      // Move the number to the space
      result.set(spaceIndex, result.get(lastNumberIndex));
      result.set(lastNumberIndex, BLANK_SPACE);
    }

    return result;
  }

  private static String formatSequence(List<Integer> sequence) {
    return sequence.stream()
        .map(n -> n == BLANK_SPACE ? "." : String.valueOf(n))
        .reduce("", (a, b) -> a + b);
  }
}
