package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
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
    return (int) IntStream.range(1, matrix.length - 1)
        .mapToObj(row -> IntStream.range(1, matrix[0].length - 1)
            .filter(col -> searchFunction.test(row, col))
            .count())
        .mapToLong(Long::longValue)
        .sum();
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
    if (matrix[row][col] != 'A') return false;

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
    return (matrix[row - rowDir][col - colDir] == 'M' && matrix[row + rowDir][col + colDir] == 'S') ||
        (matrix[row - rowDir][col - colDir] == 'S' && matrix[row + rowDir][col + colDir] == 'M');
  }

  private boolean searchDirection(int row, int col, int rowDir, int colDir, String target) {
    if (!isInBounds(row, col, rowDir, colDir, target.length())) {
      return false;
    }

    return IntStream.range(0, target.length())
        .allMatch(i -> matrix[row + i * rowDir][col + i * colDir] == target.charAt(i));
  }

  private boolean isInBounds(int row, int col, int rowDir, int colDir, int length) {
    int endRow = row + (length - 1) * rowDir;
    int endCol = col + (length - 1) * colDir;

    return endRow >= 0 && endRow < matrix.length &&
        endCol >= 0 && endCol < matrix[0].length;
  }

  public static void main(String[] args) {
    var solver = new Day4();
    System.out.println("XMAS occurrences: " + solver.findXMAS());
    System.out.println("MAS X-pattern occurrences: " + solver.findMASCross());
  }
}
