package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.function.BiPredicate;
import java.util.stream.IntStream;

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
    return findPattern(this::searchXMAS);
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

  private boolean searchXMAS(int row, int col) {
    if (matrix[row][col] != 'X') return false;

    record Direction(int row, int col) {}

    Direction[] directions = {
        new Direction(-1, -1), new Direction(-1, 0), new Direction(-1, 1),
        new Direction(0, -1),  new Direction(0, 1),
        new Direction(1, -1),  new Direction(1, 0),  new Direction(1, 1)
    };

    return java.util.Arrays.stream(directions)
        .anyMatch(dir -> searchDirection(row, col, dir.row(), dir.col(), "XMAS"));
  }

  private boolean searchMASCross(int row, int col) {
    if (!isInBounds(row, col) || matrix[row][col] != 'A') return false;

    record DiagonalPair(int row1, int col1, int row2, int col2) {}

    var diagonalPairs = List.of(
        new DiagonalPair(-1, -1, -1, 1),
        new DiagonalPair(1, -1, 1, 1)
    );

    return diagonalPairs.stream()
        .anyMatch(pair -> checkDiagonal(row, col, pair.row1(), pair.col1()) &&
            checkDiagonal(row, col, pair.row2(), pair.col2()));
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
