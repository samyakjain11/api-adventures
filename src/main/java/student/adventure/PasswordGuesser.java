package student.adventure;

public class PasswordGuesser {

  private final String password;
  private String guessed = "_";

  public PasswordGuesser(String password) {
    this.password = password.toLowerCase();
    for (int strIndex = 1; strIndex < password.length(); strIndex++) {
      if (password.charAt(strIndex) != ' ') {
        guessed += "_";
      } else {
        guessed += " ";
      }
    }
  }

  /**
   * checks if the user has already guessed the password
   *
   * @return boolean that represents whether a user has guessed the password
   */
  public boolean hasGuessed() {
    return guessed.equals(password);
  }

  /**
   * this method isn't used for the current game engine, but could be useful for future
   * implementations of this game.
   *
   * @param input holds the guesses of the user for each password
   * @return returns a string that shows the guessed part of the password in a hangman sort of
   * fashion
   */
  public String takeStringInput(String input) {
    if (input == null) {
      return guessed;
    }

    input = input.toLowerCase();

    for (int i = 0; i < input.length(); i++) {
      takeCharInput(input.charAt(i));
    }
    return guessed;
  }

  /**
   * @param guessedChar the character the user guesses
   * @return represents the current state of the password in a hangman fashion
   */
  public String takeCharInput(char guessedChar) {
    guessedChar = Character.toLowerCase(guessedChar);

    if (guessed.indexOf(guessedChar) == -1 && password.indexOf(guessedChar) > -1) {

      int latestIndex = 0;

      while (password.indexOf(guessedChar, latestIndex) != -1) {

        latestIndex = password.indexOf(guessedChar, latestIndex);

        // changes the guessed String and makes it ready to be outputted
        guessed =
            guessed.substring(0, latestIndex) + guessedChar + guessed.substring(latestIndex + 1);

        // need to increment this here so its not an infinite loop
        latestIndex++;
      }
    }
    return guessed;
  }

}
