import java.util.HashSet;
import java.util.Set;
import java.util.HashMap;
import java.util.ArrayList;
/**
 * Class Room - a room in an adventure game.
 *
 * This class is part of the "Michael's escape" application. 
 * "Michael's escape" is a very simple, text based adventure game. 
 *
 * A "Room" represents one location in the scenery of the game.  It is 
 * connected to other rooms via exits.  For each existing exit, the room 
 * stores a reference to the neighboring room.
 * 
 * @author  Michael Kölling, David J. Barnes and Henri Chevreux
 * @version 2020.12.01
 */

public class Room 
{
    private String description;
    private boolean isFinalExit;
    private boolean isPortal;
    private boolean isTrap;
    private HashMap<String, Room> exits;        // stores exits of this room.
    private HashMap<String, String> items;
    private ArrayList<Entity> charactersInRoom;
    /**
     * Create a room described "description". Initially, it has
     * no exits. "description" is something like "a kitchen" or
     * "an open court yard".
     * @param description The room's description.
     */
    public Room(String description) 
    {
        this.description = description;
        exits = new HashMap<>();
        items = new HashMap<>();
        charactersInRoom = new ArrayList<>();
    }

    /**
     * Define an exit from this room.
     * @param direction The direction of the exit.
     * @param neighbor  The room to which the exit leads.
     */
    public void setExit(String direction, Room neighbor) 
    {
        exits.put(direction, neighbor);
    }
    
    /**
     * Define an item from this room.
     * @param name The name of the item.
     * @param weight The weight of the item.
     */
    public void setItem(String name, String weight)
    {
        items.put(name, weight);
    }
    
    /**
     * Define a character from this room.
     * @param character The character object.
     */
    public void setCharacter(Entity character)
    {
        charactersInRoom.add(character);
    }
    
    /**
     * Define the final exit as this room.
     */
    public void setFinalExit()
    {
        isFinalExit= !isFinalExit;
    }
    
    /**
     * Define this room as a portal room.
     */
    public void setPortal()
    {
        isPortal = !isPortal;
    }
    
    /**
     * Define this room as a trap room.
     */
    public void setTrap()
    {
        isTrap = !isTrap;
    }
    
    /**
     * Removes a given character from this room.
     * @param character The character to be removed from this room.
     */
    public void removeCharacter(Entity character)
    {
        if (charactersInRoom.contains(character)){
            charactersInRoom.remove(character);
        }
    }
    
    /**
     * Fetch and return a given character from this room.
     * @param nameOfCharacter The name of the wanted character.
     * @return The Entity object of the wanted character.
     */
    public Entity getCharacter(String nameOfEntity)
    {
        String answer= null;
        
        for (Entity character : charactersInRoom){
            if (character.getName().toLowerCase().equals(nameOfEntity.toLowerCase())){
                return character;
            }
        }
        return null;
    }
    
    /**
     * Fetch and return the characters 
     */
    public ArrayList<Entity> getCharactersInRoom()
    {
        return this.charactersInRoom;
    }
    
    /**
     * @return The short description of the room
     * (the one that was defined in the constructor).
     */
    public String getShortDescription()
    {
        return description;
    }

    /**
     * Return a description of the room in the form:
     *     You are in the kitchen.
     *     Exits: north west
     * @return A long description of this room
     */
    public String getLongDescription()
    {
        return "You are " + description + "\n" + getExitString() + ".\n" 
        + getItemsInRoomString() + ".\n" + getCharactersString();
    }

    /**
     * Return a string describing the room's exits, for example
     * "Exits: north west".
     * @return Details of the room's exits.
     */
    private String getExitString()
    {
        String returnString = "Exits:";
        Set<String> keys = exits.keySet();
        if (keys.size()==0){return "There are no exits in this room. You are trapped!";}
        for(String exit : keys) {
            returnString += " " + exit;
        }
        return returnString;
    }
    
    public String getItemsInRoomString()
    {
        String returnString = "Items in the room:";
        Set<String> keys = items.keySet();
        if (keys.size()==0){return "There are no items in this room.";}
        for (String itemName: keys){
            returnString+= " " + itemName + "(" + items.get(itemName) + ")";
        }
        return returnString;
    }
    
    public String getCharactersString()
    {
        String returnString = "Characters in the room:";
        if (charactersInRoom.size()==0){return "There are no characters in this room. you are alone.";}
        for (Entity character : charactersInRoom){
            returnString+= " " + character.getName();
        }
        return returnString;
    }
    
    /**
     * Add an item in the room.
     * @param key The String name of the item.
     * @param value The associated weight of the item.
     */
    public void addItem(String key, String value){
        items.put(key,value);
    }
    
    /**
     * Remove an item from the room.
     * @param key The String name of the item to be removed.
     */
    public void removeItem(String key){
        items.remove(key);
    }

    /**
     * Return the room that is reached if we go from this room in direction
     * "direction". If there is no room in that direction, return null.
     * @param direction The exit's direction.
     * @return The room in the given direction.
     */
    public Room getExit(String direction) 
    {
        return exits.get(direction);
    }
    
    /**
     * Get the weight of an object in the room.
     * @param objectName The String name of the object.
     * @return The String weight of the object.
     */
    public String getItemWeight(String itemName)
    {
        return items.get(itemName);
    }

    public boolean isFinalExit(){return isFinalExit;}
    
    public boolean isPortal(){return isPortal;}
    
    public boolean isTrap(){return isTrap;}
}

