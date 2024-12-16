package se.johannesdahlgren.aoc24;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Day14 {
  record Robot(Pair position, Pair velocity) {}
  record Pair(int x, int y) {}

  public static void main(String[] args) {
    List<Robot> robots = readRobots("/day14");
    System.out.println("Loaded " + robots.size() + " robots:");
    robots.forEach(System.out::println);
  }

  private static List<Robot> readRobots(String filename) {
    List<Robot> robots = new ArrayList<>();

    try {
      InputStream is = Day14.class.getResourceAsStream(filename);
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));

      String line;
      while ((line = reader.readLine()) != null) {
        if (line.isEmpty()) continue;

        String[] parts = line.split(" ");
        Pair position = parsePair(parts[0].substring(2)); // Remove "p="
        Pair velocity = parsePair(parts[1].substring(2)); // Remove "v="

        robots.add(new Robot(position, velocity));
      }

    } catch (Exception e) {
      e.printStackTrace();
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

