package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day9 {

  public static void main(String[] args) throws IOException {
    String input = Files.readString(Path.of("src/main/resources/day9.example"));
    List<Integer> numbers = Arrays.stream(input.trim().split(""))
        .map(Integer::parseInt)
        .toList();

    StringBuilder result = new StringBuilder();
    for (int i = 0; i < numbers.size(); i += 2) {
      int blockLength = numbers.get(i);
      // Add the block using i/2 as the index
      result.append(String.valueOf(i/2).repeat(blockLength));

      // Add blank spaces if there are more numbers
      if (i + 1 < numbers.size()) {
        int blankSpace = numbers.get(i + 1);
        result.append(".".repeat(blankSpace));
      }
    }

    System.out.println("Result: " + result);
  }
}
