package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {
  public static List<String> findMultiplications(String input) {
    List<String> results = new ArrayList<>();
    // Pattern to match mul(X,Y) where X and Y are 1-3 digits
    Pattern pattern = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
    Matcher matcher = pattern.matcher(input);

    while (matcher.find()) {
      results.add(matcher.group());
    }

    return results;
  }

  public static void main(String[] args) {
    try {
      // Read input from file
      String input = Files.readString(Path.of("src/main/resources/day3.example"));

      // Find all multiplications
      List<String> multiplications = findMultiplications(input);

      // Print results
      System.out.println("Found multiplications:");
      multiplications.forEach(System.out::println);

    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    }
  }
}

