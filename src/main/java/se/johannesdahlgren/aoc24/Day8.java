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

    // Check diagonal lines
    findDiagonalAntinodes();

    return antinodes.size();
  }

  private void findDiagonalAntinodes() {
    // Check all possible starting points for diagonals
    for (int row = 0; row < rows; row++) {
      for (int col = 0; col < cols; col++) {
        // Check diagonal going down-right
        findAntinodesInDiagonal(row, col, 1, 1);
        // Check diagonal going up-right
        findAntinodesInDiagonal(row, col, -1, 1);
      }
    }
  }

  private void findAntinodesInDiagonal(int startRow, int startCol, int rowDir, int colDir) {
    Map<Character, List<Point>> nodePositions = new HashMap<>();
    int row = startRow;
    int col = startCol;

    // Collect positions of nodes along the diagonal
    while (row >= 0 && row < rows && col >= 0 && col < cols) {
      if (isNode(map[row][col])) {
        nodePositions.computeIfAbsent(map[row][col], k -> new ArrayList<>())
            .add(new Point(col, row));
      }
      row += rowDir;
      col += colDir;
    }

    // Check each set of matching nodes
    for (List<Point> positions : nodePositions.values()) {
      for (int i = 0; i < positions.size(); i++) {
        for (int j = i + 1; j < positions.size(); j++) {
          Point p1 = positions.get(i);
          Point p2 = positions.get(j);
          int dx = p2.x - p1.x;
          int dy = p2.y - p1.y;

          // Check antinode positions in both directions
          checkAndAddAntinode(p1.x - dx, p1.y - dy);
          checkAndAddAntinode(p2.x + dx, p2.y + dy);
        }
      }
    }
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

          if (isHorizontal) {
            checkAndAddAntinode(pos1 - distance, index);
            checkAndAddAntinode(pos2 + distance, index);
          } else {
            checkAndAddAntinode(index, pos1 - distance);
            checkAndAddAntinode(index, pos2 + distance);
          }
        }
      }
    }
  }

  private boolean isNode(char c) {
    return Character.isLetterOrDigit(c);
  }

  private void checkAndAddAntinode(int x, int y) {
    if (x >= 0 && x < cols && y >= 0 && y < rows) {
      antinodes.add(new Point(x, y));
    }
  }

  private record Point(int x, int y) {}
}
