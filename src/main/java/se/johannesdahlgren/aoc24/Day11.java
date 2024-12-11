package se.johannesdahlgren.aoc24;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.math.BigInteger;

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
    List<BigInteger> numbers = new ArrayList<>();

    // Parse initial space-separated numbers
    for (String num : input.trim().split("\\s+")) {
      numbers.add(new BigInteger(num));
    }

    // Process for specified number of turns
    for (int turn = 0; turn < turns; turn++) {
      List<BigInteger> newNumbers = new ArrayList<>();

      for (BigInteger num : numbers) {
        List<BigInteger> result = processNumber(num);
        newNumbers.addAll(result);
      }

      numbers = newNumbers;
    }

    System.out.println("Final number count after " + turns + " turns: " + numbers.size());
  }

  private static List<BigInteger> processNumber(BigInteger num) {
    // Check if we've seen this number before
    if (memo.containsKey(num)) {
      return new ArrayList<>(memo.get(num));
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
    memo.put(num, new ArrayList<>(result));
    return result;
  }
}
