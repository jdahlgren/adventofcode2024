package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day5 {
  private final List<Rule> rules = new ArrayList<>();
  private final List<List<Integer>> numberLists = new ArrayList<>();

  public record Rule(int before, int after) {}

  public void loadFromFile(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    List<String> lines = Files.readAllLines(path);
    boolean isParsingRules = true;

    for (String line : lines) {
      if (line.trim().isEmpty()) {
        isParsingRules = false;
        continue;
      }

      if (isParsingRules) {
        String[] parts = line.split("\\|");
        rules.add(new Rule(
            Integer.parseInt(parts[0].trim()),
            Integer.parseInt(parts[1].trim())
        ));
      } else {
        List<Integer> numbers = Arrays.stream(line.split(","))
            .map(String::trim)
            .map(Integer::parseInt)
            .toList();
        numberLists.add(numbers);
      }
    }
  }

  public int sumMiddleNumbersOfFixedLists() {
    return numberLists.stream()
        .map(this::fixNumberList)
        .map(this::getMiddleNumber)
        .mapToInt(Integer::intValue)
        .sum();
  }

  private List<Integer> fixNumberList(List<Integer> numbers) {
    List<Integer> mutableNumbers = new ArrayList<>(numbers);
    boolean needsFixing = true;

    while (needsFixing) {
      needsFixing = false;
      for (Rule rule : rules) {
        int beforeIndex = mutableNumbers.indexOf(rule.before());
        int afterIndex = mutableNumbers.indexOf(rule.after());

        if (beforeIndex != -1 && afterIndex != -1 && beforeIndex > afterIndex) {
          // Swap the numbers to fix the order
          int temp = mutableNumbers.get(beforeIndex);
          mutableNumbers.set(beforeIndex, mutableNumbers.get(afterIndex));
          mutableNumbers.set(afterIndex, temp);
          needsFixing = true;
        }
      }
    }
    return mutableNumbers;
  }

  public int sumMiddleNumbersOfValidLists() {
    return numberLists.stream()
        .filter(this::isValidNumberList)
        .map(this::getMiddleNumber)
        .mapToInt(Integer::intValue)
        .sum();
  }

  private Integer getMiddleNumber(List<Integer> numbers) {
    int middleIndex = numbers.size() / 2;
    return numbers.get(middleIndex);
  }

  private boolean isValidNumberList(List<Integer> numbers) {
    return rules.stream().allMatch(rule -> isValidRule(numbers, rule));
  }

  private boolean isValidRule(List<Integer> numbers, Rule rule) {
    int beforeIndex = numbers.indexOf(rule.before());
    int afterIndex = numbers.indexOf(rule.after());

    // If either number is not in the list, the rule doesn't apply
    if (beforeIndex == -1 || afterIndex == -1) {
      return true;
    }

    // Check if the before number comes before the after number
    return beforeIndex < afterIndex;
  }

  public static void main(String[] args) throws IOException {
    Day5 validator = new Day5();
    validator.loadFromFile("src/main/resources/day5");

    // Part 1: Original sum of valid lists
    int originalSum = validator.sumMiddleNumbersOfValidLists();
    System.out.println("Sum of middle numbers from valid lists: " + originalSum);

    // Part 2: Sum after fixing invalid lists
    int fixedSum = validator.sumMiddleNumbersOfFixedLists();
    System.out.println("Sum of middle numbers after fixing lists: " + fixedSum);
  }
}
