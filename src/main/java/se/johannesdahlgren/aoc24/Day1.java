package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.IntStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.johannesdahlgren.aoc24.util.Pair;

public class Day1 {

  private static final Logger log = LoggerFactory.getLogger(Day1.class);

  public static void main(String[] args) throws IOException {
    List<Pair> pairs = readPairsFromFile("src/main/resources/day1");
    int result = calculateColumnDistanceSum(pairs);
    log.info("Sum of column distances: {}", result);
  }

  static List<Pair> readPairsFromFile(String filePath) throws IOException {
    try (var lines = Files.lines(Path.of(filePath))) {
      return lines
          .map(Day1::getPair)
          .toList();
    }
  }

  private static Pair getPair(String line) {
    String[] parts = line.split("\\s+");
      return new Pair(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]));
  }

  static int calculateColumnDistanceSum(List<Pair> pairs) {
    List<Integer> leftColumn = pairs.stream().mapToInt(Pair::left).sorted().boxed().toList();
    List<Integer> rightColumn = pairs.stream().mapToInt(Pair::right).sorted().boxed().toList();

    return IntStream.range(0, leftColumn.size())
        .map(i -> Math.abs(leftColumn.get(i) - rightColumn.get(i)))
        .sum();
  }
}
