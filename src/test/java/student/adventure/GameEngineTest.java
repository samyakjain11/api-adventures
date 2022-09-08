package student.adventure;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/** NEED TO REWRITE TESTS FOR THE NEW PROJECT DESIGN!!!!! */
public class GameEngineTest {
  private Game game;
  private Player player;
  private Room[] rooms;

  @Before
  public void setUp() throws IOException {
    // parsing values and initializing variables for testing
    Gson gson = new Gson();
    Reader jsonReader = Files.newBufferedReader(Paths.get("src/main/resources/Rooms.json"));
    player = new Player();
    rooms = gson.fromJson(jsonReader, Room[].class);
    game = new Game(player, rooms);
  }

  // quit command
  @Test
  public void controlBoardQuitHappyPath() {
    game.controlBoard("quit");
    assertEquals(true, game.getQuit());
  }

  // examine command
  @Test
  public void controlBoardExamine() {
    assertEquals(
        "You are in the Captain America room.\n"
            + "From here you can go: southwest, southeast\n"
            + "Items Visible: shield",
        game.controlBoard("examine"));
  }

  // history command (additional feature)
  @Test
  public void controlBoardHistory() {
    game.controlBoard("go southeast");

    assertEquals("You have visited: Captain America, Iron Man", game.controlBoard("history"));
  }

  @Test
  public void controlBoardHistoryOnlyStart() {
    assertEquals("You have visited: Captain America", game.controlBoard("history"));
  }

  // go command
  @Test
  public void controlBoardGoHappyPath() {
    assertEquals(
        "You are in the Hulk room.\n"
            + "From here you can go: northeast, southeast\n"
            + "Items Visible: bruce banner's glasses",
        game.controlBoard("go Southwest"));
  }

  @Test
  public void controlBoardGoInvalidDirection() {
    assertEquals(
        "I do not recognize \"south\" \n Please try a different command!",
        game.controlBoard("go south"));
  }

  @Test
  public void controlBoardGoNonExistingDirection() {
    assertEquals(
        "I do not recognize \"somewhere\" \n Please try a different command!",
        game.controlBoard("go somewhere"));
  }

  // take command
  @Test
  // checks if output is the intended output
  public void controlBoardTakeLockedHappyPathOutput() {
    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");

    assertEquals("you took shield", game.controlBoard("guess m"));
  }

  @Test
  // this test checks if the item got removed from the room
  public void controlBoardTakeLockedHappyPathItemsInRoom() {
    // this lets me test it easily because i know the user is currently in the captain america room
    // and that that room is the first element on the array
    Room captAmerica = rooms[0];

    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");
    game.controlBoard("guess m");

    assertEquals("", captAmerica.getItemsAsString());
  }

  @Test
  // this test checks if the item got added to the player's inventory
  public void controlBoardTakeLockedHappyPathPlayerInventory() {
    Room captAmerica = rooms[0];

    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");
    game.controlBoard("guess m");

    ArrayList<String> items = new ArrayList<>();
    items.add("shield");

    assertEquals(items, player.getItems());
  }

  @Test
  // checks if output is the intended output
  public void controlBoardTakeUnlockedHappyPathOutput() {
    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");
    game.controlBoard("guess m");
    game.controlBoard("drop shield");

    assertEquals("you took shield", game.controlBoard("take shield"));
  }

  @Test
  // this test checks if the item got removed from the room
  public void controlBoardTakeUnlockedHappyPathItemsInRoom() {
    // this lets me test it easily because i know the user is currently in the captain america room
    // and that that room is the first element on the array
    Room captAmerica = rooms[0];

    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");
    game.controlBoard("guess m");
    game.controlBoard("drop shield");
    game.controlBoard("take shield");
    assertEquals("", captAmerica.getItemsAsString());
  }

  @Test
  // this test checks if the item got added to the player's inventory
  public void controlBoardTakeUnlockedHappyPathPlayerInventory() {
    Room captAmerica = rooms[0];

    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");
    game.controlBoard("guess m");
    game.controlBoard("drop shield");
    game.controlBoard("take shield");

    ArrayList<String> items = new ArrayList<>();
    items.add("shield");

    assertEquals(items, player.getItems());
  }

  @Test
  public void controlBoardTakeLockedQuit() {

    game.controlBoard("take shield");
    game.controlBoard("quit");

    assertEquals(true, game.getQuit());
  }

  @Test
  public void controlBoardTakeNothing() {
    assertEquals(
        "I do not recognize \"take\" \n Please try a different command!",
        game.controlBoard("take"));
  }

