package student.server;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * A class to represent values in a game state.
 * <p>
 * Note: these fields should be JSON-serializable values, like Strings, ints, floats, doubles, etc.
 * Please don't nest objects, as the frontend won't know how to display them.
 * <p>
 * Good example: private String shoppingList;
 * <p>
 * Bad example: private ShoppingList shoppingList;
 */
@JsonSerialize
public class AdventureState {

  // TODO: Add any additional state your game needs to this object.
  // E.g.: If your game needs to display a life total, you could add:
  // private int lifeTotal;
  // ...and whatever constructor/getters/setters you'd need

  public AdventureState() {

  }

}
