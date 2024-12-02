package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {

  public static void main(String[] args) {
    try {
      String filePath = "src/main/resources/day2";
      int validLinesPart1 = countValidLinesPart1(filePath);
      int validLinesPart2 = countValidLinesPart2(filePath);
      System.out.println("Total valid lines: " + validLinesPart1);
      System.out.println("Total valid lines: " + validLinesPart2);
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }

  private static int countValidLinesPart1(String filePath) throws IOException {
    return Files.readAllLines(Path.of(filePath)).stream()
        .map(Day2::parseNumbers)
        .mapToInt(Day2::isValidSequencePart1)
        .sum();
  }

  private static int countValidLinesPart2(String filePath) throws IOException {
    return Files.readAllLines(Path.of(filePath)).stream()
        .map(Day2::parseNumbers)
        .mapToInt(Day2::canBeValid)
        .sum();
  }

  private static List<Integer> parseNumbers(String line) {
    return Arrays.stream(line.trim().split("\\s+"))
        .map(Integer::parseInt)
        .toList();
  }

  private static int canBeValid(List<Integer> numbers) {
    // If it's already valid, return 1
    if (isValidSequence(numbers)) {
      return 1;
    }

    // Try removing each number one at a time
    for (int i = 0; i < numbers.size(); i++) {
      List<Integer> modified = new ArrayList<>(numbers);
      modified.remove(i);
      if (isValidSequence(modified)) {
        return 1;
      }
    }

    return 0;
  }

  private static int isValidSequencePart1(List<Integer> numbers) {
    if (isValidSequence(numbers)) {
      return 1;
    }
    return 0;
  }

  private static boolean isValidSequence(List<Integer> numbers) {
    if (numbers.size() < 2) return true;

    boolean increasing = true;
    boolean decreasing = true;

    for (int i = 1; i < numbers.size(); i++) {
      int difference = numbers.get(i) - numbers.get(i - 1);

      // Check if sequence breaks the rules
      if ((difference <= 0 || difference > 3) &&
          (difference >= 0 || difference < -3)) {
        return false;
      }

      if (difference > 0) decreasing = false;
      if (difference < 0) increasing = false;

      // If neither increasing nor decreasing is possible
      if (!increasing && !decreasing) {
        return false;
      }
    }

    return true;
  }
}
