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
    cols = lines.getFirst().length();
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

  public Result solve() {
    List<Integer> reachableNinesCount = new ArrayList<>();
    int totalDistinctPaths = 0;

    for (Point start : startPoints) {
      Set<Point> reachableNines = new HashSet<>();
      int pathsFromThisStart = 0;

      for (Point end : endPoints) {
        int paths = countDistinctPaths(start, end);
        if (paths > 0) {
          reachableNines.add(end);
          pathsFromThisStart += paths;
        }
      }

      reachableNinesCount.add(reachableNines.size());
      totalDistinctPaths += pathsFromThisStart;

    }

    return new Result(reachableNinesCount, totalDistinctPaths);
  }

  private int countDistinctPaths(Point start, Point end) {
    List<List<Point>> allPaths = new ArrayList<>();
    List<Point> currentPath = new ArrayList<>();
    currentPath.add(start);
    findPaths(start, end, currentPath, allPaths);
    return allPaths.size();
  }

  private void findPaths(Point current, Point end, List<Point> currentPath,
      List<List<Point>> allPaths) {
    if (current.equals(end)) {
      allPaths.add(new ArrayList<>(currentPath));
      return;
    }

    int currentValue = heightMap[current.x][current.y];

    for (int i = 0; i < 4; i++) {
      int newX = current.x + DX[i];
      int newY = current.y + DY[i];
      Point next = new Point(newX, newY);

      if (isValid(newX, newY) && !currentPath.contains(next)) {
        int nextValue = heightMap[newX][newY];
        if (nextValue == currentValue + 1) {
          currentPath.add(next);
          findPaths(next, end, currentPath, allPaths);
          currentPath.removeLast();
        }
      }
    }
  }

  private boolean isValid(int x, int y) {
    return x >= 0 && x < rows && y >= 0 && y < cols;
  }

  public static class Result {
    final List<Integer> reachableNinesCount;
    final int totalDistinctPaths;

    Result(List<Integer> reachableNinesCount, int totalDistinctPaths) {
      this.reachableNinesCount = reachableNinesCount;
      this.totalDistinctPaths = totalDistinctPaths;
    }
  }

  private record Point(int x, int y) {

  }

  public static void main(String[] args) throws IOException {
    Day10 solver = new Day10("src/main/resources/day10");
    Result result = solver.solve();

    int totalReachableNines = result.reachableNinesCount.stream()
        .mapToInt(Integer::intValue)
        .sum();

    System.out.println("Total sum of all reachable 9s: " + totalReachableNines);
    System.out.println("Total number of distinct paths: " + result.totalDistinctPaths);
  }
}
