package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day4 {
  private char[][] matrix;
  private List<String> foundLocations = new ArrayList<>();

  public Day4() {
    loadMatrix();
  }

  private void loadMatrix() {
    try {
      Path path = Paths.get("src/main/resources/day4");
      List<String> lines = Files.readAllLines(path);

      matrix = new char[lines.size()][lines.get(0).length()];
      for (int i = 0; i < lines.size(); i++) {
        matrix[i] = lines.get(i).toCharArray();
      }

    } catch (IOException e) {
      System.err.println("Error reading input file: " + e.getMessage());
    }
  }

  public List<String> findXMAS() {
    // Search in all 8 directions
    int[] rowDir = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] colDir = {-1, 0, 1, -1, 1, -1, 0, 1};
    String target = "XMAS";

    for (int row = 0; row < matrix.length; row++) {
      for (int col = 0; col < matrix[0].length; col++) {
        if (matrix[row][col] == 'X') {
          // Check all 8 directions from this X
          for (int dir = 0; dir < 8; dir++) {
            if (searchDirection(row, col, rowDir[dir], colDir[dir], target)) {
              String location = String.format("XMAS found at (%d,%d) in direction (%d,%d)",
                  row, col, rowDir[dir], colDir[dir]);
              foundLocations.add(location);
            }
          }
        }
      }
    }

    return foundLocations;
  }

  private boolean searchDirection(int row, int col, int rowDir, int colDir, String target) {
    if (row < 0 || row >= matrix.length || col < 0 || col >= matrix[0].length) {
      return false;
    }

    // Check if we can fit the word in this direction
    if (!isInBounds(row, col, rowDir, colDir, target.length())) {
      return false;
    }

    // Check each character
    for (int i = 0; i < target.length(); i++) {
      if (matrix[row + i * rowDir][col + i * colDir] != target.charAt(i)) {
        return false;
      }
    }

    return true;
  }

  private boolean isInBounds(int row, int col, int rowDir, int colDir, int length) {
    int endRow = row + (length - 1) * rowDir;
    int endCol = col + (length - 1) * colDir;

    return endRow >= 0 && endRow < matrix.length &&
        endCol >= 0 && endCol < matrix[0].length;
  }

  public static void main(String[] args) {
    Day4 solver = new Day4();
    List<String> results = solver.findXMAS();

    if (results.isEmpty()) {
      System.out.println("No occurrences of XMAS found");
    } else {
      System.out.println("Found " + results.size() + " occurrence(s) of XMAS:");
      results.forEach(System.out::println);
    }
  }
}
