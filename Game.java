import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
/**
 *  This class is the main class of the "Michael's escape" application. 
 *  "Michael's escape" is a very simple, text based adventure game. The player
 *  can interact with items and characters, move around in a spaceship, and has
 *  to follow a narrative to win the game.
 * 
 *  To play this game, create an instance of this class and call the "play"
 *  method.
 * 
 *  This main class creates and initialises all the others: it creates all
 *  rooms, creates the parser and starts the game.  It also evaluates and
 *  executes the commands that the parser returns.
 * 
 * @author  Michael Kölling, David J. Barnes and Henri Chevreux
 * @version 2020.12.01
 */

public class Game 
{
    private Room mainHub, serversRoom, kitchen, enginesRoom, office, apartments, trapRoom, portalRoom, finalExitRoom;
    private Parser parser;
    private ArrayList<Room> commonRooms;
    private ArrayList<Entity> characters;
    private Random randomGenerator;
    private boolean wantToQuit;
    private Scanner input;
    private Player currentPlayer;
    private boolean talkedToKeyHolder;
    
    /**
     * Create the game and initialise its internal map with the player object.
     */
    public Game()
    {
        currentPlayer = new Player("Michael");
        createRooms();
        parser = new Parser();
        randomGenerator = new Random();
        input = new Scanner(System.in);
    }

    /**
     * Create all the rooms and link their exits together.
     */
    private void createRooms()
    {
        // create the rooms
        mainHub = new Room("in the main hub.");
        enginesRoom = new Room("in the engines room.");
        kitchen = new Room("in the kitchen.");
        apartments = new Room("in the apartments.");
        office = new Room("in the main office.");
        serversRoom= new Room("in the servers room.");
        
        //special rooms
        trapRoom = new Room("in a trap room!");
        portalRoom = new Room("in a portal room. You are being teleported to another room...");
        finalExitRoom = new Room("in the escape pod.");
        
        //add the common rooms to the commonRooms ArrayList(rooms that are not portalRoom nor finalExitRoom)
        commonRooms = new ArrayList<>();
        commonRooms.add(mainHub);
        commonRooms.add(enginesRoom);
        commonRooms.add(kitchen);
        commonRooms.add(apartments);
        commonRooms.add(office);
        commonRooms.add(serversRoom);
        
        
        createExits();
        
        createItems();
        
        createCharacters();
        
        //place the portal rooms
        portalRoom.setPortal();
        
        //place the exit
        finalExitRoom.setFinalExit();
        
        //place the trap rooms
        trapRoom.setTrap();
        
        currentPlayer.setCurrentRoom(enginesRoom);  // start game in the engines room
    }
    
    /**
     * initialise room exits.
     */
    private void createExits()
    {
        enginesRoom.setExit("north-east", serversRoom);
        enginesRoom.setExit("south-east", apartments);
        
        serversRoom.setExit("west", enginesRoom);
        serversRoom.setExit("east", mainHub);
        
        apartments.setExit("west", enginesRoom);
        apartments.setExit("east", mainHub);
        
        mainHub.setExit("south-west",apartments);
        mainHub.setExit("north-west", serversRoom);
        mainHub.setExit("south-east",kitchen);
        mainHub.setExit("north-east", office);
        
        kitchen.setExit("west", mainHub);
        kitchen.setExit("east", finalExitRoom);
        kitchen.setExit("south", trapRoom);
        
        office.setExit("north", portalRoom);
        office.setExit("west", mainHub);
        office.setExit("east", finalExitRoom);
        
        finalExitRoom.setExit("north-west", office);
        finalExitRoom.setExit("south-west", kitchen);
    }
    
    /**
     * initialise each item with their respective weight and initial room.
     */
    private void createItems()
    {
        // create objects in rooms
        enginesRoom.setItem("wrench", "30");
        enginesRoom.setItem("hammer","70");
        
        serversRoom.setItem("computer","50");
        
        apartments.setItem("lamp","20");
        apartments.setItem("bed","not pickable");
        
        mainHub.setItem("map","1"); //map item can be used with viewMap command
        
        kitchen.setItem("table", "not pickable");
        
        office.setItem("pen","5");
        
    }
    
