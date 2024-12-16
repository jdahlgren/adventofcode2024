package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day14 {
  record Robot(Pair position, Pair velocity) {
    Robot move(int width, int height) {
      int newX = Math.floorMod(position.x + velocity.x, width);
      int newY = Math.floorMod(position.y + velocity.y, height);
      return new Robot(new Pair(newX, newY), velocity);
    }

    String getQuadrant(int width, int height) {
      int midX = width / 2;
      int midY = height / 2;

      if (position.x == midX || position.y == midY) {
        return "middle";
      }

      if (position.x < midX) {
        return position.y < midY ? "top-left" : "bottom-left";
      } else {
        return position.y < midY ? "top-right" : "bottom-right";
      }
    }
  }
  record Pair(int x, int y) {}

  public static void main(String[] args) throws Exception {
    final int ROOM_WIDTH = 101;
    final int ROOM_HEIGHT = 103;
    final int SECONDS = 100;

    List<Robot> robots = readRobots("src/main/resources/day14");

    for (int second = 1; second <= SECONDS; second++) {
      robots = robots.stream()
          .map(r -> r.move(ROOM_WIDTH, ROOM_HEIGHT))
          .toList();

      // Check for clusters every few seconds
      if (second % 5 == 0) {
        Map<Pair, Integer> positions = new HashMap<>();
        for (Robot robot : robots) {
          positions.merge(robot.position, 1, Integer::sum);
        }

        // If we have any position with multiple robots, might be interesting
        boolean hasClusters = positions.values().stream().anyMatch(count -> count > 2);
        if (hasClusters) {
          System.out.println("\nPossible pattern at second " + second + ":");
          printRoom(robots, ROOM_WIDTH, ROOM_HEIGHT);
        }
      }
    }

    // Count final quadrants
    long topLeft = robots.stream()
        .filter(r -> r.getQuadrant(ROOM_WIDTH, ROOM_HEIGHT).equals("top-left"))
        .count();

    long topRight = robots.stream()
        .filter(r -> r.getQuadrant(ROOM_WIDTH, ROOM_HEIGHT).equals("top-right"))
        .count();

    long bottomLeft = robots.stream()
        .filter(r -> r.getQuadrant(ROOM_WIDTH, ROOM_HEIGHT).equals("bottom-left"))
        .count();

    long bottomRight = robots.stream()
        .filter(r -> r.getQuadrant(ROOM_WIDTH, ROOM_HEIGHT).equals("bottom-right"))
        .count();

    System.out.println("\nFinal counts after " + SECONDS + " seconds:");
    System.out.println("Top-left: " + topLeft);
    System.out.println("Top-right: " + topRight);
    System.out.println("Bottom-left: " + bottomLeft);
    System.out.println("Bottom-right: " + bottomRight);
    System.out.println("Product: " + (topLeft * topRight * bottomLeft * bottomRight));
  }

  private static void printRoom(List<Robot> robots, int width, int height) {
    char[][] grid = new char[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        grid[y][x] = '.';
      }
    }

    // Count robots at each position
    Map<Pair, Integer> counts = new HashMap<>();
    for (Robot robot : robots) {
      counts.merge(robot.position, 1, Integer::sum);
    }

    // Mark positions
    for (var entry : counts.entrySet()) {
      Pair pos = entry.getKey();
      int count = entry.getValue();
      grid[pos.y][pos.x] = count > 9 ? '*' : Character.forDigit(count, 10);
    }

    // Print the grid
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        System.out.print(grid[y][x]);
      }
      System.out.println();
    }
  }

  private static List<Robot> readRobots(String filename) throws Exception {
    List<Robot> robots = new ArrayList<>();
    Path path = Paths.get(filename);

    List<String> lines = Files.readAllLines(path);
    for (String line : lines) {
      if (line.isEmpty()) continue;

      String[] parts = line.split(" ");
      Pair position = parsePair(parts[0].substring(2));
      Pair velocity = parsePair(parts[1].substring(2));

      robots.add(new Robot(position, velocity));
    }

    return robots;
  }

  private static Pair parsePair(String input) {
    String[] coordinates = input.split(",");
    return new Pair(
        Integer.parseInt(coordinates[0]),
        Integer.parseInt(coordinates[1])
    );
  }
}

