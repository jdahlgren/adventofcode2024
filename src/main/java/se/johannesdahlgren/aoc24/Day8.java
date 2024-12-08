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
    // Find all nodes and group them by their value
    Map<Character, List<Point>> nodes = findAllNodes();

    // For each group of same-value nodes, find potential antinodes
    for (List<Point> sameNodes : nodes.values()) {
      for (int i = 0; i < sameNodes.size(); i++) {
        for (int j = i + 1; j < sameNodes.size(); j++) {
          Point p1 = sameNodes.get(i);
          Point p2 = sameNodes.get(j);

          // Calculate direction vector between points
          int dx = p2.x - p1.x;
          int dy = p2.y - p1.y;

          // Check potential antinodes in both directions
          checkAndAddAntinode(p1.x - dx, p1.y - dy);  // Before first node
          checkAndAddAntinode(p2.x + dx, p2.y + dy);  // After second node
        }
      }
    }

    return antinodes.size();
  }

  private Map<Character, List<Point>> findAllNodes() {
    Map<Character, List<Point>> nodes = new HashMap<>();
    for (int y = 0; y < rows; y++) {
      for (int x = 0; x < cols; x++) {
        if (isNode(map[y][x])) {
          nodes.computeIfAbsent(map[y][x], k -> new ArrayList<>())
              .add(new Point(x, y));
        }
      }
    }
    return nodes;
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