  @Test
  public void controlBoardTakeRandom() {
    assertEquals(
        "I do not recognize \"avengers\" \n Please try a different command!",
        game.controlBoard("take avengers"));
  }

  // drop command
  @Test
  // checks if the output is correct
  public void controlBoardDropHappyPathOutput() {
    // setup so the player has something to drop
    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");
    game.controlBoard("guess m");

    assertEquals("you dropped shield", game.controlBoard("drop shield"));
  }

  @Test
  // checks if the item got added to the room
  public void controlBoardDropHappyPathItemsInRoom() {
    Room captAmerica = rooms[0];

    String input = "vibranium";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);

    game.controlBoard("take shield");

    game.controlBoard("drop shield");

    assertEquals("shield", captAmerica.getItemsAsString());
  }

  @Test
  // check if it got removed from the player's inventory
  public void controlBoardDropHappyPathPlayerInventory() {
    Room captAmerica = rooms[0];

    game.controlBoard("take shield");
    game.controlBoard("guess v");
    game.controlBoard("guess i");
    game.controlBoard("guess b");
    game.controlBoard("guess r");
    game.controlBoard("guess a");
    game.controlBoard("guess n");
    game.controlBoard("guess u");
    game.controlBoard("guess m");

    game.controlBoard("drop shield");

    ArrayList<String> items = new ArrayList<>();

    assertEquals(items, player.getItems());
  }

  @Test
  public void controlBoardDropNonExistingItem() {
    assertEquals(
        "I do not recognize \"avengers\" \n Please try a different command!",
        game.controlBoard("drop avengers"));
  }

  @Test
  public void controlBoardDropNothing() {
    assertEquals(
        "I do not recognize \"drop\" \n Please try a different command!",
        game.controlBoard("drop"));
  }

  //guess command
  @Test
  public void controlBoardGuessHappyPath() {
    game.controlBoard("take shield");
    assertEquals("v________",game.controlBoard("guess v"));
  }

  @Test
  public void controlBoardGuessCapitals() {
    game.controlBoard("take shield");
    assertEquals("v________",game.controlBoard("guess V"));
  }

  @Test
  public void controlBoardGuessMultipleChars() {
    game.controlBoard("take shield");
    assertEquals("please make sure you're entering ONE(1) character at a time.",
            game.controlBoard("guess vasd"));
  }

  @Test
  public void controlBoardGuessNothing() {
    game.controlBoard("take shield");
    assertEquals("I do not recognize \"guess\" \n Please try a different command!",
            game.controlBoard("guess"));
  }

  @Test
  public void controlBoardGuessSpace() {
    game.controlBoard("take shield");
    assertEquals("I do not recognize \"guess\" \n Please try a different command!",
            game.controlBoard("guess "));
  }

  //random edge cases

  @Test
  public void controlBoardRandom() {
    assertEquals(
        "I do not recognize \"hello\" \n Please try a different command!",
        game.controlBoard("hello"));
  }

  @Test
  public void controlBoardWhitespaces() {

    assertEquals(
            "Oh no! This item seems to be locked in a safe! You must guess the password of the safe in order to take this item." +
                    "\nyour hint is what is captain america's shield made out of?" +
                    "\n(pshh, if you cant figure out the password, enter all the letters in the alphabet to skip/move on)",
        game.controlBoard("   take     shield     "));
  }

  @Test
  public void controlBoardNull() {
    assertEquals(
        "I do not recognize \"\" \n" + " Please try a different command!", game.controlBoard(null));
  }

  @Test
  public void controlBoardCapitalization() {
    assertEquals(
        "You are in the Captain America room.\n"
            + "From here you can go: southwest, southeast\n"
            + "Items Visible: shield",
        game.controlBoard("eXAmInE"));
  }

  @Test
  public void controlBoardSingleWordCommandMultipleWordsInput() {
    assertEquals(
        "You are in the Captain America room.\n"
            + "From here you can go: southwest, southeast\n"
            + "Items Visible: shield",
        game.controlBoard("examine this room"));
  }

  //win condition

  @Test
  public void winCondition() {
    // adds all the items from all the rooms to the final/end room
    for (Room current : rooms) {
      rooms[rooms.length - 1].addItem(current.getItemsAsString());
    }

    // sets the room that the player is currently in to the final/end room
    player.setCurrentRoom("End-Room");

    assertEquals("Congratulations! You finished the game!", game.controlBoard("exit"));
  }
}
