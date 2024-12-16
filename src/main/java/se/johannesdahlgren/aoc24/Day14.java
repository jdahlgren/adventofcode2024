package se.johannesdahlgren.aoc24;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Day14 {
  record Robot(Pair position, Pair velocity) {}
  record Pair(int x, int y) {}

  public static void main(String[] args) throws Exception {
    List<Robot> robots = readRobots("src/main/resources/day14");
    System.out.println("Loaded " + robots.size() + " robots:");
    robots.forEach(System.out::println);
  }

  private static List<Robot> readRobots(String filename) throws Exception {
    List<Robot> robots = new ArrayList<>();
    Path path = Paths.get(filename);

    List<String> lines = Files.readAllLines(path);
    for (String line : lines) {
      if (line.isEmpty()) continue;

      String[] parts = line.split(" ");
      Pair position = parsePair(parts[0].substring(2)); // Remove "p="
      Pair velocity = parsePair(parts[1].substring(2)); // Remove "v="

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