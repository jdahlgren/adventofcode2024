package se.johannesdahlgren.aoc24;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day12 {
  private List<List<Character>> grid = new ArrayList<>();
  private Set<Point> visited = new HashSet<>();
  private List<Plot> plots = new ArrayList<>();

  record Point(int row, int col) {}

  record Plot(Set<Point> points, char letter, int area, int perimeter) {}

  public void solve() {
    readInput();
    findPlots();
    System.out.println("Number of plots: " + plots.size());
    printPlots();
  }

  private void readInput() {
    try {
      Files.readAllLines(Path.of("src/main/resources/day12")).forEach(line -> {
        grid.add(line.chars()
            .mapToObj(ch -> (char) ch)
            .toList());
      });
    } catch (IOException e) {
      throw new RuntimeException("Error reading input file", e);
    }
  }

  private void findPlots() {
    for (int i = 0; i < grid.size(); i++) {
      for (int j = 0; j < grid.get(i).size(); j++) {
        Point point = new Point(i, j);
        if (!visited.contains(point)) {
          Set<Point> currentPlot = new HashSet<>();
          char letter = grid.get(i).get(j);
          dfs(i, j, letter, currentPlot);
          if (!currentPlot.isEmpty()) {
            int area = currentPlot.size();
            int perimeter = calculatePerimeter(currentPlot);
            plots.add(new Plot(currentPlot, letter, area, perimeter));
          }
        }
      }
    }
  }

  private int calculatePerimeter(Set<Point> plot) {
    int perimeter = 0;
    for (Point point : plot) {
      // Check all four sides of the current point
      Point[] neighbors = {
          new Point(point.row() + 1, point.col()), // down
          new Point(point.row() - 1, point.col()), // up
          new Point(point.row(), point.col() + 1), // right
          new Point(point.row(), point.col() - 1)  // left
      };

      for (Point neighbor : neighbors) {
        // A side adds to perimeter if either:
        // 1. It's outside the grid bounds
        // 2. The neighbor point is not part of the same plot
        if (isOutsideGrid(neighbor) || !plot.contains(neighbor)) {
          perimeter++;
        }
      }
    }
    return perimeter;
  }

  private boolean isOutsideGrid(Point point) {
    return point.row() < 0 || point.row() >= grid.size() ||
        point.col() < 0 || point.col() >= grid.get(0).size();
  }

  private void dfs(int row, int col, char letter, Set<Point> currentPlot) {
    if (row < 0 || row >= grid.size() || col < 0 || col >= grid.get(0).size()) return;

    Point point = new Point(row, col);
    if (visited.contains(point)) return;
    if (grid.get(row).get(col) != letter) return;

    visited.add(point);
    currentPlot.add(point);

    // Check all four directions
    dfs(row + 1, col, letter, currentPlot); // down
    dfs(row - 1, col, letter, currentPlot); // up
    dfs(row, col + 1, letter, currentPlot); // right
    dfs(row, col - 1, letter, currentPlot); // left
  }

  private void printPlots() {
    for (int i = 0; i < plots.size(); i++) {
      Plot plot = plots.get(i);
      System.out.printf("Plot %d (%c):%n", i + 1, plot.letter());
      System.out.printf("Area: %d%n", plot.area());
      System.out.printf("Perimeter: %d%n", plot.perimeter());

      // Find boundaries of the plot
      int minRow = plot.points().stream().mapToInt(Point::row).min().getAsInt();
      int maxRow = plot.points().stream().mapToInt(Point::row).max().getAsInt();
      int minCol = plot.points().stream().mapToInt(Point::col).min().getAsInt();
      int maxCol = plot.points().stream().mapToInt(Point::col).max().getAsInt();

      // Print the plot
      for (int r = minRow; r <= maxRow; r++) {
        for (int c = minCol; c <= maxCol; c++) {
          if (plot.points().contains(new Point(r, c))) {
            System.out.print(grid.get(r).get(c));
          } else {
            System.out.print('.');
          }
        }
        System.out.println();
      }
      System.out.println();
    }
  }

  public static void main(String[] args) {
    new Day12().solve();
  }
}
