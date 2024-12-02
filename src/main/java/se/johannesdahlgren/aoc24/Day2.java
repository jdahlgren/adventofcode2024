package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day2 {

  public static void main(String[] args) {
    try {
      String filePath = "src/main/resources/day2";
      int validLines = countValidLines(filePath);
      System.out.println("Total valid lines: " + validLines);
    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }

  private static int countValidLines(String filePath) throws IOException {
    return Files.readAllLines(Path.of(filePath)).stream()
        .map(Day2::parseNumbers)
        .mapToInt(Day2::isValidSequence)
        .sum();
  }

  private static List<Integer> parseNumbers(String line) {
    return Arrays.stream(line.trim().split("\\s+"))
        .map(Integer::parseInt)
        .toList();
  }

  private static int isValidSequence(List<Integer> numbers) {
    boolean increasing = true;
    boolean decreasing = true;

    for (int i = 1; i < numbers.size(); i++) {
      int difference = numbers.get(i) - numbers.get(i - 1);

      // Check if sequence breaks the rules
      if ((difference <= 0 || difference > 3) &&
          (difference >= 0 || difference < -3)) {
        return 0;
      }

      if (difference > 0) {
        decreasing = false;
      }
      if (difference < 0) {
        increasing = false;
      }

      // If neither increasing nor decreasing is possible
      if (!increasing && !decreasing) {
        return 0;
      }
    }

    return 1;
  }
}
