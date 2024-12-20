package se.johannesdahlgren.aoc24;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
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

  private List<Pair> testPairs;

  @BeforeEach
  void setUp() {
    testPairs = List.of(
        new Pair(3, 4),
        new Pair(4, 3),
        new Pair(2, 5),
        new Pair(1, 3),
        new Pair(3, 9),
        new Pair(3, 3)
    );
  }

  @Test
  void testCountOccurrencesInSecondColumn() {
    Map<Integer, Long> occurrences = Day1.countOccurrencesInSecondColumn(testPairs);

    assertEquals(4, occurrences.size(), "Should have 4 unique elements from the first column");
    assertEquals(9L, occurrences.get(3), "3 should occur 9 times in the second column");
    assertEquals(1L, occurrences.get(4), "4 should occur 1 time in the second column");
    assertEquals(0L, occurrences.get(2), "2 should occur 0 times in the second column");
    assertEquals(0L, occurrences.get(1), "1 should occur 0 time in the second column");
  }

  @Test
  void testCalculateSimilarityScore() {
    Map<Integer, Long> occurrences = Map.of(
        3, 2L,
        4, 1L,
        2, 0L,
        1, 1L
    );

    long similarityScore = Day1.calculateSimilarityScore(occurrences);

    assertEquals(11, similarityScore, "Similarity score should be (3*2 + 4*1 + 2*0 + 1*1) = 11");
  }

  @Test
  void testWithEmptyList() {
    List<Pair> emptyList = List.of();

    Map<Integer, Long> occurrences = Day1.countOccurrencesInSecondColumn(emptyList);
    assertTrue(occurrences.isEmpty(), "Occurrences map should be empty for an empty list");

    long similarityScore = Day1.calculateSimilarityScore(occurrences);
    assertEquals(0, similarityScore, "Similarity score should be 0 for an empty list");

    int distanceSum = Day1.calculateColumnDistanceSum(emptyList);
    assertEquals(0, distanceSum, "Distance sum should be 0 for an empty list");
  }
}
