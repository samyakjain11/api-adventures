package student.adventure;

import java.util.ArrayList;
import java.util.List;

public class Player {

  private final List<String> items;
  private final List<String> roomsVisited;
  private String currentRoom;
  private final String startRoom = "Captain America";

  public Player() {
    items = new ArrayList<>();
    roomsVisited = new ArrayList<>();
    currentRoom = startRoom;
    roomsVisited.add(startRoom);
  }

  public String getCurrentRoom() {
    return currentRoom;
  }

  public void setCurrentRoom(String currentRoom) {
    this.currentRoom = currentRoom;
    if (!roomsVisited.contains(currentRoom)) {
      roomsVisited.add(currentRoom);
    }
  }

  public List<String> getRoomsVisited() {
    return roomsVisited;
  }

  public List<String> getItems() {
    return items;
  }

  public void addItems(String item) {
    items.add(item);
  }
}
