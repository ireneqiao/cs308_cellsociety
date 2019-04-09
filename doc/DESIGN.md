
# Design Goals
The purpose of this project is to implement a program that can simulate several different cell automation models based on configuration details provided in XML source files.  
The simulation models eventually implemented include: Spread of Fire, Game of Life, Percolation, Segregation, WATOR, and Rock Paper Scissors.  

* Features accomplished:  
**DETAILS**  

* Project structure:
    * Simulation (Cell classes, Neighbors, etc.):  
    **DETAILS**
    * Configuration (Simulation, XMLParser, XMLAlert):  
    **DETAILS**
    * Visualization (IntroScene, UI):  
    **DETAILS**

# Add New Features:
## Adding another simulation model:
* **Cell Class:**  
Our project has an abstract cell superclass that can be extended to create a new simulation. The superclass contains the majority of the necessary methods such that there are only three methods that have to be written. The core method is findNextState(). This is where the rules of the simulation come into play. The end result of this method is determining how the cell should respond, given its current neighbors. The myNextState variable must be assigned somewhere within this method. Additionally, the setParams() method needs to be written to assign all the parameters passed through the constructor to the appropriate variables. This allows the UI to adjust the parameters and cell regularly calls this method to ensure that they are updated. Lastly, the initializeStatesList() needs to be written. This is just creating a list of all possible states for the simulation.
* **Simulation:**   
At initialization of the application, Simulation reads in a source file SimulationConfig.txt, which provides information about the default grid size, valid simulation types and associated number of states and parameters. In order for the new simulation model to run, the name of this new model type as well as associated numbers of states and parameters shall be added to the general config file so that the parsing results of the new model's configuration will be accepted by Simulation class as a valid model.  
Additionally, the Simulation class will also need to add a case statement in the initGrid() method so that corresponding concrete cells can be initialized for the new simulation model.
## Adding a new Cell Shape
* **Neighbors:**  
There is an abstract Neighbors superclass that can be extended. Once extended, only one method needs to be written. The setIndexMap() creates a map with the agreed upon index for a current neighbor as a key. The value would then be a list with the change in row and then the change in column. For example, look at the image below. Here, for each cell neighbor, the larger number is the index, used as the key in the map, and the coordinates are the change in row and column from the original cell, stored in a list in the value of the map.
![](https://i.imgur.com/nIltupv.png)
 By creating this hashmap, the superclass then has defined methods that allow the cell simulation class to choose which neighbors are active and the cell should take into consideration.
 The new shape will also need to be added in the cell class in a switch case such that the correct neighborhood will be created.
* **Simulation & XMLParser:**  
The XMLParser reads in the cell shape by parsing text content within the tag 'CellShape' in a model's configuration file, and passes this piece of information to Simulation, who passes it down to UI and cells at their initialization call.  
Thus, in order for a new cell shape to take effect, the string specifying this shape must be provided within the 'CellShape' tag inside the source configuration file.


## Adding a New Edge Type:
* **Neighbors:**  
This is currently handled in the Neighbors superclass in the handleEdgesAndAddCoords() method. When a cell is setting its neighbors and one of its neighbors would be out of bounds, it calls this method to determine if and how that neighbor should be added. Within this method, the edge type is checked and a corresponding method is called. For example, if the edge type is toroidal, a method is called find the correct coordinates to the correct neighbor on the other side of the grid. To add a new edge type, currently it would be necessary to write a method to determine the coordinates of the correct cell and then call that method within the handleEdgesAndAddCoords() method.
* **Simulation & XMLParser:**  
The XMLParser reads in the edge type information by parsing text content within the tag 'EdgeStyle' in a model's configuration file, and passes this piece of information to Simulation, who passes it down to cells at their initialization call.  
Thus, in order for a new cell shape to take effect, the string specifying this new edge type must be provided within the 'EdgeStyle' tag inside the source configuration file. Specific rules for handling the new edge type will be defined by the concrete Cell and Neighbors classes.

# Major Design Choices
* **Running one single simulation at a time:**
    * Description  
    The Simulation class (despite its naming, this class is more of the configuration part whereas Cell classes are more of the real Simulation part) was designed to extend Application at the very beginning of this project, which simplified the program control flow by enabling Simulation to directly control the animation timeline as well as communicate with all the other classes to process file parsing, error handling, and object initialization, etc. 
    * Trade-off  
    Such a design eventually limits the program's capability of running multiple simulation models at the same time. Moreover, as operations including JavaFX timeline control, file parsing and model setting up are all implemented in the Simulation class, we were unable to draw a clear line between front end and back end in the Simulation class, which would somehow limit the program's flexibility of adapting the same backend to various front ends.

* **Exception handling:**  
    * Description  
    The XMLAlert class is specifically created for popping alert dialogue boxes when any Java exception occurs, or when encountering missing/invalid data issues in parsing a model configuration XML file. There are several XMLAlerts initialized at the initialization of XMLParser, where the error messages are read in from a source file and sequentially passed into the constructor of XMLAlert.
    * Trade-off  
    While the XMLAlerts are created and stored in the XMLParser class, after the parsing results are transferred to Simulation, the latter will still have to validate the parsed data against what has been defined in the general config file, and will also need to access and pop up XMLAlert's dialogue boxes if any invalidity occurs. For this purpose, the XMLAlerts are declared as package-private variables in order to allow Simulation to access them.  
    With such a design, the program can function normally and is able to detect and handle the exception/error cases by showing corresponding XMLAlert diapogue boxes. Despite the effectiveness, the code structure appears a bit messy and still has space for improvements (eventually this part of code was refactored during the refactoring stage).

* **Abstract Superclass for Cell:**  
A lot of the methods for the cell class are the same, such as: findNeighbors(), updateState(), userSwitchState(), and several others. By creating an abstract superclass, these methods are shared with all its children and it avoids duplication, it's easy to extend and add new simulations, and it separates the code in a manner that is readable.

* **Abstract Superclass for Neighbor:**  
This was a harder decison. The benefits were, that regardless of the shape, there are several methods that would be the same. However, in each concrete implementation, there is only one method that is actually written. This made it hard to justify the necessity for a new class for each shape. However, no other option that wouldn't involve large amounts of duplicate code or adding the methods directly to the cell class were thought of, and so an abstract superclass was made. This worked well and makes it easy to add new shapes in terms of locating a cell's neighbors.


# Assumptions or Decision
* **Burning Simulation:** a cell will check if it should catch on fire once for every burning neighbor it has. This means that more burning neighbors increases the chance of a tree catching. This was decided because in a real forest fire situation, more fire nearby would also increase the likelihood of catching.
* **WATOR Simulation:** It was assumed that each cell could only house a maximum of one animal per step. This mimics reality, as two things cannot exist in the same space. Additionally, had more than one animal been allowed, it would become exceedingly difficult to keep track of all who was housed in a cell and how to handle the movements of each inhabitant. Thus, for simplicity and attempting to remain realistic, cells can only host one animal.
* **Segregation:** The rules for the movement of a cell, when unsatisfied, were left rather vague. As a result, it was decided that when a cell became dissatisfied, it would scroll from its starting location down each row until it either found an empty cell or looped all the way to its starting coordinates. If it made it back to where it began, the cell would not move, as there were no empty spaces that hadn't been claimed by other cells. This was a fairly easy way to ensure that the cell would only check each other cell once, as opposed to being completely random. While this does result in the first few steps displaying extremely large groups moving to the bottom and then the top of the grid, it quickly disperses and still results in a successful simulation.
* **Rocks Paper Scissors:** If, when a cell is checked to set its next state, the next state has already been set, it is assumed that it was "eaten" by a neighbor and it is not able to affect its neighbors in this step. Also, white is considered to be able to be "eaten" by any other cell, whether white tries to "eat" another color or is attempted to be "eaten" by another color, it always loses. Lastly, the "gradient" component of the simulation was not added due to time constraints, however it would be easy to add in the future. Within the CellRPS class a gradient variable would need to be created and incremented and decremented as appropriate, and then the XML file would need to add a parameter and the UI would need to add a parameter slider. All of these are extremely doable with our current structure.

