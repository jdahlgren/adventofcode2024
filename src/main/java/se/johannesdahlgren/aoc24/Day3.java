package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

  private static final Pattern MULTIPLICATION_PATTERN = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");

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

  private static Multiplication processMultiplication(String matchText) {
    Matcher mulMatcher = MULTIPLICATION_PATTERN.matcher(matchText);
    if (mulMatcher.find()) {
      int x = Integer.parseInt(mulMatcher.group(1));
      int y = Integer.parseInt(mulMatcher.group(2));
      return new Multiplication(x, y);
    }
    return null;
  }

  public static List<Multiplication> findAndCalculateMultiplications(String input) {
    List<Multiplication> results = new ArrayList<>();
    Matcher matcher = MULTIPLICATION_PATTERN.matcher(input);

    while (matcher.find()) {
      Multiplication mult = processMultiplication(matcher.group());
      if (mult != null) {
        results.add(mult);
      }
    }

    return results;
  }

  public static List<Multiplication> findAndCalculateMultiplicationsWithReset(String input) {
    List<Multiplication> results = new ArrayList<>();

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
            Multiplication mult = processMultiplication(match);
            if (mult != null) {
              results.add(mult);
            }
          }
        }
      }
    }

    return results;
  }

  private static void printResults(String partName, List<Multiplication> multiplications) {
    System.out.println("\n=== " + partName + " ===");
    System.out.println(partName.equals("PART 1") ?
        "All multiplications found:" :
        "Multiplications (between do() and don't()):");

    multiplications.forEach(System.out::println);

    int totalSum = multiplications.stream()
        .mapToInt(m -> m.result)
        .sum();

    System.out.println("\nSum of multiplication results: " + totalSum);
  }

  public static void main(String[] args) {
    try {
      // Read input from file
      String input = Files.readString(Path.of("src/main/resources/day3"));

      // Part 1
      List<Multiplication> multiplicationsPartOne = findAndCalculateMultiplications(input);
      printResults("PART 1", multiplicationsPartOne);

      // Part 2
      List<Multiplication> multiplicationsPartTwo = findAndCalculateMultiplicationsWithReset(input);
      printResults("PART 2", multiplicationsPartTwo);

    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }
}

