package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day8 {
  private final char[][] map;
  private final int rows;
  private final int cols;
  private final Set<Point> antinodes = new HashSet<>();

  public static void main(String[] args) throws IOException {
    Day8 day8 = new Day8();
    System.out.println("Number of antinodes: " + day8.countAntinodes());
  }

  public Day8() throws IOException {
    List<String> lines = Files.readAllLines(Path.of("src/main/resources/day8"));
    this.rows = lines.size();
    this.cols = lines.get(0).length();
    this.map = new char[rows][cols];

    for (int i = 0; i < rows; i++) {
      map[i] = lines.get(i).toCharArray();
    }
  }

  public int countAntinodes() {
    // Check horizontal lines
    for (int row = 0; row < rows; row++) {
      findAntinodesInLine(row, true);
    }

    // Check vertical lines
    for (int col = 0; col < cols; col++) {
      findAntinodesInLine(col, false);
    }

    return antinodes.size();
  }

  private void findAntinodesInLine(int index, boolean isHorizontal) {
    Map<Character, List<Integer>> nodePositions = new HashMap<>();

    // Collect positions of all nodes in the line
    if (isHorizontal) {
      for (int col = 0; col < cols; col++) {
        if (isNode(map[index][col])) {
          nodePositions.computeIfAbsent(map[index][col], k -> new ArrayList<>()).add(col);
        }
      }
    } else {
      for (int row = 0; row < rows; row++) {
        if (isNode(map[row][index])) {
          nodePositions.computeIfAbsent(map[row][index], k -> new ArrayList<>()).add(row);
        }
      }
    }

    // Check each set of matching nodes
    for (List<Integer> positions : nodePositions.values()) {
      for (int i = 0; i < positions.size(); i++) {
        for (int j = i + 1; j < positions.size(); j++) {
          int pos1 = positions.get(i);
          int pos2 = positions.get(j);
          int distance = pos2 - pos1;

          // Check antinode positions in both directions
          checkAndAddAntinode(pos1 - distance, index, isHorizontal);
          checkAndAddAntinode(pos2 + distance, index, isHorizontal);
        }
      }
    }
  }

  private boolean isNode(char c) {
    return Character.isLetterOrDigit(c);
  }

  private void checkAndAddAntinode(int position, int index, boolean isHorizontal) {
    if (isHorizontal) {
      if (position >= 0 && position < cols) {
        antinodes.add(new Point(position, index));
      }
    } else {
      if (position >= 0 && position < rows) {
        antinodes.add(new Point(index, position));
      }
    }
  }

  private record Point(int x, int y) {}
}
