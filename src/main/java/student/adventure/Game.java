package student.adventure;

import java.util.HashMap;

public class Game {

  public static final String EXAMINE_COMMAND = "examine";
  public static final String QUIT_COMMAND = "quit";
  public static final String GO_COMMAND = "go";
  public static final String TAKE_COMMAND = "take";
  public static final String DROP_COMMAND = "drop";
  public static final String HISTORY_COMMAND = "history";
  public static final String GUESS_COMMAND = "guess";
  public static final String EXIT_COMMAND = "exit";
  public static final String INSTRUCTIONS =
      "Welcome to Avengers Adventure! \nYou may use the command \""
          + EXAMINE_COMMAND
          + "\" to examine the current room you're in \nuse the command \""
          + QUIT_COMMAND
          + "\" to quit the game \nuse the command \""
          + GO_COMMAND
          + "\" to go in a specific direction \nuse the command \""
          + TAKE_COMMAND
          + "\" to take a visible item \nuse the command \""
          + DROP_COMMAND
          + "\" to drop a specific item\nuse the command \""
          + HISTORY_COMMAND
          + "\" to see which rooms you have already visited";
  public final String unrecognizable = "I do not recognize \"";
  public final String tryAgain = "\" \n Please try a different command!";
  public static final int LEAST_NUMBER_OF_MOVES = 104;

  private final HashMap<String, Room> stringToRoomMapper = new HashMap<>();
  private final Player player;
  private boolean quit;
  private PasswordGuesser passwordGuesser;
  private boolean isTakeInProgress = false;
  private String itemBeingTaken;
  private String passwordInstructions;
  private int numberOfMoves;
  private boolean gameComplete;

  public Game(Player player, Room[] rooms) {
    this.player = player;

    // initializes hashmap that maps a string to its specific room object
    for (Room room : rooms) {
      stringToRoomMapper.put(room.getRoomName(), room);
    }
  }

  /**
   * wrapper method that takes all user input and then diverts the input to the relevant helper
   * methods
   *
   * @param input represents what the user inputs
   * @return returns a string that represents what the user sees
   */
  public String controlBoard(String input) {
    numberOfMoves++;

    if (input == null || input.equals("")) {
      return unrecognizable + tryAgain;
    }

    // removes whitespaces at the end and beginning of input
    input = input.trim();
    input = input.toLowerCase();

    // removes double whitespaces
    input = input.replaceAll("\\s+", " ");

    // goes inside this if the password is being guessed right now
    if (isTakeInProgress && passwordGuesser != null) {
      return takeInProgressGuesses(input);
    }


    /* OLD IMPLEMENTATION
    //    if (input.contains(QUIT_COMMAND)) {
    //      quit = true;
    //      return "Quit successfully!!";
    //    } else if (input.contains(EXAMINE_COMMAND)) {
    //      return examine();
    //    } else if (input.contains(HISTORY_COMMAND)) {
    //      String arrayOfRoomsVisited = player.getRoomsVisited().toString();
    //
    //      // the substring removes the square brackets
    //      return "You have visited: "
    //          + arrayOfRoomsVisited.substring(1, arrayOfRoomsVisited.length() - 1);
    //    }
    //
    //    // the rest of the commands must have two worded inputs, if they dont, the user will be
    // prompted
    //    // to
    //    // try another command
    //    if (!input.contains(" ")) return unrecognizable + input + tryAgain;
    //
    //    String firstWord = input.substring(0, input.indexOf(" "));
    //    String remainingInput = input.substring(input.indexOf(" ") + 1);
    //
    //    switch (firstWord) {
    //      case GO_COMMAND:
    //        return go(remainingInput);
    //
    //      case TAKE_COMMAND:
    //        String output = take(remainingInput);
    //        if (output.equalsIgnoreCase(QUIT_COMMAND)) {
    //          quit = true;
    //          return "Quit successfully!!";
    //        }
    //        return output;
    //
    //      case DROP_COMMAND:
    //        return drop(remainingInput);
    //
    //      default:
    //        return (unrecognizable + input + tryAgain);
    //    }
     */
    String[] command;

    // this portion basically puts the input into a string[], if there are no spaces then the second
    // element is set to value null
    if (input.contains(" ")) {
      command = input.split(" ", 2);
    } else {
      command = new String[2];
      command[0] = input;
      command[1] = null;
    }

    switch (command[0]) {
      case QUIT_COMMAND:
        quit = true;
        return "Quit successfully!!";

      case EXAMINE_COMMAND:
        return examine();

      case HISTORY_COMMAND:
        String arrayOfRoomsVisited = player.getRoomsVisited().toString();

        // the substring removes the square brackets
        return "You have visited: "
            + arrayOfRoomsVisited.substring(1, arrayOfRoomsVisited.length() - 1);

      case EXIT_COMMAND:
        return exit();

      case GO_COMMAND:
        if (command[1] == null) {
          return unrecognizable + GO_COMMAND + tryAgain;
        }
        return go(command[1]);

      case TAKE_COMMAND:
        if (command[1] == null) {
          return unrecognizable + TAKE_COMMAND + tryAgain;
        }
        String output = take(command[1]);
        if (output.equalsIgnoreCase(QUIT_COMMAND)) {
          quit = true;
          return "Quit successfully!!";
        }
        return output;

      case DROP_COMMAND:
        if (command[1] == null) {
          return unrecognizable + DROP_COMMAND + tryAgain;
        }
        return drop(command[1]);

      default:
        return (unrecognizable + input + tryAgain);
    }
  }