    /**
     * initialise the characters.
     */
    private void createCharacters()
    {
        //create characters. Name of instance and String in parameter must be the same (not case sensitive)
        Entity henri = new Entity("Henri");
        Entity et = new Entity("E.T","Alien");
        
        //create the unique KeyHolder character
        KeyHolder david = new KeyHolder("David");
        
        //place characters in their initial rooms
        mainHub.setCharacter(henri);
        kitchen.setCharacter(et);
        mainHub.setCharacter(david);
        
        david.setDigitCode("12345");
    }
    
    /**
     *  Main play routine. Loops until end of play.
     */
    public void play() 
    {            
        printWelcome();
        wantToQuit=false;
        talkedToKeyHolder=false;
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the game is over.
        boolean finished = false;
        while (! finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for playing. Good bye.");
    }

    /**
     * Print out the opening message for the player.
     */
    private void printWelcome()
    {
        System.out.println();
        System.out.println("Hello Michael...");
        System.out.println("You are an intergalactic pirate and you have just sabotaged a spaceship by overheating the engines.");
        System.out.println("You have 10min until the ship explodes.");
        System.out.println("To win the game, you need to find your friend David and follow his instructions.");
        System.out.println("Good luck...");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println(currentPlayer.getCurrentRoom().getLongDescription());
    }
    
    /**
     * Print out the ending message for the player.
     */
    private void printEnd()
    {
        System.out.println();
        System.out.println("You found the exit, you beat the game. Well done!");
        System.out.println();
    }
    
    /**
     * Move every NPC to a respective random common room.
     */
    private void moveCharacters()
    {
        ArrayList<Entity> charactersInRoom = new ArrayList<>();
        for (Room commonRoom : commonRooms){
                //fetch the NPCs in every room
                charactersInRoom = commonRoom.getCharactersInRoom();
                
                for (int i = (charactersInRoom.size() - 1); i >= 0; i--){
                    int randomInt = randomGenerator.nextInt(commonRooms.size()-1);
                    Room targetRoom = commonRooms.get(randomInt); //fetch a random common room from the ArrayList
                    
                    //move the character to the randomly selected room
                    targetRoom.setCharacter(charactersInRoom.get(i));
                    commonRoom.removeCharacter(charactersInRoom.get(i));
                }
            }
    }
    
    /**
     * Ask for a code guess if the player has talked to the key holder. 
     * End the game with narrative plot twist if guess is correct.
     */
    private void finalExitProcedure()
    {
        Room playerRoom=currentPlayer.getCurrentRoom();
        if (playerRoom.isFinalExit()){ //the new room is a final exit
            System.out.println("You found the escape pod!");
            if (talkedToKeyHolder==true){//the player has to talk to the key holder before being able to enter the code
                System.out.println("Enter the code to open the escape pod: ");
                String codeGuess = input.nextLine().trim();//fetch the user input
                
                if (codeGuess.equals(KeyHolder.getDigitCode())){//the code is correct, game ends
                    System.out.println("The code is correct.");
                    plotTwistDecision(); //initialise the plot twist procedure
                    printEnd();         
                    System.out.println();
                    wantToQuit = true;  //game ends successfully
                } else {//otherwise, print out error
                    System.out.println("Wrong code, try again later!");
                }
            }
            else {
                System.out.println("You have to find and talk to your friend before using the escape pod. He can't be that far...");
            }
        }
    }
    
    /**
     * Teleport the player to a random common room.
     */
    private void portalProcedure()
    {
        Room playerRoom=currentPlayer.getCurrentRoom();
        if (playerRoom.isPortal()){
            System.out.println();
            System.out.println("You are " + playerRoom.getShortDescription());//Print out the description of the portal room
            System.out.println();
            int randomInt = randomGenerator.nextInt(commonRooms.size()-1);
            Room targetRoom=commonRooms.get(randomInt);//fetch a random common room
            currentPlayer.setCurrentRoom(targetRoom);
            System.out.println(targetRoom.getLongDescription());
            System.out.println(currentPlayer.getStringSteps());
        }
    }
    
    /**
     * End the game when player enters a trap room.
     */
    private void trapRoomProcedure()
    {
        Room playerRoom=currentPlayer.getCurrentRoom();
        if (playerRoom.isTrap()){
            System.out.println();
            System.out.println("You are " + playerRoom.getShortDescription());//Print out the description of the trap room
            System.out.println("The room suddenly depressurises and ejects you outside.");
            gameOver(1);
        }
    }
    
    /**
     * Narrative plot twist before ending the game.
     * Does not affect the end of the game.
     */
    private void plotTwistDecision()
    {
        System.out.println("Unfortunately you realise that the escape pod only supports one person.\n"
        +"You can choose to rush inside the pod and leave your friend or sacrifice yourself for him...");
        System.out.println("Make the right decision:    betray    sacrifice");
        String decision = input.nextLine().toLowerCase().trim(); //fetch the user input
        if (decision.equals("betray")){
            System.out.println("You rush inside the pod and initialise the launch.\n"+
            "It's already too late when your friend realises what you've just done.\n"+
            "You observe him being ejected in the space vacuum while you get away from the spaceship...");
        } else if (decision.equals("sacrifice")){
            System.out.println("You wait for your friend before going in. You both look at each other in a long silence.\n"+
            "Before you can even say a word he reaches to his laser weapon and shoots you in the right leg.\n"+
            "You hear him say 'I'm sorry' before seeing the pod getting away without you...");
        } else {
            System.out.println("Wrong input");
            plotTwistDecision();                //input is incorrect, repeat method
        }
    }
    
    /**
     * Recurrent operations when a player enters a room.
     * @param anyRoom The room the player has entered.
     */
    private void enteringRoomProcedure(Room anyRoom) {
        moveCharacters(); //NPCs move in random rooms when the player changes room
        currentPlayer.setCurrentRoom(anyRoom);//Move the player to the new room
        if (anyRoom.isPortal()){portalProcedure();}
        else if (anyRoom.isFinalExit()){finalExitProcedure();}
        else if (anyRoom.isTrap()){trapRoomProcedure();}
        else {                                                  //the room is common, normal procedure
            System.out.println(anyRoom.getLongDescription());
            System.out.println(currentPlayer.getStringSteps());
        }
        //Game over if time is over
        if (currentPlayer.kill()){
            gameOver(0);
        }
        
    }
    /**
     * Print out the type of game over text according to the exit code.
     * @param exitCode The integer unique to each type of game over.
     */
    private void gameOver(int exitCode)
    {
        if (exitCode==0){
            System.out.println("Too late, the spaceship exploded...");
        } else if (exitCode==1){
            System.out.println("You died in the space vaccum.");
        }                                                       //add other variations here
        else {
            System.out.println("Wrong exit code.");
        }                                               
        System.out.println("--GAME OVER--");
        wantToQuit=true;                    //terminate program
    }
    
    /**
     * Given a command, process (that is: execute) the command.
     * @param command The command to be processed.
     * @return true If the command ends the game, false otherwise.
     */
    private boolean processCommand(Command command) 
    {

        if(command.isUnknown()) {
            System.out.println("I don't know what you mean...");
            return false;
        }

        String commandWord = command.getCommandWord();
        if (commandWord.equals("help")) {
            printHelp();
        }
        else if (commandWord.equals("go")) {
            goRoom(command);
        }
        else if (commandWord.equals("inventory")) {
            openInventory(command);
        }
        else if (commandWord.equals("pickup")) {
            pickupItem(command);
        }
        else if (commandWord.equals("drop")) {
            dropItem(command);
        }
        else if (commandWord.equals("viewMap")) {
            viewMap(command);
        }
        else if (commandWord.equals("give")) {
            giveToCharacter(command);
        }
        else if (commandWord.equals("talk")) {
            talkToCharacter(command);
        }
        else if (commandWord.equals("back")){
            goBack();
        }
        else if (commandWord.equals("quit")) {
            wantToQuit = quit(command);
        }
        // else command not recognised.
        return wantToQuit;
    }
    
    // implementations of user commands:

    /**
     * Print out some help information.
     * Here we print some stupid, cryptic message and a list of the 
     * command words.
     */
    private void printHelp() 
    {
        System.out.println("You are lost.");
        System.out.println();
        System.out.println("Your command words are:");
        parser.showCommands();
    }

    /** 
     * Try to in to one direction. If there is an exit, enter the new
     * room, otherwise print an error message.
     */
    private void goRoom(Command command) 
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know where to go...
            System.out.println("Go where?");
        }

