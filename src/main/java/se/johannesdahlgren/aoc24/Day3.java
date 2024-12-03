package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
  private static final Pattern MULTIPLICATION_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

  private static int calculateMultiplication(String matchText) {
    Matcher mulMatcher = MULTIPLICATION_PATTERN.matcher(matchText);
    if (mulMatcher.find()) {
      int x = Integer.parseInt(mulMatcher.group(1));
      int y = Integer.parseInt(mulMatcher.group(2));
      return x * y;
    }
    return 0;
  }

  public static int findAndSumMultiplications(String input) {
    int sum = 0;
    Matcher matcher = MULTIPLICATION_PATTERN.matcher(input);

    while (matcher.find()) {
      sum += calculateMultiplication(matcher.group());
    }

    return sum;
  }

  public static int findAndSumMultiplicationsWithReset(String input) {
    int sum = 0;
    Pattern controlPattern = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)");
    Matcher matcher = controlPattern.matcher(input);

    boolean shouldContinue = true;

    while (matcher.find()) {
      String match = matcher.group();

      switch (match) {
        case "do()" -> shouldContinue = true;
        case "don't()" -> shouldContinue = false;
        default -> {
          if (shouldContinue && match.startsWith("mul")) {
            sum += calculateMultiplication(match);
          }
        }
      }
    }

    return sum;
  }

  public static void main(String[] args) {
    try {
      // Read input from file
      String input = Files.readString(Path.of("src/main/resources/day3"));

      System.out.println("Part 1 sum: " + findAndSumMultiplications(input));
      System.out.println("Part 2 sum: " + findAndSumMultiplicationsWithReset(input));

    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }
}