  /**
   * called when user enters the examine command keyword this method will print all relevant
   * information of the current room that the player is in.
   */
  private String examine() {
    return (stringToRoomMapper.get(player.getCurrentRoom()).toString());
  }

  /**
   * this method changes the room that the player is currently in
   *
   * @param direction represents the direction the user wants to move in
   */
  private String go(String direction) {
    Room currentRoom = stringToRoomMapper.get(player.getCurrentRoom());
    Room nextRoom = stringToRoomMapper.get(currentRoom.getDirections().get(direction));

    if (nextRoom == null) {
      return unrecognizable + direction + tryAgain;
    }

    player.setCurrentRoom(nextRoom.getRoomName());
    return stringToRoomMapper.get(player.getCurrentRoom()).toString();
  }

  /**
   * @param item represents the item that the user wants to take
   */
  private String take(String item) {
    Room currentRoom = stringToRoomMapper.get(player.getCurrentRoom());

    // there's a try catch here because im attempting to get values from a hashmap using the string
    // item as the key, if that key doesn't exist, then a nullPointer is thrown and that tells me
    // that the item doesn't exist, which is taken care of by the catch by saying that the item
    // is unrecognizable
    try {
      if (currentRoom.getItems().get(item).equals("unlocked")) {
        player.addItems(item);

        // code for removing the item from the room
        // credits:
        // https://www.geeksforgeeks.org/remove-an-entry-using-key-from-hashmap-while-iterating-over-it/
        String keyToBeRemoved = item;
        currentRoom
            .getItems()
            .entrySet()
            .removeIf(entry -> (keyToBeRemoved.equalsIgnoreCase(entry.getKey())));

        return "you took " + item;

      } else if (currentRoom.getItems().get(item).equals("locked")) {
        isTakeInProgress = true;
        itemBeingTaken = item;
        passwordInstructions = "";
        passwordGuesser = new PasswordGuesser(currentRoom.getPassword());

        passwordInstructions += (
            "Oh no! This item seems to be locked in a safe! You must guess the password of the safe in order"
                + " to take this item.\n");
        passwordInstructions += ("your hint is " + currentRoom.getHint() + "\n");
        passwordInstructions += (
            "(pshh, if you cant figure out the password, enter all the letters in the alpha"
                + "bet to skip/move on)");

        return passwordInstructions;

      }
    } catch (NullPointerException e) {
      return unrecognizable + item + tryAgain;
    }
    return unrecognizable + item + tryAgain;
  }

