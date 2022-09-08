package student.server;

import java.sql.*;
import java.util.LinkedHashMap;
import student.adventure.Game;

public class DatabaseHelper {


  private static Connection dbConnection;

  public DatabaseHelper(String databaseURL) throws SQLException {
    try {
      dbConnection = DriverManager.getConnection(databaseURL);
    } catch (SQLException throwables) {
      throw new SQLException("incorrect URL");
    }
  }

  /**
   * inserts records into the leaderboard.
   *
   * @param playerName    String that represents the name of the player
   * @param numberOfMoves int that represents the number of moves a user takes to finish the game
   */
  public void insertData(String playerName, int numberOfMoves) {

    if (numberOfMoves > Game.LEAST_NUMBER_OF_MOVES) {
      Statement statement = null;
      String st =
          "INSERT INTO leaderboard_samyakj3 VALUES ('" + playerName + "', " + numberOfMoves + ")";

      try {
        statement = dbConnection.createStatement();
        statement.executeUpdate(st);
      } catch (SQLException throwable) {
        throwable.printStackTrace();
      }
    } else {
      throw new IllegalArgumentException(
          "The number you entered is less than the least amount of moves!");
    }
  }

  /**
   * @return returns a LinkedHashMap which is sorted by ascending score, this means whichever player
   * used the least amount of moves to finish the game.
   */
  public LinkedHashMap<String, Integer> getSortedLeaderBoard() {
    String query = "SELECT * FROM leaderboard_samyakj3 ORDER BY score ASC";
    return tableToLinkedMap(query);
  }

  /**
   * @return gets unsorted leaderboard
   */
  public LinkedHashMap<String, Integer> getLeaderBoard() {
    String query = "SELECT * FROM leaderboard_samyakj3";
    return tableToLinkedMap(query);
  }

  /**
   * @param query represents the query that should be executed before returning map
   * @return represents a linkedHashMap that is generated from the schema.
   */
  private LinkedHashMap<String, Integer> tableToLinkedMap(String query) {
    LinkedHashMap<String, Integer> leaderboard = new LinkedHashMap();
    Statement statement;
    try {
      statement = dbConnection.createStatement();
      statement.execute(query);
      ResultSet results = statement.getResultSet();
      while (results.next()) {
        String playerName = results.getString(1);
        int score = results.getInt(2);
        leaderboard.put(playerName, score);
      }
    } catch (SQLException throwable) {
      throwable.printStackTrace();
    }
    return leaderboard;
  }


  /**
   * truncates (deletes all records) the table
   */
  public void truncate() {
    Statement statement = null;
    String st = "DELETE from leaderboard_samyakj3";

    try {
      statement = dbConnection.createStatement();
      statement.executeUpdate(st);
    } catch (SQLException throwable) {
      throwable.printStackTrace();
    }

  }


}