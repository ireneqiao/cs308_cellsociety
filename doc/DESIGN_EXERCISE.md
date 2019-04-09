

# Lab Exercise

Hsingchih Tang (ht114), Irene Qiao (isq), Carrie Hunner (clh87)

## 1. RPS Design Description

#### Weapon Class

```java
abstract class Weapon{
    private String myName;
    
    // return whether this Weapon is beaten by the other Weapon
    abstract boolean beat(Weapon w, List<Weapon> canBeat);
    
}
```

#### Player Class

```java
    
public class Player {
    private int myScore;
    private Weapon currentWeapon;

    // make player pick a Weapon from given list
    public void makePlay(List<Weapon> weapons){
        Random myRand = new Random();
        currentWeapon = weapons.get(myRand.nextInt(weapons.size()));
    }

    // update player's score
    public void updateScore(int delta){
        myScore+=delta;
    }
    
    
    // return player's current score
    public int getScore(){
        return myScore;
    }
    
    public Weapon getWeapon(){
        return currentWeapon;
    }
    
```
#### Game Class
```java
public class Game {
    private static final int POINTS_PER_WIN = 1;
    private static final int TOTAL_GAMES_PLAYED = 5;
    private static final String WEAPON_FILE = "";
    //WEAPON_FILE contains each weapon with a list of other weapons that it can beat
    private int numGames;
    private List<Weapon> weapons;
    private List<Player> players;
    
    
    private void setupGame(){
        // read in weapon names and corresponding weapons it can beat
        Scanner in = new Scanner(new FileReader(WEAPON_FILE));
        /*
         * PSEUDO CODE: Read the first string as the name of the current weapon, 
         * every line after is the weapons it can beat until a certain character is found (eg. //) 
         * and then next string is next weapon and continues until complete
         * */
    }
    
    // Assign new weapon to each player
    private void gameRound(){
    for (Player player: players )
        player.makePlay(weapons);
    }
    for(int i = 0; i<players.size(); i++){
        for (int j = i; j<players.size(); j++){
            if(players.get(i).getWeapon().beat(players.get(j).getWeapon())){
                players.get(i).updateScore(POINTS_PER_WIN);
            }
            else{
                players.get(j).updateScore(POINTS_PER_WIN);
            }
        }
    }
    numGames += 1;
}
```

## 2. Cell Society: High Level Design
- How does a Cell know what rules to apply for its simulation?
    - Different simulations will make use of its own corresponding subclass which extends the Cell class. The Cell class will be abstract, with rules of updating Cell states defined specifically in each of the subclasses.
    - The XML file will contain the type of simulation to run.
- How does a Cell know about its neighbors? How can it update itself without effecting its neighbors update?
    - A list of neighbors will be held by each cell in the Cell class. The Simulation class is responsible for adding neighbor Cells for each Cell during grid setup. The current state of the cell will be stored for access by neighbors, and the next state of the cell is stored separately.
    - The Simulation class calls a method (whichNextState) on each Cell to determine its next State, which is then stored in the Cell's myNextState parameter. After a full run on all Cells to find their next States, the Simulation loops through all Cells again to explicitly update the States.
- What is the grid? Does it have any behaviors? Who needs to know about it?
    - The grid is a 2D array of Cell objects stored in Simulation class.
    - The grid has width and height (instance variables stored in Simulation class) but no behaviors
    - The grid is used for initializing cells and calling methods for finding next States and updating States for all Cells from the Simulation class.
- What information about a simulation needs to be the configuration file?
    - Type of simulation
    - Width and height of grid
    - global parameters/configurations of cells and states
- How is the GUI updated after all the cells have been updated?
    - Each Cell contains a State object
    - After the grid of Cells is updated, the simulation will call methods from the State class for each Cell that will provide a graphical representation of the State.

## 3. Cell Society: Classes & XML File
#### Simulation Class
```java
// Responsible for reading in simulation configuration from XML file
// Runs simulation and visualization on all cells in the grid
public class Simulation {
    private int width;
    private int height;
    private Cell[][] cells;
    private ArrayList<State> states;
    private HashMap<State, double> stateToPercentage;
    
    private void setupState(){
        State s = new State(); //"state" depends on type of simulation because State is an abstract class
        s.setPercentage(something read from XML);
        s.setFill(something read from XML);
        states.add(s);
    }
    
    
    private void setupGrid(){
    // PSEUDO CODE: 
    // Read in XML file containing the simulation 2-D grid's width, height, percentage
    // Create new State based on XML
        for(Line stateLine : XML_file){
            setupState();
        }
        
        for (int i = 0; i<width; i++){
            for (int j = 0; j<height; j++){
                //construct Cell objects
                //introduce random state assignment
                State randomState = states.get(random)
                cells.add(new Cell(width, height, r, c, randomState);
            }
        }
        //set up list of neighbors of each Cell
        for (int i = 0; i<width; i++){
            for (int j = 0; j<height; j++){
                setupNeighbors(cells[i][j]);
            }
        }
        
    }
    
    // USER INTERACTION METHODS
    // Start/resume simulation
    private void resume(){
    
    }
    
    // Pause simulation
    private void pause(){
    
    }
    
    // Generate visualization
    private void visualize(){
    
    }
       
    // Add valid Cell neighbor to c's collection of neighbors.
    private void setupNeighbors(Cell c){
        c.addNeighbor(some Cell);
    }
    
    //for each Cell, determine next state based on current states of neighbors
    private void updateNextState(){
        for (int i = 0; i<width; i++){
            for (int j = 0; j<height; j++){
                cells[i][j].whichNextState();
            }
        }
    }
     
    //for each Cell, assign next state to current state
    private void updateGrid(){
        for (int i = 0; i<width; i++){
            for (int j = 0; j<height; j++){
                cells[i][j].updateState();
            }
        }
    }  
}

```
#### State Class
```javascript
// Stores basic configurations of States that might be held by a Cell
abstract public class State(){
    private Color myFill;
    private double myPercentage;
    public State(){
    }
/* Place holder method
abstract public State nextState(){ //possibly include parameters for neighboring conditions for variety of possible future states
    //return next state
    }}
*/
}
```
#### Cell Class
```java
// Smallest component of the grid in a simulation
// Can hold and change States
abstract public class Cell (){
    private State myCurrentState    //cell state
    private State myNextState; 
    private final ArrayList<Cell> myNeighbors;
    private int myRow;
    private int myCol;
    private int myHeight;
    private int myWidth;
    
    Cell(int gridWidth, int gridHeight, int row, int col, int state)
    
    // Apply a set of specific rules to decide the next state for this Cell
    public abstract void whichNextState(){
    
    }
    
    // set myCurrentState to the value stored in myNextState
    public void updateState(){
        myCurrentState = myNextState;
    } 
}

    
    public void addNeighbor(Cell c){
        myNeighbors.add(c);
    }
```
##### Fire simulation
```java
public class FireCell extends Cell{

}
```

##### Segregation simulation
```java
public class SegCell extends Cell{

}
```
##### Percolation simulation
```java
public class PercCell extends Cell{

}
```
##### Other subclasses of Cell super class
- depends on the simulation
- different subclasses for 5 assigned simulations


##### XML File
- Type of simulation
- Read width and height of grid
- Configuration for state
- Global parameters (total numbers, percentage of states)
