package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Day7 {
  private static final Logger LOGGER = Logger.getLogger(Day7.class.getName());

  public static void main(String[] args) {
    Day7 day7 = new Day7();
    long sumTwoOperators = day7.sumValidTestNumbers(false);
    long sumThreeOperators = day7.sumValidTestNumbers(true);
    System.out.println("Sum of valid test numbers (+ and *): " + sumTwoOperators);
    System.out.println("Sum of valid test numbers (+, * and ||): " + sumThreeOperators);
  }

  public long sumValidTestNumbers(boolean includeConcatenation) {
    long sum = 0;
    try {
      List<String> lines = Files.readAllLines(Path.of("src/main/resources/day7"));

      for (String line : lines) {
        String[] parts = line.split(":");
        long targetValue = Long.parseLong(parts[0].trim());
        String[] numbers = parts[1].trim().split("\\s+");

        if (includeConcatenation) {
          if (canReachTargetThreeOps(numbers, targetValue, 0, Long.parseLong(numbers[0]), String.valueOf(numbers[0]))) {
            sum += targetValue;
          }
        } else {
          if (canReachTargetTwoOps(numbers, targetValue, 0, Long.parseLong(numbers[0]))) {
            sum += targetValue;
          }
        }
      }
    } catch (Exception e) {
      LOGGER.log(Level.SEVERE, "Error processing file", e);
    }
    return sum;
  }

  private boolean canReachTargetThreeOps(String[] numbers, long target, int index, long currentResult, String currentStringNum) {
    if (index == numbers.length - 1) {
      return currentResult == target;
    }

    long nextNum = Long.parseLong(numbers[index + 1]);
    String nextStringNum = numbers[index + 1];

    // Try addition
    if (canReachTargetThreeOps(numbers, target, index + 1, currentResult + nextNum, String.valueOf(currentResult + nextNum))) {
      return true;
    }

    // Try multiplication
    if (canReachTargetThreeOps(numbers, target, index + 1, currentResult * nextNum, String.valueOf(currentResult * nextNum))) {
      return true;
    }

    // Try concatenation
    long concatenatedNum = Long.parseLong(currentStringNum + nextStringNum);
    return canReachTargetThreeOps(numbers, target, index + 1, concatenatedNum, String.valueOf(concatenatedNum));
  }

  private boolean canReachTargetTwoOps(String[] numbers, long target, int index, long currentResult) {
    if (index == numbers.length - 1) {
      return currentResult == target;
    }

    long nextNum = Long.parseLong(numbers[index + 1]);

    // Try addition
    if (canReachTargetTwoOps(numbers, target, index + 1, currentResult + nextNum)) {
      return true;
    }

    // Try multiplication
    return canReachTargetTwoOps(numbers, target, index + 1, currentResult * nextNum);
  }
}
