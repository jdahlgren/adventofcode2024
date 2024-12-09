package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day9 {

  public static void main(String[] args) throws IOException {
    Day9 day9 = new Day9();
    String input = day9.readInputFile();
    List<Integer> numbers = day9.parseInput(input);
    String initialSequence = day9.processSequence(numbers);
    String finalSequence = day9.moveLastDigitToFirstSpace(initialSequence);

    System.out.println("Initial sequence: " + initialSequence);
    System.out.println("Final sequence: " + finalSequence);
  }

  private String readInputFile() throws IOException {
    return Files.readString(Path.of("src/main/resources/day9.example"));
  }

  private List<Integer> parseInput(String input) {
    return Arrays.stream(input.trim().split(""))
        .map(Integer::parseInt)
        .toList();
  }

  private String processSequence(List<Integer> numbers) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < numbers.size(); i += 2) {
      addBlock(result, i/2, numbers.get(i));

      if (i + 1 < numbers.size()) {
        addBlankSpace(result, numbers.get(i + 1));
      }
    }
    return result.toString();
  }

  private void addBlock(StringBuilder result, int index, int length) {
    result.append(String.valueOf(index).repeat(length));
  }

  private void addBlankSpace(StringBuilder result, int length) {
    result.append(".".repeat(length));
  }

  private String moveLastDigitToFirstSpace(String sequence) {
    // Find the last number in the sequence
    int lastIndex = sequence.length() - 1;
    while (lastIndex >= 0 && sequence.charAt(lastIndex) == '.') {
      lastIndex--;
    }

    if (lastIndex < 0) {
      return sequence; // No numbers found
    }

    // Extract the last digit
    int endOfNumber = lastIndex + 1;
    int startOfNumber = lastIndex;
    while (startOfNumber > 0 && Character.isDigit(sequence.charAt(startOfNumber - 1))) {
      startOfNumber--;
    }
    String lastNumber = sequence.substring(startOfNumber, endOfNumber);

    // Find first blank space
    int firstSpaceIndex = sequence.indexOf('.');
    if (firstSpaceIndex == -1) {
      return sequence; // No spaces found
    }

    // Create new sequence with the last digit moved
    StringBuilder result = new StringBuilder(sequence);
    result.replace(firstSpaceIndex, firstSpaceIndex + 1, lastNumber);
    result.replace(startOfNumber, endOfNumber, ".");

    return result.toString();
  }
}
