package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Day7 {

  public static void main(String[] args) {
    Day7 day7 = new Day7();
    long sum = day7.sumValidTestNumbers();
    System.out.println("Sum of valid test numbers: " + sum);
  }

  public long sumValidTestNumbers() {
    long sum = 0;
    try {
      List<String> lines = Files.readAllLines(Path.of("src/main/resources/day7.example"));

      for (String line : lines) {
        String[] parts = line.split(":");
        int targetValue = Integer.parseInt(parts[0].trim());
        String[] numbers = parts[1].trim().split("\\s+");

        if (canReachTarget(numbers, targetValue, 0, Long.parseLong(numbers[0]))) {
          sum += targetValue;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sum;
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
}
