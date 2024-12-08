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
  private final Map<Point, Character> antinodeToNode = new HashMap<>();

  public static void main(String[] args) throws IOException {
    Day8 day8 = new Day8();
    int part1 = day8.countAntinodes();
    int part2 = day8.countAntinodesWithPropagation();
    System.out.println("Part 1 - Number of antinodes: " + part1);
    System.out.println("Part 2 - Number of antinodes with propagation: " + part2);
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
    findAntinodes(findAllNodes());
    return antinodes.size();
  }

  public int countAntinodesWithPropagation() {
    Set<Point> allAntinodes = new HashSet<>();
    Map<Character, List<Point>> currentNodes = findAllNodes();
    Set<Point> newAntinodes;

    do {
      antinodes.clear();
      findAntinodes(currentNodes);
      newAntinodes = new HashSet<>(antinodes);
      newAntinodes.removeAll(allAntinodes);

      // Add new antinodes to allAntinodes
      allAntinodes.addAll(newAntinodes);

      // Add new antinodes as nodes for next iteration
      for (Point antinode : newAntinodes) {
        char nodeValue = antinodeToNode.get(antinode);
        currentNodes.computeIfAbsent(nodeValue, k -> new ArrayList<>()).add(antinode);
      }

    } while (!newAntinodes.isEmpty());

    return allAntinodes.size();
  }

  private void findAntinodes(Map<Character, List<Point>> nodes) {
    // For each group of same-value nodes, find potential antinodes
    for (Map.Entry<Character, List<Point>> entry : nodes.entrySet()) {
      char nodeValue = entry.getKey();
      List<Point> sameNodes = entry.getValue();

      for (int i = 0; i < sameNodes.size(); i++) {
        for (int j = i + 1; j < sameNodes.size(); j++) {
          Point p1 = sameNodes.get(i);
          Point p2 = sameNodes.get(j);

          // Calculate direction vector between points
          int dx = p2.x - p1.x;
          int dy = p2.y - p1.y;

          // Check potential antinodes in both directions
          checkAndAddAntinode(p1.x - dx, p1.y - dy, nodeValue);
          checkAndAddAntinode(p2.x + dx, p2.y + dy, nodeValue);
        }
      }
    }
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

  private void checkAndAddAntinode(int x, int y, char nodeValue) {
    if (x >= 0 && x < cols && y >= 0 && y < rows) {
      Point antinode = new Point(x, y);
      antinodes.add(antinode);
      antinodeToNode.put(antinode, nodeValue);
    }
  }

  private record Point(int x, int y) {}
}
