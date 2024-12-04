package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiPredicate;

public class Day4 {
  private final char[][] matrix;

  public Day4() {
    this.matrix = loadMatrix();
  }

  private char[][] loadMatrix() {
    try {
      Path path = Paths.get("src/main/resources/day4");
      List<String> lines = Files.readAllLines(path);

      if (lines.isEmpty()) {
        throw new IOException("Input file is empty");
      }

      char[][] matrix = new char[lines.size()][lines.getFirst().length()];
      for (int i = 0; i < lines.size(); i++) {
        matrix[i] = lines.get(i).toCharArray();
      }
      return matrix;

    } catch (IOException e) {
      System.err.println("Error reading input file: " + e.getMessage());
      return new char[0][0];
    }
  }

  public int findXMAS() {
    return findPattern(this::searchXMAS);
  }

  public int findMASCross() {
    return findPattern(this::searchMASCross);
  }

  private int findPattern(BiPredicate<Integer, Integer> searchFunction) {
    int count = 0;
    for (int row = 1; row < matrix.length - 1; row++) {
      for (int col = 1; col < matrix[0].length - 1; col++) {
        if (searchFunction.test(row, col)) {
          count++;
        }
      }
    }
    return count;
  }

  private boolean searchXMAS(int row, int col) {
    if (matrix[row][col] != 'X') return false;

    int[] rowDir = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] colDir = {-1, 0, 1, -1, 1, -1, 0, 1};
    String target = "XMAS";

    for (int dir = 0; dir < 8; dir++) {
      if (searchDirection(row, col, rowDir[dir], colDir[dir], target)) {
        return true;
      }
    }
    return false;
  }

  private boolean searchMASCross(int row, int col) {
    if (matrix[row][col] != 'A') return false;

    return (checkDiagonal(row, col, -1, -1) && checkDiagonal(row, col, -1, 1)) ||
        (checkDiagonal(row, col, 1, -1) && checkDiagonal(row, col, 1, 1));
  }

  private boolean checkDiagonal(int row, int col, int rowDir, int colDir) {
    return (matrix[row - rowDir][col - colDir] == 'M' && matrix[row + rowDir][col + colDir] == 'S') ||
        (matrix[row - rowDir][col - colDir] == 'S' && matrix[row + rowDir][col + colDir] == 'M');
  }

  private boolean searchDirection(int row, int col, int rowDir, int colDir, String target) {
    if (!isInBounds(row, col, rowDir, colDir, target.length())) {
      return false;
    }

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
    System.out.println("XMAS occurrences: " + solver.findXMAS());
    System.out.println("MAS X-pattern occurrences: " + solver.findMASCross());
  }
}
