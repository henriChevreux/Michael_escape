import java.util.HashMap;
import java.util.Set;
/**
 * This class is part of the "Michael's escape" application. 
 * "Michael's escape" is a very simple, text based adventure game.
 * 
 * An Entity is any Non-Player Character(NPC) in the game.
 * Each Entity has a name, identity and inventory.
 * Each Entity can store an infinite amount of objects.
 * 
 * When the Player moves to another room, 
 * each Entity is allocated to a random room to simulate a movement.
 *
 * @author Henri Chevreux
 * @version 2020.12.01
 */
public class Entity
{
    // instance variables - replace the example below with your own
    protected HashMap<String, String> inventory;
    protected String name;
    protected String identity;
    
    /**
     * Constructor for objects of class Entity
     */
    public Entity(String nameOfEntity, String identityOfEntity)
    {
        // initialise instance variables
        inventory = new HashMap<>();
        name = nameOfEntity;
        identity = identityOfEntity;
    }
    
    public Entity(String nameOfEntity)
    {
        this(nameOfEntity,"Human");
    }
    
    /**
     * Add the given item in the character inventory.
     * @param itemName The String name of the item to store.
     * @param itemWeight The String weight of the corresponding item to store.
     */
    public void addToInventory(String itemName, String itemWeight)
    {
        inventory.put(itemName,itemWeight);
        System.out.println("Thank you kind sir!");
    }
    
    /**
     * Find if given item is in the NPC inventory.
     * @itemName The String name of the object to look for.
     */
    public boolean isInInventory(String itemName)
    {
        return inventory.containsKey(itemName);
    }
    
    /**
     * @return The content of the NPC inventory.
     */
    public String getStringInventory()
    {
        String returnString = "Items in the inventory of "+name+":";
        Set<String> keys = inventory.keySet();
        if (keys.size()==0){return "no items in the inventory.";}
        for (String itemName: keys){
            returnString+= " " + itemName + "(" + inventory.get(itemName) + ")";
        }
        return returnString;
    }
    
    public String getName() {return name;}
    
    /**
     * @return A simple description of the NPC.
     */
    public String getDescription()
    {
        return "Hi, my name is "+name+" and I am a "+identity + ". You can talk to me or give me items.";
    }
}   
