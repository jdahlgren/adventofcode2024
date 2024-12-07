package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Day6 {

  private enum Direction {
    NORTH(0, -1),
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }

    public Direction turnRight() {
      return switch (this) {
        case NORTH -> EAST;
        case EAST -> SOUTH;
        case SOUTH -> WEST;
        case WEST -> NORTH;
      };
    }
  }

  private record Position(int x, int y) {

  }

  public int countDistinctLocations() throws IOException {
    List<String> lines = Files.readAllLines(Path.of("src/main/resources/day6"));
    char[][] map = createMap(lines);
    Position startPos = findStartPosition(map);
    Set<Position> visitedPositions = new HashSet<>();

    Direction currentDirection = Direction.NORTH;
    Position currentPos = startPos;
    visitedPositions.add(currentPos);

    while (true) {
      Position nextPos = new Position(
          currentPos.x + currentDirection.dx,
          currentPos.y + currentDirection.dy
      );

      if (!isInsideMap(nextPos, map)) {
        break;
      }

      if (isObstacle(nextPos, map)) {
        currentDirection = currentDirection.turnRight();
        continue;
      }

      currentPos = nextPos;
      visitedPositions.add(currentPos);
    }

    return visitedPositions.size();
  }

  private char[][] createMap(List<String> lines) {
    char[][] map = new char[lines.size()][lines.get(0).length()];
    for (int i = 0; i < lines.size(); i++) {
      map[i] = lines.get(i).toCharArray();
    }
    return map;
  }

  private Position findStartPosition(char[][] map) {
    for (int y = 0; y < map.length; y++) {
      for (int x = 0; x < map[y].length; x++) {
        if (map[y][x] == '^') {
          return new Position(x, y);
        }
      }
    }
    throw new IllegalStateException("No start position found");
  }

  private boolean isInsideMap(Position pos, char[][] map) {
    return pos.y >= 0 && pos.y < map.length &&
        pos.x >= 0 && pos.x < map[pos.y].length;
  }

  private boolean isObstacle(Position pos, char[][] map) {
    return map[pos.y][pos.x] == '#';
  }

  public static void main(String[] args) throws IOException {
    Day6 day6 = new Day6();
    int distinctLocations = day6.countDistinctLocations();
    System.out.println("Number of distinct locations visited: " + distinctLocations);

  }
}
