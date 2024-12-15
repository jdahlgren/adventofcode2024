package se.johannesdahlgren.aoc24;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Day13 {
  static class Position {
    int x, y;
    int tokens;
    String path;

    Position(int x, int y, int tokens, String path) {
      this.x = x;
      this.y = y;
      this.tokens = tokens;
      this.path = path;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Position position = (Position) o;
      return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
      return Objects.hash(x, y);
    }
  }

  static class Button {
    int dx, dy;

    Button(int dx, int dy) {
      this.dx = dx;
      this.dy = dy;
    }
  }

  public static int solveMachine(String input) {
    String[] lines = input.trim().split("\n");

    // Parse button A movement
    String[] aLine = lines[0].substring("Button A: ".length()).split(", ");
    Button buttonA = new Button(
        Integer.parseInt(aLine[0].substring(2)),
        Integer.parseInt(aLine[1].substring(2))
    );

    // Parse button B movement
    String[] bLine = lines[1].substring("Button B: ".length()).split(", ");
    Button buttonB = new Button(
        Integer.parseInt(bLine[0].substring(2)),
        Integer.parseInt(bLine[1].substring(2))
    );

    // Parse prize location
    String[] prizeLine = lines[2].substring("Prize: ".length()).split(", ");
    int targetX = Integer.parseInt(prizeLine[0].substring(2));
    int targetY = Integer.parseInt(prizeLine[1].substring(2));

    Queue<Position> queue = new LinkedList<>();
    Set<Position> visited = new HashSet<>();

    Position start = new Position(0, 0, 0, "");
    queue.add(start);
    visited.add(start);

    while (!queue.isEmpty()) {
      Position current = queue.poll();

      if (current.x == targetX && current.y == targetY) {
        return current.tokens;
      }

      Position afterA = new Position(
          current.x + buttonA.dx,
          current.y + buttonA.dy,
          current.tokens + 3,
          ""
      );

      Position afterB = new Position(
          current.x + buttonB.dx,
          current.y + buttonB.dy,
          current.tokens + 1,
          ""
      );

      if (!visited.contains(afterA) && isReasonablePosition(afterA, targetX, targetY)) {
        queue.add(afterA);
        visited.add(afterA);
      }

      if (!visited.contains(afterB) && isReasonablePosition(afterB, targetX, targetY)) {
        queue.add(afterB);
        visited.add(afterB);
      }
    }

    return -1; // Return -1 if no solution found
  }

  private static boolean isReasonablePosition(Position pos, int targetX, int targetY) {
    // Add some reasonable bounds to prevent infinite exploration
    int maxDistance = Math.max(Math.abs(targetX), Math.abs(targetY)) * 2;
    return Math.abs(pos.x) <= maxDistance && Math.abs(pos.y) <= maxDistance;
  }

  public static void main(String[] args) throws IOException {
    String input = Files.readString(Path.of("src/main/resources/day13"));
    String[] machines = input.split("\n\\s*\n");

    int totalTokens = 0;
    for (int i = 0; i < machines.length; i++) {
      int tokens = solveMachine(machines[i].trim());
      if (tokens != -1) {
        System.out.println("Machine " + (i + 1) + ": " + tokens + " tokens");
        totalTokens += tokens;
      } else {
        System.out.println("Machine " + (i + 1) + ": No solution found");
      }
    }
    System.out.println("Total tokens needed: " + totalTokens);
  }
}

