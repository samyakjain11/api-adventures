package student.server;

import static org.junit.Assert.assertEquals;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import student.server.DatabaseHelper;

public class DatabaseTester {

  DatabaseHelper db;
  private final static String DATABASE_URL = "jdbc:sqlite:src/test/java/resources/adventures.db";

  @Before
  public void setUp() {
    try {
      db = new DatabaseHelper(DATABASE_URL);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }

    //removes all existing records in the database that may cause errors
    db.truncate();
  }

  @Test
  public void constructorInvalidURL() {
    try {
      db = new DatabaseHelper("gksjdfhksjdf");
    }
    catch(SQLException e)
    {
      assertEquals("incorrect URL", e.getMessage());
    }
  }


  @Test
  public void insertValues() {
    db.insertData("samyak", 200);
    LinkedHashMap<String, Integer> expectedLeaderboard = new LinkedHashMap<>();
    expectedLeaderboard.put("samyak", 200);

    assertEquals(expectedLeaderboard, db.getLeaderBoard());
  }

  @Test
  public void insertNegativeScore() {
    try {
      db.insertData("samyak", -45);
    } catch (IllegalArgumentException e) {
      assertEquals("The number you entered is less than the least amount of moves!",
          e.getMessage());
    }
  }

  @Test
  public void insertSmallScore() {
    try {
      db.insertData("samyak", 10);
    } catch (IllegalArgumentException e) {
      assertEquals("The number you entered is less than the least amount of moves!",
          e.getMessage());
    }
  }

  @Test
  public void truncateRegularTable() {
    db.insertData("fsjk", 499);
    db.truncate();

    LinkedHashMap<String, Integer> expectedLeaderboard = new LinkedHashMap<>();
    assertEquals(expectedLeaderboard, db.getSortedLeaderBoard());

  }

  @Test
  public void getSortedLeaderBoard() {
    db.insertData("samyak", 200);
    db.insertData("hello", 400);
    db.insertData("hi", 105);
    db.insertData("sup", 179);

    LinkedHashMap<String, Integer> expectedLeaderboard = new LinkedHashMap<>();

    expectedLeaderboard.put("hi", 105);
    expectedLeaderboard.put("sup", 179);
    expectedLeaderboard.put("samyak", 200);
    expectedLeaderboard.put("hello", 400);

    assertEquals(expectedLeaderboard, db.getLeaderBoard());
  }

  // must make sure there arent any dummy values inside the table when testing a second time
  @After
  public void finish() {
    db.truncate();
  }


}
