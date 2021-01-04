/**
 * This class is part of the "Michael's escape" application. 
 * "Michael's escape" is a very simple, text based adventure game.
 * 
 * A Player class inherited from the Entity class.
 * In addition to the characteristics of an Entity,
 * A Player has a weight limit, a step limit.
 * 
 * Unlike an Entity that acts as a dynamic object stored in a room,
 * a Player has a currentRoom and previousRoom field 
 * that respectively points to its previous and current location.
 *
 * @author  Henri Chevreux
 * @version 2020.12.01
 */
public class Player extends Entity
{
    private static final int MAXWEIGHT= 100;
    private final static int MAXSTEPS = 10;
    private int numberOfSteps;
    private int totalWeightOfInventory;
    private Room currentRoom;
    private Room previousRoom;
    
    /**
     * Constructor for objects of class Player
     */
    public Player(String nameOfEntity, String identityOfEntity)
    {
        super(nameOfEntity, identityOfEntity);
        totalWeightOfInventory=0;
        currentRoom=null;
        previousRoom=null;
        numberOfSteps=0;
    }
    
    public Player(String nameOfEntity)
    {
        this(nameOfEntity, "Human");
    }
    
    /**
     * Return the number of rooms the currentPlayer can enter before game over.
     */
    public String getStringSteps()
    {
        int stepsRemaining = MAXSTEPS-numberOfSteps;
        return stepsRemaining+" minutes remaining until explosion";
    }
    
    /**
     * Return true if the player doesn't have any steps left.
     * Return false otherwise.
     */
    public boolean kill()
    {
        return numberOfSteps>=MAXSTEPS;
    }
    
    /**
     * Remove a given item from the player inventory.
     * @param The String name of the item to remove.
     */
    public void removeItem(String itemName)
    {
            Integer weightOfItem = Integer.parseInt(inventory.get(itemName));
            inventory.remove(itemName);
            incrementTotalWeightOfInventory(-weightOfItem);
    }

    /**
     * @return The content of the current player inventory and its total weight.
     */
    public String getStringInventory()
    {
        return super.getStringInventory()+"\n"+"Your current total weight:"+totalWeightOfInventory+"/"+getMaxWeight();
    }
    
    public void addToInventory(String itemName, String itemWeight)
    {
        incrementTotalWeightOfInventory(Integer.parseInt(itemWeight));
        inventory.put(itemName,itemWeight);
    }
    
     /**
     * Increment or decrement the value of the inventory weight.
     * @param value The value to increment or decrement to the inventory weight. 
     * @return Return true if the parameter is accepted. Return false otherwise.
     */
    public boolean incrementTotalWeightOfInventory(int value)
    {
        if (totalWeightOfInventory+value > MAXWEIGHT){
            return false;
        } else {totalWeightOfInventory+=value;
            return true;
        }
    }
    
    /**
     * Change the location of the player and update the previous location.
     * @param newRoom The new room of the player.
     */
    public void setCurrentRoom(Room newRoom)
    {
        previousRoom = currentRoom;
        currentRoom = newRoom;
        if (previousRoom != null && !newRoom.isPortal()) {   //Time does not pass if this is the first room or the room is a portal
            numberOfSteps++;        //increment number of steps taken by the player
        }
    }
    
    //other accesors
    public String getItemWeight(String itemName)
    {
        return inventory.get(itemName);
    }
    
    public int getInventoryWeight()
    {
        return totalWeightOfInventory;
    }
    
    public Room getCurrentRoom()
    {
        return currentRoom;
    }
    
    public Room getPreviousRoom()
    {
        return previousRoom;
    }
    
    public int getNumberOfSteps()
    {
        return numberOfSteps;
    }
    
    public int getMaxWeight()
    {
        return MAXWEIGHT;
    }
}
