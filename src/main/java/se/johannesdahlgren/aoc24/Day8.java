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
  private final Set<Point> allPossibleAntinodes = new HashSet<>();

  public static void main(String[] args) throws IOException {
    Day8 day8 = new Day8();
    int part1 = day8.countAntinodes();
    int part2 = day8.countAllPossibleAntinodes();
    System.out.println("Part 1 - Number of antinodes at double distance: " + part1);
    System.out.println("Part 2 - Number of antinodes at all distances: " + part2);
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
    findAntinodes(findAllNodes(), true);
    return antinodes.size();
  }

  public int countAllPossibleAntinodes() {
    findAntinodes(findAllNodes(), false);
    return allPossibleAntinodes.size();
  }

  private void findAntinodes(Map<Character, List<Point>> nodes, boolean onlyDouble) {
    // For each group of same-value nodes, find potential antinodes
    for (List<Point> sameNodes : nodes.values()) {
      for (int i = 0; i < sameNodes.size(); i++) {
        for (int j = i + 1; j < sameNodes.size(); j++) {
          Point p1 = sameNodes.get(i);
          Point p2 = sameNodes.get(j);

          // Calculate direction vector between points
          int dx = p2.x - p1.x;
          int dy = p2.y - p1.y;

          if (onlyDouble) {
            // Original behavior - only double distance
            checkAndAddAntinode(p1.x - dx, p1.y - dy, antinodes);
            checkAndAddAntinode(p2.x + dx, p2.y + dy, antinodes);
          } else {
            // Check all positions using this vector
            int gcd = gcd(Math.abs(dx), Math.abs(dy));
            int unitDx = dx / gcd;
            int unitDy = dy / gcd;

            // Check positions before p1
            int x = p1.x;
            int y = p1.y;
            checkAndAddAntinode(x, y, allPossibleAntinodes); // Include the first node position
            while (true) {
              x -= unitDx;
              y -= unitDy;
              if (!checkAndAddAntinode(x, y, allPossibleAntinodes)) {
                break;
              }
            }

            // Check positions after p2
            x = p2.x;
            y = p2.y;
            checkAndAddAntinode(x, y, allPossibleAntinodes); // Include the second node position
            while (true) {
              x += unitDx;
              y += unitDy;
              if (!checkAndAddAntinode(x, y, allPossibleAntinodes)) {
                break;
              }
            }

            // Check positions between p1 and p2
            x = p1.x + unitDx;
            y = p1.y + unitDy;
            while (x != p2.x || y != p2.y) {
              checkAndAddAntinode(x, y, allPossibleAntinodes);
              x += unitDx;
              y += unitDy;
            }
          }
        }
      }
    }
  }

  private int gcd(int a, int b) {
    if (b == 0) return Math.abs(a);
    return gcd(b, a % b);
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

  private boolean checkAndAddAntinode(int x, int y, Set<Point> targetSet) {
    if (x >= 0 && x < cols && y >= 0 && y < rows) {
      targetSet.add(new Point(x, y));
      return true;
    }
    return false;
  }

  private record Point(int x, int y) {}
}
