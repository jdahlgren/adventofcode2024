package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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

      // Skip robots on the middle lines
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
    final int ROOM_WIDTH = 11;  // Width of the room
    final int ROOM_HEIGHT = 7;  // Height of the room
    final int SECONDS = 10;     // How many seconds to simulate

    List<Robot> robots = readRobots("src/main/resources/day14");
    System.out.println("Initial state:");
    printQuadrantCounts(robots, ROOM_WIDTH, ROOM_HEIGHT);

    for (int second = 1; second <= SECONDS; second++) {
      robots = robots.stream()
          .map(r -> r.move(ROOM_WIDTH, ROOM_HEIGHT))
          .toList();

      System.out.println("\nAfter second " + second + ":");
      printQuadrantCounts(robots, ROOM_WIDTH, ROOM_HEIGHT);
    }
  }

  private static void printQuadrantCounts(List<Robot> robots, int width, int height) {
    long topLeft = robots.stream()
        .filter(r -> r.getQuadrant(width, height).equals("top-left"))
        .count();

    long topRight = robots.stream()
        .filter(r -> r.getQuadrant(width, height).equals("top-right"))
        .count();

    long bottomLeft = robots.stream()
        .filter(r -> r.getQuadrant(width, height).equals("bottom-left"))
        .count();

    long bottomRight = robots.stream()
        .filter(r -> r.getQuadrant(width, height).equals("bottom-right"))
        .count();

    long middle = robots.stream()
        .filter(r -> r.getQuadrant(width, height).equals("middle"))
        .count();

    System.out.println("Top-left: " + topLeft);
    System.out.println("Top-right: " + topRight);
    System.out.println("Bottom-left: " + bottomLeft);
    System.out.println("Bottom-right: " + bottomRight);
    System.out.println("On middle lines: " + middle);
    System.out.println("Total robots: " + robots.size());
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
