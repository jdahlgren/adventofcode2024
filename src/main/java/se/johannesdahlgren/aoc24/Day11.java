package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day11 {
  public static void main(String[] args) throws Exception {
    String input = Files.readString(Path.of("src/main/resources/day11"));
    List<Long> numbers = new ArrayList<>();

    // Parse initial numbers
    for (String line : input.split("\n")) {
      numbers.add(Long.parseLong(line.trim()));
    }

    // Process for 25 turns
    for (int turn = 0; turn < 25; turn++) {
      List<Long> newNumbers = new ArrayList<>();

      for (Long num : numbers) {
        if (num == 0) {
          newNumbers.add(1L);
        } else if (hasEvenDigits(num)) {
          // Split the number into two halves
          String numStr = String.valueOf(num);
          int mid = numStr.length() / 2;
          long firstHalf = Long.parseLong(numStr.substring(0, mid));
          long secondHalf = Long.parseLong(numStr.substring(mid));
          newNumbers.add(firstHalf);
          newNumbers.add(secondHalf);
        } else {
          newNumbers.add(num * 2024);
        }
      }

      numbers = newNumbers;
    }

    System.out.println("Final number count: " + numbers.size());
  }

  private static boolean hasEvenDigits(long num) {
    return String.valueOf(Math.abs(num)).length() % 2 == 0;
  }
}

