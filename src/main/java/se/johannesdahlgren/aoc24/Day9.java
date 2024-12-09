package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day9 {
  private static final int BLANK_SPACE = -1; // Using -1 to represent blank space

  public static void main(String[] args) throws IOException {
    Day9 day9 = new Day9();
    String input = day9.readInputFile();
    List<Integer> numbers = day9.parseInput(input);
    List<Integer> initialSequence = day9.processSequence(numbers);
    List<Integer> individualMoveSequence = day9.moveNumbersToSpaces(initialSequence);
    List<Integer> groupMoveSequence = day9.moveGroupsToSpaces(initialSequence);

    long individualSum = day9.calculateSum(individualMoveSequence);
    long groupSum = day9.calculateSum(groupMoveSequence);

    System.out.println("Initial sequence: " + formatSequence(initialSequence));
    System.out.println("Individual move sequence: " + formatSequence(individualMoveSequence));
    System.out.println("Individual move sum: " + individualSum);
    System.out.println("Group move sequence: " + formatSequence(groupMoveSequence));
    System.out.println("Group move sum: " + groupSum);
  }

  private String readInputFile() throws IOException {
    return Files.readString(Path.of("src/main/resources/day9"));
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

    List<Integer> spaceIndices = new ArrayList<>();
    for (int i = 0; i < result.size(); i++) {
      if (result.get(i) == BLANK_SPACE) {
        spaceIndices.add(i);
      }
    }

    if (spaceIndices.isEmpty()) {
      return result;
    }

    for (int spaceIndex : spaceIndices) {
      int lastNumberIndex = result.size() - 1;
      while (lastNumberIndex >= 0 && result.get(lastNumberIndex) == BLANK_SPACE) {
        lastNumberIndex--;
      }

      if (lastNumberIndex < 0 || lastNumberIndex <= spaceIndex) {
        break;
      }

      result.set(spaceIndex, result.get(lastNumberIndex));
      result.set(lastNumberIndex, BLANK_SPACE);
    }

    return result;
  }

  private long calculateSum(List<Integer> sequence) {
    long sum = 0;
    for (int i = 0; i < sequence.size(); i++) {
      int number = sequence.get(i);
      if (number != BLANK_SPACE) {
        sum += (long) i * number;
      }
    }
    return sum;
  }

  private static String formatSequence(List<Integer> sequence) {
    return sequence.stream()
        .map(n -> n == BLANK_SPACE ? "." : String.valueOf(n))
        .reduce("", (a, b) -> a + b);
  }


  private List<Integer> moveGroupsToSpaces(List<Integer> sequence) {
    List<Integer> result = new ArrayList<>(sequence);

    // Create a map of numbers and their positions
    Map<Integer, List<Integer>> numberPositions = new HashMap<>();
    for (int i = result.size() - 1; i >= 0; i--) {
      int number = result.get(i);
      if (number != BLANK_SPACE) {
        numberPositions.computeIfAbsent(number, k -> new ArrayList<>()).add(i);
      }
    }

    // Find all spaces
    List<Integer> spaceIndices = new ArrayList<>();
    for (int i = 0; i < result.size(); i++) {
      if (result.get(i) == BLANK_SPACE) {
        spaceIndices.add(i);
      }
    }

    // Sort numbers from highest to lowest to process from right to left
    List<Integer> sortedNumbers = new ArrayList<>(numberPositions.keySet());
    sortedNumbers.sort(Collections.reverseOrder());

    for (int number : sortedNumbers) {
      List<Integer> positions = numberPositions.get(number);
      int groupSize = positions.size();

      // Find consecutive spaces that can fit this group
      List<Integer> availableSpaces = findConsecutiveSpaces(spaceIndices, groupSize);

      if (!availableSpaces.isEmpty()) {
        // Move the group
        for (int i = 0; i < groupSize; i++) {
          int fromPos = positions.get(i);
          int toPos = availableSpaces.get(i);

          // Only move if the destination is before the source
          if (toPos < fromPos) {
            result.set(toPos, number);
            result.set(fromPos, BLANK_SPACE);
            // Update space indices
            spaceIndices.remove(Integer.valueOf(toPos));
            spaceIndices.add(fromPos);
            Collections.sort(spaceIndices);
          }
        }
      }
    }

    return result;
  }

  private List<Integer> findConsecutiveSpaces(List<Integer> spaceIndices, int needed) {
    List<Integer> available = new ArrayList<>();
    if (spaceIndices.isEmpty() || needed == 0) {
      return available;
    }

    int start = 0;
    while (start < spaceIndices.size()) {
      available.clear();
      available.add(spaceIndices.get(start));

      for (int i = start + 1; i < spaceIndices.size() && available.size() < needed; i++) {
        if (spaceIndices.get(i) == spaceIndices.get(i-1) + 1) {
          available.add(spaceIndices.get(i));
        } else {
          break;
        }
      }

      if (available.size() == needed) {
        return new ArrayList<>(available);
      }

      start++;
    }

    return new ArrayList<>();
  }
}
