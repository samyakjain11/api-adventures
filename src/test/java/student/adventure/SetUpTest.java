package student.adventure;

import com.google.gson.Gson;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.file.NoSuchFileException;
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

public class SetUpTest {

  SetUp setter;

  @Before
  public void setUp() {
    setter = new SetUp();
  }

  @Test
  //if it doesnt throw any exceptions, it works!
  public void parseValidFile() throws IOException {
    setter.setUpGame("src/test/java/resources/validRooms.json");
  }

  @Test
  public void parseMalformedFile() throws IOException {
    try{
      setter.setUpGame("src/test/java/resources/malformed.json");
    }
    catch(com.google.gson.JsonSyntaxException e)
    {
      assertEquals("malformed file", e.getMessage());
    }
  }

  @Test
  public void parseIncorrectFieldsFile() throws IOException {
    try {
      setter.setUpGame("src/test/java/resources/invalidFields.json");
    }
    catch(IllegalArgumentException e)
    {
      assertEquals("not parsed properly", e.getMessage());
    }
  }

  @Test
  public void parseNonexistingRoomDirections() throws IOException {
    try {
      setter.setUpGame("src/test/java/resources/nonExistingRoomDirections.json");
    }
    catch(IllegalArgumentException e)
    {
     assertEquals("direction to room that does not exist", e.getMessage());
    }
  }

  @Test
  public void parseInvalidFile() throws IOException {
    try {
      setter.setUpGame("src/test/java/resources/somerandomjsonfile.json");
    }
    catch(NoSuchFileException e)
    {
      assertEquals("no such file exists", e.getMessage());
    }
  }

  @Test
  //if this test runs that means that it ends successfully when "quit" is entered
  //this is simply because the game is in a while loop until it is finished.
  public void runGame() throws IOException {
    setter.setUpGame("src/test/java/resources/validRooms.json");

    String input = "quit";
    InputStream in = new ByteArrayInputStream(input.getBytes());
    System.setIn(in);

    setter.runGame();
  }
}



