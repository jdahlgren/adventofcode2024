package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day10 {
  private static final int[] DX = {0, 1, 0, -1}; // right, down, left, up
  private static final int[] DY = {1, 0, -1, 0};

  private final int[][] heightMap;
  private final int rows;
  private final int cols;
  private final List<Point> startPoints = new ArrayList<>();
  private final List<Point> endPoints = new ArrayList<>();

  public Day10(String filename) throws IOException {
    List<String> lines = Files.readAllLines(Path.of(filename));
    rows = lines.size();
    cols = lines.get(0).length();
    heightMap = new int[rows][cols];

    // Parse the height map and find start (0) and end (9) points
    for (int i = 0; i < rows; i++) {
      String line = lines.get(i);
      for (int j = 0; j < cols; j++) {
        heightMap[i][j] = Character.getNumericValue(line.charAt(j));
        if (heightMap[i][j] == 0) {
          startPoints.add(new Point(i, j));
        } else if (heightMap[i][j] == 9) {
          endPoints.add(new Point(i, j));
        }
      }
    }
  }

  public List<Integer> findReachableNines() {
    List<Integer> reachableNinesCount = new ArrayList<>();

    for (Point start : startPoints) {
      Set<Point> reachableNines = findReachableNinesFromStart(start);
      reachableNinesCount.add(reachableNines.size());
      System.out.println("Starting point (" + start.x + "," + start.y + ") can reach "
          + reachableNines.size() + " endpoints");
    }

    return reachableNinesCount;
  }

  private Set<Point> findReachableNinesFromStart(Point start) {
    Set<Point> visited = new HashSet<>();
    Set<Point> reachableNines = new HashSet<>();
    dfs(start, visited, reachableNines);
    return reachableNines;
  }

  private void dfs(Point current, Set<Point> visited, Set<Point> reachableNines) {
    visited.add(current);

    if (heightMap[current.x][current.y] == 9) {
      reachableNines.add(current);
      return;
    }

    int currentValue = heightMap[current.x][current.y];

    for (int i = 0; i < 4; i++) {
      int newX = current.x + DX[i];
      int newY = current.y + DY[i];
      Point next = new Point(newX, newY);

      if (isValid(newX, newY) && !visited.contains(next)) {
        int nextValue = heightMap[newX][newY];
        if (nextValue == currentValue + 1) {
          dfs(next, visited, reachableNines);
        }
      }
    }
  }

  private boolean isValid(int x, int y) {
    return x >= 0 && x < rows && y >= 0 && y < cols;
  }

  private static class Point {
    final int x;
    final int y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Point point = (Point) o;
      return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }

  public static void main(String[] args) throws IOException {
    Day10 solver = new Day10("src/main/resources/day10");
    List<Integer> reachableNinesCount = solver.findReachableNines();
    System.out.println("\nSummary of reachable 9s from each starting point: " + reachableNinesCount);
  }
}
