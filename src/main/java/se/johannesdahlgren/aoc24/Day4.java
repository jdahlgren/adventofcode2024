package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiPredicate;

public record Day4(char[][] matrix) {

  public Day4() {
    this(loadMatrix());
  }

  private static char[][] loadMatrix() {
    try {
      List<String> lines = Files.readAllLines(Paths.get("src/main/resources/day4"));

      if (lines.isEmpty()) {
        throw new IOException("Input file is empty");
      }

      return lines.stream()
          .map(String::toCharArray)
          .toArray(char[][]::new);

    } catch (IOException e) {
      System.err.println("Error reading input file: " + e.getMessage());
      return new char[0][0];
    }
  }

  public int findXMAS() {
    int count = 0;
    String target = "XMAS";
    int[] rowDir = {-1, -1, -1, 0, 0, 1, 1, 1};
    int[] colDir = {-1, 0, 1, -1, 1, -1, 0, 1};

    for (int row = 0; row < matrix.length; row++) {
      for (int col = 0; col < matrix[0].length; col++) {
        if (matrix[row][col] == 'X') {
          for (int dir = 0; dir < 8; dir++) {
            if (searchDirection(row, col, rowDir[dir], colDir[dir], target)) {
              count++;
            }
          }
        }
      }
    }
    return count;
  }

  public int findMASCross() {
    return findPattern(this::searchMASCross);
  }

  private int findPattern(BiPredicate<Integer, Integer> searchFunction) {
    int count = 0;
    for (int row = 0; row < matrix.length; row++) {
      for (int col = 0; col < matrix[0].length; col++) {
        if (isInBounds(row, col) && searchFunction.test(row, col)) {
          count++;
        }
      }
    }
    return count;
  }

  private boolean searchMASCross(int row, int col) {
    if (!isInBounds(row, col) || matrix[row][col] != 'A') return false;

    return (checkDiagonal(row, col, -1, -1) && checkDiagonal(row, col, -1, 1)) ||
        (checkDiagonal(row, col, 1, -1) && checkDiagonal(row, col, 1, 1));
  }

  private boolean checkDiagonal(int row, int col, int rowDir, int colDir) {
    int mRow = row - rowDir;
    int mCol = col - colDir;
    int sRow = row + rowDir;
    int sCol = col + colDir;

    if (!isInBounds(mRow, mCol) || !isInBounds(sRow, sCol)) return false;

    return (matrix[mRow][mCol] == 'M' && matrix[sRow][sCol] == 'S') ||
        (matrix[mRow][mCol] == 'S' && matrix[sRow][sCol] == 'M');
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

  private boolean isInBounds(int row, int col) {
    return row >= 0 && row < matrix.length &&
        col >= 0 && col < matrix[0].length;
  }

  private boolean isInBounds(int row, int col, int rowDir, int colDir, int length) {
    int endRow = row + (length - 1) * rowDir;
    int endCol = col + (length - 1) * colDir;

    return isInBounds(row, col) && isInBounds(endRow, endCol);
  }

  public static void main(String[] args) {
    var solver = new Day4();
    System.out.println("XMAS occurrences: " + solver.findXMAS());
    System.out.println("MAS X-pattern occurrences: " + solver.findMASCross());
  }
}
