package se.johannesdahlgren.aoc24;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day11 {
  private static final BigInteger MULTIPLIER = BigInteger.valueOf(2024);
  private static final Map<BigInteger, List<BigInteger>> memo = new HashMap<>();

  public static void main(String[] args) throws Exception {
    processInput(25);
    memo.clear(); // Clear memo between runs
    processInput(75);
  }

  private static void processInput(int turns) throws Exception {
    String input = Files.readString(Path.of("src/main/resources/day11"));
    Map<BigInteger, Long> numberCounts = new HashMap<>();

    // Parse initial space-separated numbers
    for (String num : input.trim().split("\\s+")) {
      BigInteger number = new BigInteger(num);
      numberCounts.merge(number, 1L, Long::sum);
    }

    // Process for specified number of turns
    for (int turn = 0; turn < turns; turn++) {
      Map<BigInteger, Long> newNumberCounts = new HashMap<>();

      for (var entry : numberCounts.entrySet()) {
        BigInteger num = entry.getKey();
        long count = entry.getValue();

        List<BigInteger> processed = processNumber(num);
        for (BigInteger result : processed) {
          newNumberCounts.merge(result, count, Long::sum);
        }
      }

      numberCounts = newNumberCounts;
    }

    long totalCount = numberCounts.values().stream().mapToLong(Long::longValue).sum();
    System.out.println("Final number count after " + turns + " turns: " + totalCount);
  }

  private static List<BigInteger> processNumber(BigInteger num) {
    // Check if we've seen this number before
    if (memo.containsKey(num)) {
      return memo.get(num);
    }

    List<BigInteger> result = new ArrayList<>();

    if (num.equals(BigInteger.ZERO)) {
      result.add(BigInteger.ONE);
    } else {
      String numStr = num.toString();
      if (numStr.length() % 2 == 0) {
        int mid = numStr.length() / 2;
        result.add(new BigInteger(numStr.substring(0, mid)));
        result.add(new BigInteger(numStr.substring(mid)));
      } else {
        result.add(num.multiply(MULTIPLIER));
      }
    }

    // Store result in memo
    memo.put(num, result);
    return result;
  }
}
