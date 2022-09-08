package student.adventure;

import com.fasterxml.jackson.databind.exc.InvalidNullException;
import com.google.gson.Gson;

import java.io.Reader;
import java.nio.file.*;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


public class SetUp {

  private Game game;
  private Player player;
  private Room[] rooms;

  /**
   * sets up the game by parsing the json file
   *
   * @param filePath represents the filepath of the json file
   * @return returns an empty string if everything works as intended
   * @throws IOException if the path of the json file is incorrect
   */
  public void setUpGame(String filePath) throws IOException {
    try {
      Gson gson = new Gson();
      Reader jsonReader = Files.newBufferedReader(Paths.get(filePath));
      rooms = gson.fromJson(jsonReader, Room[].class);
      player = new Player();
      game = new Game(player, rooms);
    } catch (NoSuchFileException e) {
      throw new NoSuchFileException("no such file exists");
    } catch (com.google.gson.JsonSyntaxException e) {
      throw new com.google.gson.JsonSyntaxException("malformed file");
    }

    // goes to all rooms but the ending because the ending room doesnt have items, hints, or passwords
    for (int i = 0; i < rooms.length - 1; i++) {
      Room room = rooms[i];

      if (!(room.getItems() != null
          && room.getRoomName() != null
          && room.getHint() != null
          && room.getPassword() != null)) {
        throw new IllegalArgumentException("not parsed properly");
      }
    }

    ArrayList<String> roomNames = new ArrayList<>();

    for (Room room : rooms) {
      roomNames.add(room.getRoomName());
    }

    //goes to all the rooms and checks if the directions are to valid rooms that actually exist
    for (Room current : rooms) {
      for (Map.Entry mapElement : current.getDirections().entrySet()) {
        if (!roomNames.contains(mapElement.getValue())) {
          throw new IllegalArgumentException("direction to room that does not exist");
        }
      }
    }
  }

  public void runGame() {
    Scanner scanner = new Scanner(System.in);
    String input;
    System.out.println(Game.INSTRUCTIONS);

    while (!game.getQuit()) {
      input = scanner.nextLine();
      System.out.print(game.controlBoard(input) + "\n> ");
    }
  }


}