        String direction = command.getSecondWord();
        
        // Try to leave current room.
        Room newRoom = currentPlayer.getCurrentRoom().getExit(direction);
        
        if (newRoom == null) {
            System.out.println("There is no door!");
        }
        else {
            enteringRoomProcedure(newRoom);
        }
    }
    
    /**
     * Print out the content of player inventory.
     */
    private void openInventory(Command command)
    {
        System.out.println(currentPlayer.getStringInventory());
    }
    
    /**
     * Store given item in the player inventory.
     * Remove given item from the room.
     */
    private void pickupItem(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to pickup...
            System.out.println("Pickup which item?");
            return;
        }
        
        String itemToStore = command.getSecondWord();
        
        
        String weightOfItem = currentPlayer.getCurrentRoom().getItemWeight(itemToStore);
        
        int inventoryWeight = currentPlayer.getInventoryWeight();
        if (weightOfItem == null){ //result of the search is unsuccessful
            System.out.println("There is no such item in this room.");
        } else if (weightOfItem == "not pickable") { 
            System.out.println("This item is not pickable");
        } else if (inventoryWeight+Integer.parseInt(weightOfItem)>currentPlayer.getMaxWeight()){ 
            System.out.println("Maximum weight for the inventory has been reached. Drop items to reduce your weight.");
        } else { //rsult of the search is successful
            currentPlayer.addToInventory(itemToStore,weightOfItem);
            currentPlayer.getCurrentRoom().removeItem(itemToStore);
            System.out.println(itemToStore + " has been added to the inventory.");
            System.out.println(currentPlayer.getCurrentRoom().getItemsInRoomString());
            System.out.println(currentPlayer.getStringInventory());
        }
    }
    
    /**
     * Remove given item from player inventory.
     * Store given item in the room.
     */
    private void dropItem(Command command)
    {
        if(!command.hasSecondWord()) {
            // if there is no second word, we don't know what to pickup...
            System.out.println("Drop which object?");
            return;
        }
        String itemName = command.getSecondWord();
        String itemWeight = currentPlayer.getItemWeight(itemName);
        if (itemWeight==null){
            System.out.println("This object is not in your inventory");
        } else {
            currentPlayer.getCurrentRoom().addItem(itemName,itemWeight.toString());
            currentPlayer.removeItem(itemName);
            System.out.println(itemName + " has been removed from the inventory.");
            System.out.println(currentPlayer.getCurrentRoom().getItemsInRoomString());
            System.out.println(currentPlayer.getStringInventory());
        }
    }
    
    /**
     * Add the given item to the inventory of the given character.
     * Print out the updated inventory of the player.
     */
    private void giveToCharacter(Command command)
    {
        if(!command.hasThirdWord()) {
            // if there is no second or third word, we don't know who to talk to...
            System.out.println("Invalid parameters. Enter 'give -itemName- -characterName-'.");
            return;
        }
        //check if item is in the inventory of player
        String itemName = command.getSecondWord();
        if (!currentPlayer.isInInventory(itemName)){
            System.out.println("This item isn't in the inventory.");
            return;
        }
        //check if the character exists in this room
        Entity targetCharacter = currentPlayer.getCurrentRoom().getCharacter(command.getThirdWord());
        if (targetCharacter==null){
            System.out.println("This character doesn't exist in this room.");
        } else { //the command is valid, give to the character the given object
            String weigthOfObject=currentPlayer.getItemWeight(itemName); //fetch the weight of the given item
            targetCharacter.addToInventory(itemName,weigthOfObject);
            currentPlayer.removeItem(itemName);
            System.out.println(currentPlayer.getStringInventory());
        }
    }
    
    /**
     * Print out a visual game map if player has a map item in his inventory.
     */
    private void viewMap(Command command)
    {
        if(currentPlayer.isInInventory("map")){ //check if the player has a map item
        System.out.println(
        "                                                                  +--------------+                        \n"+
        "                                                                  |              |                        \n"+
        "                                                                  |      ???     |                        \n"+
        "                                                                  |              |                        \n"+
        "                                                                  +-------+------+                        \n"+
        "                                                                          |                               \n"+
        "                                                                          |                               \n"+
        "                                                                          |                               \n"+
        "                       +--------------+                           +-------+------+                        \n"+
        "                       |              |                           |              |                        \n"+
        "                       |              |                           |              |                        \n"+
        "        +--------------+ Servers Room +-------------+ +-----------+ main office  +--------------+         \n"+
        "        |              |              |             | |           |              |              |         \n"+
        "+-------+------+       |              |      +------+-+-----+     |              |      +-------+-------+ \n"+ 
        "|              |       +--------------+      |              |     +--------------+      |               | \n"+
        "|              |                             |              |                           |               | \n"+
        "| Engines Room |                             |    MainHub   |                           |   Escape Pod  | \n"+
        "|              |                             |              |                           |               | \n"+
        "|              |       +--------------+      |              |     +--------------+      |               | \n"+
        "+-------+------+       |              |      +------+-+-----+     |              |      +-------+-------+ \n"+
        "        |              |              |             | |           |              |              |         \n"+
        "        +--------------+  Apartments  +-------------+ +-----------+    Kitchen   +--------------+         \n"+
        "                       |              |                           |              |                        \n"+
        "                       |              |                           |              |                        \n"+
        "                       +--------------+                           +-------+------+                        \n"+
        "                                                                          |                               \n"+
        "                                                                          |                               \n"+
        "                                                                          |                               \n"+
        "                                                                  +-------+------+                        \n"+
        "                                                                  |              |                        \n"+
        "                                                                  |     ???      |                        \n"+
        "                                                                  |              |                        \n"+
        "                                                                  +--------------+                        \n");

        } 
        else{//otherwise print out error
            System.out.println("You need to find a map first...");
        }
    }
    /**
     * Print out a simple NPC tirade.
     * Print out the digit code if NPC is a keyHolder.
     */
    private void talkToCharacter(Command command)
    {
        if(!(command.hasThirdWord() && command.getSecondWord().equals("to"))) {
            // if there is no third word, we don't know who to talk to...
            System.out.println("Talk to who? Enter 'talk to -PlayerName-'.");
            return;
        }
        String characterName = command.getThirdWord();
        Entity searchedEntity = currentPlayer.getCurrentRoom().getCharacter(characterName); //fetch the NPC in the room
        if (searchedEntity!=null){
            System.out.println(searchedEntity.getDescription());
            if (searchedEntity instanceof KeyHolder){
                talkedToKeyHolder = true;
            }
        } else {//Print out error if NPC is not in the current room
            System.out.println("This character is not in the room.");
        }
    }
    
    /**
     * Set the room of the player to the one previously visited.
     */
    private void goBack()
    {
        Room newRoom = currentPlayer.getPreviousRoom();
        if (newRoom==null) {
            System.out.println("You have to move to another room before going back.");
        } else if (newRoom.isPortal()){
            System.out.println("You cannot teleport back...");
        } else {
            enteringRoomProcedure(newRoom);
        }
    }
    
    /** 
     * "Quit" was entered. Check the rest of the command to see
     * whether we really quit the game.
     * @return true, if this command quits the game, false otherwise.
     */
    private boolean quit(Command command)
    {
        if(command.hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        }
        else {
            return true;  // signal that we want to quit
        }
    }
}
