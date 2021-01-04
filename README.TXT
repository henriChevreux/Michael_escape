Project: Michael's escape
Authors: Michael Kölling, David J. Barnes and Henri Chevreux

The core implementation of the project is part of the material for the book

   Objects First with Java - A Practical Introduction using BlueJ
   Sixth edition
   David J. Barnes and Michael Kölling
   Pearson Education, 2016
   
This project is a simple framework for an adventure game. 
The player can move between rooms, pickup/drop objects and interact with non player characters. 
In this version, there are multiple non player characters moving randomly between those rooms.

To start this application, create an instance of class "Game" and call its
"play" method.

This project was written as part of PPA coursework.

All the base and challenge tasks have been implemented.
Other additional tasks have been implemented.

Base tasks implemented:

  - The game has several locations/rooms.
  - The player can walk through the locations.
  - There are items in some rooms. Every room can hold any number of items. Some items can be picked up by the player, others can’t.
  - The player can carry some items with him. Every item has a weight. The player can carry items only up to a certain total weight.
  - The player can win. The player is informed that they have won at the end of the game.
  - Implement a command “back” that takes you back to the last room you’ve been in.
  - Add at least four new commands (inventory, back, talk, give, viewMap, pickup, drop).
  
Challenge tasks implemented:
  
  - Added characters to the game. Characters are people or animals or monsters – anything that moves. Characters are also in rooms (like the player and the items). Unlike items, characters can move around by themselves.
  - Extended the parser to recognise three-word commands. You could, for example, have a command give bread dwarf to give some bread (which you are carrying) to the dwarf.
  - Added a magic transporter room – every time you enter it you are transported to a random room in your game.

Additional challenge tasks implemented:
  - Implemented general game logic, the player has to talk to the key holder and enter the correct digit code in the exit room to win.
  - Added a narrative plot-twist at the end of the game.
  - Added a visual map that the player can display by picking up the map object in the main hub.
  - Added a game over logic when the player dies.
  - Added a time limit, the player has a maximum number of minutes (one minute is one move from a room to another). Sends a game over and exits the game if time limit is over.
  - Added a trap room. Ends a game over and exits the game when entered.
  - Implemented a proper Player class as a subclass of Entity to avoid code repetition.
  
Read chapter 8 of the book to get a detailed description of the project.
