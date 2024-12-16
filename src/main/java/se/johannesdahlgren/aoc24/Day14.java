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
  }
  record Pair(int x, int y) {}

  public static void main(String[] args) throws Exception {
    final int ROOM_WIDTH = 11;  // Width of the room
    final int ROOM_HEIGHT = 7;  // Height of the room
    final int SECONDS = 10;     // How many seconds to simulate

    List<Robot> robots = readRobots("src/main/resources/day14");
    System.out.println("Initial state:");
    robots.forEach(System.out::println);

    for (int second = 1; second <= SECONDS; second++) {
      robots = robots.stream()
          .map(r -> r.move(ROOM_WIDTH, ROOM_HEIGHT))
          .toList();

      System.out.println("\nAfter second " + second + ":");
      System.out.println("Robots: " + robots.size());
      robots.forEach(System.out::println);
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