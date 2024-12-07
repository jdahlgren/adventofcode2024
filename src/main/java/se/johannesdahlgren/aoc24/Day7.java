package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day7 {

  public List<Integer> findValidLines() {
    List<Integer> validLines = new ArrayList<>();
    try {
      List<String> lines = Files.readAllLines(Path.of("src/main/resources/day7.example"));

      for (int lineNumber = 0; lineNumber < lines.size(); lineNumber++) {
        String line = lines.get(lineNumber);
        String[] parts = line.split(":");
        int targetValue = Integer.parseInt(parts[0].trim());
        String[] numbers = parts[1].trim().split("\\s+");

        if (canReachTarget(numbers, targetValue, 0, Long.parseLong(numbers[0]))) {
          validLines.add(lineNumber + 1);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return validLines;
  }

  private boolean canReachTarget(String[] numbers, int target, int index, long currentResult) {
    if (index == numbers.length - 1) {
      return currentResult == target;
    }

    long nextNum = Long.parseLong(numbers[index + 1]);

    // Try addition
    if (canReachTarget(numbers, target, index + 1, currentResult + nextNum)) {
      return true;
    }

    // Try multiplication
    return canReachTarget(numbers, target, index + 1, currentResult * nextNum);
  }

  public static void main(String[] args) {
    Day7 day7 = new Day7();
    List<Integer> validLines = day7.findValidLines();
    System.out.println("Valid lines: " + validLines);
    System.out.println("Number of valid lines: " + validLines.size());

  }
}
