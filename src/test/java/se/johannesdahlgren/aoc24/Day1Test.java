package se.johannesdahlgren.aoc24;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import se.johannesdahlgren.aoc24.util.Pair;

class Day1Test {

  @Test
  void testCalculateDistances() {
    List<Pair> pairs = List.of(
        new Pair(3, 4),
        new Pair(4, 3),
        new Pair(2, 5),
        new Pair(1, 3),
        new Pair(3, 9),
        new Pair(3, 3)
    );

    int result = Day1.calculateColumnDistanceSum(pairs);
    assertEquals(11, result, "The sum of column distances should be 11");
  }

  @Test
  void testReadPairsFromFile(@TempDir Path tempDir) throws IOException {
    Path inputFile = tempDir.resolve("test_input.txt");
    Files.writeString(inputFile, "3 4\n4 3\n2 5\n1 3\n3 9\n3 3");

    List<Pair> pairs = Day1.readPairsFromFile(inputFile.toString());

    assertEquals(6, pairs.size(), "Should read 6 pairs from the file");
    assertEquals(new Pair(3, 4), pairs.get(0), "First pair should be (3, 4)");
    assertEquals(new Pair(3, 3), pairs.get(5), "Last pair should be (3, 3)");
  }

  @Test
  void testCalculateDistancesWithEmptyList() {
    List<Pair> pairs = new ArrayList<>();

    int result = Day1.calculateColumnDistanceSum(pairs);
    assertEquals(0, result, "The sum of column distances for an empty list should be 0");
  }

  @Test
  void testReadPairsFromNonExistentFile() {
    assertThrows(IOException.class, ()
        -> Day1.readPairsFromFile("non_existent_file.txt"), "Should throw IOException for non-existent file");
  }
}
