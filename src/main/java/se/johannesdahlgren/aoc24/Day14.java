package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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

      if (hasCluster(robots)) {
        System.out.println("\nPossible pattern at second " + second + ":");
        printRoom(robots, ROOM_WIDTH, ROOM_HEIGHT);
        System.out.println("Press Enter to continue...");
        System.in.read();
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

  private static boolean hasCluster(List<Robot> robots) {
    for (Robot robot : robots) {
      int neighborCount = 0;
      for (Robot other : robots) {
        if (robot == other) continue;

        // Check if robots are adjacent (including diagonally)
        int dx = Math.abs(robot.position.x - other.position.x);
        int dy = Math.abs(robot.position.y - other.position.y);
        if (dx <= 1 && dy <= 1) {
          neighborCount++;
          if (neighborCount >= 2) { // At least 3 robots in cluster (this one + 2 neighbors)
            return true;
          }
        }
      }
    }
    return false;
  }

  private static void printRoom(List<Robot> robots, int width, int height) {
    char[][] grid = new char[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        grid[y][x] = '.';
      }
    }

    // Mark robot positions
    for (Robot robot : robots) {
      grid[robot.position.y][robot.position.x] = '#';
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
