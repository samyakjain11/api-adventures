package student.server;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;
import student.adventure.Game;

public class UserInterfaceAdventureServiceTest
{
  UserInterfaceAdventureService service;
  int gameID;

  @Before
  public void setUp() throws AdventureException {
    service = new UserInterfaceAdventureService();
    gameID = service.newGame();
  }

  @Test
  public void initialCommands()
  {
    Map<String, List<String>> expectedCommandOptions = new HashMap<>();

    ArrayList<String> goCommands = new ArrayList<>();
    goCommands.add(" southwest");
    goCommands.add(" southeast");

    ArrayList<String> takeCommands = new ArrayList<>();
    takeCommands.add(" shield");

    ArrayList<String> dropCommands = new ArrayList<>();

    expectedCommandOptions.put("drop", dropCommands);
    expectedCommandOptions.put("go", goCommands);
    expectedCommandOptions.put("take", takeCommands);

    assertEquals(expectedCommandOptions,
        service.getGame(gameID).getCommandOptions());
  }

  @Test
  public void initialMessage()
  {
    assertEquals(UserInterfaceAdventureService.WELCOME,
        service.getGame(gameID).getMessage());
  }

  @Test
  public void takeLockedItemMessage() {
    service.executeCommand(gameID, new Command("take", "shield"));

    assertEquals("\nOh no! This item seems to be locked in a safe! You must guess the "
        + "password of the safe in order to take this item.\n"
        + "your hint is what is captain america's shield made out of?\n"
        + "(pshh, if you cant figure out the password, enter all the letters in the alphabet "
        + "to skip/move on)",
        service.getGame(gameID).getMessage());
  }

  @Test
  public void takeLockedItemCommands() {
    service.executeCommand(gameID, new Command("take", "shield"));
    Map<String, List<String>> expectedCommandOptions = new HashMap<>();

    expectedCommandOptions.put("quit", new ArrayList<>());

    ArrayList<String> alphabetList = new ArrayList<>();
    String alphabet = "abcdefghijklmnopqrstuvwxyz";

    for (int i = 0; i < alphabet.length(); i++) {
      alphabetList.add(alphabet.substring(i, i + 1));
    }
    expectedCommandOptions.put("guess", alphabetList);

    assertEquals(expectedCommandOptions, service.getGame(gameID).getCommandOptions());
  }

  @Test
  public void destroyGame()
  {
    service.destroyGame(gameID);

    Map<Integer, Game> gameMap = new HashMap<>();
    Map<Integer, GameStatus> gameStatusMap = new HashMap<>();

    assertEquals(gameMap, service.getGameMap());
    assertEquals(gameStatusMap, service.getGameStatusMap());


  }

  @Test
  public void destroyNonExistingGame()
  {
    assertEquals(false, service.destroyGame(548583845));
  }

  @Test
  public void reset()
  {
    service.reset();
    assertEquals(null, service.getGameStatusMap());
    assertEquals(null, service.getGameMap());
  }

  @Test
  public void fetchLeaderBoard()
  {
    LinkedHashMap<String, Integer> expectedLeaderBoard = new LinkedHashMap<>();
    expectedLeaderBoard.put("samyak", 105);

    LinkedHashMap<String, Integer> actualLeaderBoard = service.fetchLeaderboard();
    assertEquals(expectedLeaderBoard, actualLeaderBoard);
  }

  @Test
  public void winner()
  {
    service.getGAME(gameID).getPlayer().setCurrentRoom("End-Room");
    service.getGAME(gameID).getCurrentRoom().addItem("shield");
    service.getGAME(gameID).getCurrentRoom().addItem("bruce banner's glasses");
    service.getGAME(gameID).getCurrentRoom().addItem("arc reactor");
    service.getGAME(gameID).getCurrentRoom().addItem("mjolnir");
    service.getGAME(gameID).getCurrentRoom().addItem("doctor strange's red cape");
    service.getGAME(gameID).getCurrentRoom().addItem("heart-shaped herb");
    service.getGAME(gameID).getCurrentRoom().addItem("web-shooters");
    service.getGAME(gameID).getCurrentRoom().addItem("danvers dog tag");
    service.getGAME(gameID).getCurrentRoom().addItem("element gun");
    service.getGAME(gameID).setNumberOfMoves(104);

    Command command = new Command("exit", "");
    command.setPlayerName("samyak");
    service.executeCommand(gameID, command);

    assertEquals("\nCongratulations! You finished the game!",service.getGame(gameID).getMessage());

  }




}
