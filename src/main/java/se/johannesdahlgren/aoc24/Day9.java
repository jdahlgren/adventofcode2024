package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Day9 {

  public static void main(String[] args) throws IOException {
    Day9 day9 = new Day9();
    String input = day9.readInputFile();
    List<Integer> numbers = day9.parseInput(input);
    String result = day9.processSequence(numbers);
    System.out.println("Result: " + result);
  }

  private String readInputFile() throws IOException {
    return Files.readString(Path.of("src/main/resources/day9.example"));
  }

  private List<Integer> parseInput(String input) {
    return Arrays.stream(input.trim().split(""))
        .map(Integer::parseInt)
        .toList();
  }

  private String processSequence(List<Integer> numbers) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < numbers.size(); i += 2) {
      addBlock(result, i/2, numbers.get(i));

      if (i + 1 < numbers.size()) {
        addBlankSpace(result, numbers.get(i + 1));
      }
    }
    return result.toString();
  }

  private void addBlock(StringBuilder result, int index, int length) {
    result.append(String.valueOf(index).repeat(length));
  }

  private void addBlankSpace(StringBuilder result, int length) {
    result.append(".".repeat(length));
  }
}
