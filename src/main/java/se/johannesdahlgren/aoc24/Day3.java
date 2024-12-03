package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
  public static class Multiplication {
    int x;
    int y;
    int result;

    public Multiplication(int x, int y) {
      this.x = x;
      this.y = y;
      this.result = x * y;
    }

    @Override
    public String toString() {
      return String.format("mul(%d,%d) = %d", x, y, result);
    }
  }

  public static List<Multiplication> findAndCalculateMultiplications(String input) {
    List<Multiplication> results = new ArrayList<>();
    Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      int x = Integer.parseInt(matcher.group(1));
      int y = Integer.parseInt(matcher.group(2));
      results.add(new Multiplication(x, y));
    }

    return results;
  }

  public static List<Multiplication> findAndCalculateMultiplicationsWithReset(String input) {
    List<Multiplication> results = new ArrayList<>();

    // Pattern to find control sequences and multiplications
    Pattern controlPattern = Pattern.compile("do\\(\\)|don't\\(\\)|mul\\((\\d{1,3}),(\\d{1,3})\\)");
    Matcher matcher = controlPattern.matcher(input);

    boolean shouldContinue = true;

    while (matcher.find()) {
      String match = matcher.group();

      if (match.equals("do()")) {
        shouldContinue = true;
        continue;
      }

      if (match.equals("don't()")) {
        shouldContinue = false;
        continue;
      }

      // Process multiplication if we should continue
      if (shouldContinue && match.startsWith("mul")) {
        Pattern mulPattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
        Matcher mulMatcher = mulPattern.matcher(match);
        if (mulMatcher.find()) {
          int x = Integer.parseInt(mulMatcher.group(1));
          int y = Integer.parseInt(mulMatcher.group(2));
          results.add(new Multiplication(x, y));
        }
      }
    }

    return results;
  }

  public static void main(String[] args) {
    try {
      // Read input from file
      String input = Files.readString(Path.of("src/main/resources/day3"));

      List<Multiplication> multiplications = findAndCalculateMultiplicationsWithReset(input);

      // Print individual results
      System.out.println("Individual multiplications:");
      multiplications.forEach(System.out::println);

      // Calculate and print sum
      int totalSum = multiplications.stream()
          .mapToInt(m -> m.result)
          .sum();

      System.out.println("\nSum of all multiplication results: " + totalSum);

    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }
}

