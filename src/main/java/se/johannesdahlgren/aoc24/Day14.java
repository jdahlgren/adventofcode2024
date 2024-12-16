package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day14 {
  record Robot(Pair position, Pair velocity) {
    Robot move() {
      return new Robot(
          new Pair(position.x + velocity.x, position.y + velocity.y),
          velocity
      );
    }

    boolean isInBounds(int size) {
      return position.x >= 0 && position.x < size
          && position.y >= 0 && position.y < size;
    }
  }
  record Pair(int x, int y) {}

  public static void main(String[] args) throws Exception {
    final int ROOM_SIZE = 100;  // Size of the room (100x100)
    final int SECONDS = 10;     // How many seconds to simulate

    List<Robot> robots = readRobots("src/main/resources/day14");
    System.out.println("Initial state:");
    robots.forEach(System.out::println);

    for (int second = 1; second <= SECONDS; second++) {
      robots = robots.stream()
          .map(Robot::move)
          .filter(r -> r.isInBounds(ROOM_SIZE))
          .toList();

      System.out.println("\nAfter second " + second + ":");
      System.out.println("Robots remaining: " + robots.size());
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