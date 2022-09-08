package student.adventure;

// import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

public class Room {

  private String room;
  private Map<String, String> item;
  private String hint;
  private String password;
  private Map<String, String> directions;

  public String getRoomName() {
    return room;
  }

  public Map<String, String> getItems() {
    return item;
  }

  public String getHint() {
    return hint;
  }

  public String getPassword() {
    // decodes the encoded password from json file
    // credits: https://mkyong.com/java/how-do-convert-byte-array-to-string-in-java/
    return new String(Base64.getDecoder().decode(password), StandardCharsets.UTF_8);
  }

  public void addItem(String item) {
    this.item.put(item, "unlocked");
  }

  public Map<String, String> getDirections() {
    return directions;
  }

  /**
   * @return represents the string that will be returned when the user inputs examine
   */
  public String toString() {
    if (this.room.equals("End-Room")) {
      return "You have reached the End-Room, drop all the items you picked up from all the rooms"
          + " here to win!!!!";
    }
    String roomName = "You are in the " + room + " room.";
    String possibleDirections = "From here you can go: ";
    String itemsVisible = "Items Visible: ";

    // iterates through map and adds all possible directions
    // source: https://www.geeksforgeeks.org/iterate-map-java/
    for (Map.Entry<String, String> entry : directions.entrySet()) {
      possibleDirections += entry.getKey() + ", ";
    }
    //possibleDirections += entry.getKey() + " to " + entry.getValue() + ", ";

    // removes the extra comma and whitespace added
    possibleDirections = possibleDirections.substring(0, possibleDirections.length() - 2);

    // iterates through map and adds all items that are in the room
    // source: https://www.geeksforgeeks.org/iterate-map-java/
    for (Map.Entry<String, String> entry : item.entrySet()) {
      itemsVisible += entry.getKey() + ", ";
    }

    // removes the extra comma and whitespace added if needed.
    if (!itemsVisible.equals("Items Visible: ")) {
      itemsVisible = itemsVisible.substring(0, itemsVisible.length() - 2);
    }

    return roomName + "\n" + possibleDirections + "\n" + itemsVisible;
  }

  // **used for testing purposes only**
  public String getItemsAsString() {
    String itemsInRoom = "";

    // iterates through map and adds all items that are in the room
    // source: https://www.geeksforgeeks.org/iterate-map-java/
    for (Map.Entry<String, String> entry : item.entrySet()) {
      itemsInRoom += entry.getKey() + ", ";
    }

    // removes comma if needed
    if (itemsInRoom.length() != 0) {
      return itemsInRoom.substring(0, itemsInRoom.length() - 2);
    } else {
      return itemsInRoom;
    }
  }
}