  /**
   * @param item represents the item that the player wants to drop
   */
  private String drop(String item) {
    // this block of code goes through the player's inventory and checks for the item
    for (int i = 0; i < player.getItems().size(); i++) {
      if (player.getItems().get(i).equals(item)) {
        // adds the item to rooms items
        stringToRoomMapper.get(player.getCurrentRoom()).addItem(item);
        // removes the item from the player's inventory
        return "you dropped " + player.getItems().remove(i);
      }
    }
    return unrecognizable + item + tryAgain;
  }

  /**
   * @return boolean that represents whether the user has chosen to quit or not
   */
  public boolean getQuit() {
    return quit;
  }

  private String takeInProgressGuesses(String input) {
    Room currentRoom = stringToRoomMapper.get(player.getCurrentRoom());

    if (input.equals(QUIT_COMMAND)) {
      quit = true;
      return "Quit successfully!!";
    }
    // at this point the command should be two words. for example: "guess a"
    //
    else if (!input.contains(" ") || !input.substring(0, input.indexOf(" "))
        .equals(GUESS_COMMAND)) {
      return unrecognizable + input + tryAgain;
    }

    //captures whatever is after the space
    input = input.substring(input.indexOf(" ") + 1, input.length());

    if (input.length() > 1) {
      return "please make sure you're entering ONE(1) character at a time.";
    } else if (!Character.isLetter(input.charAt(0))) {
      return "please make sure to enter a LETTER from the english alphabet.";
    } else {
      String resultOfGuessingLetter = passwordGuesser.takeCharInput(input.charAt(0));
      if (passwordGuesser.hasGuessed()) {
        // for removing the item from the room
        // credits:
        // https://www.geeksforgeeks.org/remove-an-entry-using-key-from-hashmap-while-iterating-over-it/
        String keyToBeRemoved = itemBeingTaken;
        currentRoom
            .getItems()
            .entrySet()
            .removeIf(entry -> (keyToBeRemoved.equalsIgnoreCase(entry.getKey())));

        // for adding the item to the player's inventory
        player.addItems(itemBeingTaken);

        passwordGuesser = null;
        isTakeInProgress = false;

        return "you took " + itemBeingTaken;
      }
      return resultOfGuessingLetter;
    }
  }

  /**
   * @return represents a boolean which tells the main method whether the user has finished the game
   */
  private String exit() {
    Room currentRoom = stringToRoomMapper.get(player.getCurrentRoom());

    boolean isCurrentRoomFinalRoom = currentRoom.getRoomName().equals("End-Room");
    boolean doesFinalRoomHaveAllItems =
        currentRoom.getItems().containsKey("shield")
            && currentRoom.getItems().containsKey("bruce banner's glasses")
            && currentRoom.getItems().containsKey("arc reactor")
            && currentRoom.getItems().containsKey("mjolnir")
            && currentRoom.getItems().containsKey("doctor strange's red cape")
            && currentRoom.getItems().containsKey("heart-shaped herb")
            && currentRoom.getItems().containsKey("web-shooters")
            && currentRoom.getItems().containsKey("danvers dog tag")
            && currentRoom.getItems().containsKey("element gun");
    if (isCurrentRoomFinalRoom && doesFinalRoomHaveAllItems) {
      return "Congratulations! You finished the game!";
    } else {
      return ("Keep Playing! You must drop all your picked up items in the final room order to exit!");
    }
  }

  public Room getCurrentRoom() {
    return stringToRoomMapper.get(player.getCurrentRoom());
  }

  public boolean getIsTakeInProgress() {
    return isTakeInProgress;
  }

  public Player getPlayer() {
    return player;
  }

  public String getPasswordInstructions() {
    return passwordInstructions;
  }

  public int getNumberOfMoves() {
    return numberOfMoves;
  }

  public boolean isGameComplete() {
    return gameComplete;
  }

  public void setGameComplete(boolean gameComplete) {
    this.gameComplete = gameComplete;
  }

  //testing purposes only
  public void setNumberOfMoves(int numberOfMoves) {
    this.numberOfMoves = numberOfMoves;
  }


}
