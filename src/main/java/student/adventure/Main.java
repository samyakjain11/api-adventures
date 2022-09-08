package student.adventure;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) throws IOException {
    SetUp setUp = new SetUp();
    setUp.setUpGame("src/main/resources/Rooms.json");
    setUp.runGame();

  }
}
