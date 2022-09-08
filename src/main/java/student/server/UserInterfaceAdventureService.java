package student.server;

import com.google.gson.Gson;
import java.sql.SQLException;
import student.adventure.Game;
import student.adventure.Player;
import student.adventure.Room;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class UserInterfaceAdventureService implements AdventureService {

  private final static String DATABASE_URL = "jdbc:sqlite:src/main/resources/adventures.db";
  private Map<Integer, Game> gameMap = new HashMap<>();
  private Map<Integer, GameStatus> gameStatusMap = new HashMap<>();
  private DatabaseHelper database;
  public static final String WELCOME = "Hello, welcome to Avengers Adventures";

  {
    try {
      database = new DatabaseHelper(DATABASE_URL);
    } catch (
        SQLException throwables) {
      throwables.printStackTrace();
    }
  }


  @Override
  public void reset() {
    gameStatusMap = null;
    gameMap = null;
    database.truncate();
  }

  @Override
  public int newGame() throws AdventureException {
    //	System.out.println("reached");
    int id = (int) (Math.random() * Integer.MAX_VALUE);
    gameMap.put(id, loadGame("src/main/resources/Rooms.json"));
    gameStatusMap.put(id, new GameStatus(false, id, WELCOME, null, null,
        new AdventureState(), null));
    return id;
  }

  @Override
  public GameStatus getGame(int id) {
    Game currentGame = gameMap.get(id);
    GameStatus gameStatus = gameStatusMap.get(id);
    Map<String, List<String>> commandOptions = new HashMap<>();
    Room currentRoom = currentGame.getCurrentRoom();

    // if the user has finished the game

    //if the user is in the process of taking a locked item
    if (currentGame.getIsTakeInProgress()) {
      commandOptions.put("quit", new ArrayList<>());

      ArrayList<String> alphabetList = new ArrayList<>();
      String alphabet = "abcdefghijklmnopqrstuvwxyz";

      // creates a list of strings with all the letters as the elements
      for (int i = 0; i < alphabet.length(); i++) {
        alphabetList.add(alphabet.substring(i, i + 1));
      }

      commandOptions.put("guess", alphabetList);
    } else if (currentGame.getCurrentRoom().getRoomName().equals("End-Room")) {
      String examine = currentRoom.toString();

      List<String> playerInventory = currentGame.getPlayer().getItems();
      commandOptions.put("drop", playerInventory);

    } else {
      String examine = currentRoom.toString();

      //this is in the format of
      //You are in: ... \n from here you can go: ... \n items visible: ....
      String[] roomInfo = examine.split("\n", 3);

      //omits everything before the ":"
      String possibleDirections = roomInfo[1].substring(roomInfo[1].indexOf(":") + 1);
      String visibleItems = roomInfo[2].substring(roomInfo[2].indexOf(":") + 1);

      ArrayList<String> possibleDirectionsList = new ArrayList<>();
      ArrayList<String> visibleItemsList = new ArrayList<>();
      List<String> playerInventory = currentGame.getPlayer().getItems();

      for (String direction : possibleDirections.split(",")) {
        possibleDirectionsList.add(direction);
      }

      for (String item : visibleItems.split(",")) {
        visibleItemsList.add(item);
      }

      commandOptions.put("go", possibleDirectionsList);
      commandOptions.put("take", visibleItemsList);
      commandOptions.put("drop", playerInventory);

      if (currentRoom.getRoomName().equals("End-Room")) {
        commandOptions.put("exit", new ArrayList<>());
      }
    }
    gameStatus.setCommandOptions(commandOptions);

    return gameStatus;
  }

  @Override
  public boolean destroyGame(int id) {
    if (gameMap.get(id) == null) {
      return false;
    }

    gameMap.entrySet()
        .removeIf(entry -> (id == (entry.getKey())));

    gameStatusMap.entrySet()
        .removeIf(entry -> (id == (entry.getKey())));

    return true;
  }

  @Override
  public void executeCommand(int id, Command command) {
    Game game = gameMap.get(id);
    GameStatus gameStatus = gameStatusMap.get(id);
    String baseMessage = "";

    if (game.getIsTakeInProgress()) {
      baseMessage = game.getPasswordInstructions();
    }

    gameStatus
        .setMessage(baseMessage + "\n" +
            game
                .controlBoard(command.getCommandName() + " " + command.getCommandValue()));

    // this sets the state of the game to finished when the user finishes the game
    if (gameStatus.getMessage().equals("\nCongratulations! You finished the game!")) {
      game.setGameComplete(true);
    }

    // this is for when the user finds all needed items and have received the "you won" message
    if (game.isGameComplete()) {
      gameStatus.setCommandOptions(null);
      database.insertData(command.getPlayerName(), game.getNumberOfMoves());
    }
  }

  @Override
  public LinkedHashMap<String, Integer> fetchLeaderboard() {
    return database.getSortedLeaderBoard();
  }

  private Game loadGame(String jsonPath) {
    try {
      Gson gson = new Gson();
      Reader jsonReader = Files.newBufferedReader(Paths.get(jsonPath));
      Room[] rooms = gson.fromJson(jsonReader, Room[].class);
      Player player = new Player();
      return new Game(player, rooms);
    } catch (IOException e) {
      return null;
    }
  }

  // testing purposes only
  public Map<Integer, Game> getGameMap() {
    return gameMap;
  }

  // testing purposes only
  public Map<Integer, GameStatus> getGameStatusMap() {
    return gameStatusMap;
  }

  // testing purposes only
  public Game getGAME(int id) {
    return gameMap.get(id);
  }


}
