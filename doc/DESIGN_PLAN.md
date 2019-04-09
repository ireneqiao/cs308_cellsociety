# Design Plan
Hsingchih Tang (ht114), Irene Qiao (isq), Carrie Hunner (clh87)

## Introduction
* The problem to solve
    * We want to create a flexible design of Cellular Automata that is animated and can be easily applied to a wide-range of potential simulation scenarios.
* The primary design goals
    * We want to have a flexible cell Class that can be implemented for all cell types in different simulations and a flexible user interface class that can be extended to implement various interfaces with different graphics or different types of interaction. 
* The primary architecture of the design
    * The abstract Cell class will be closed
        * Each concrete implementation of the Cell class will be closed
    * The abstract UI class will be closed
    * The simulation class will likely be closed as well.

## Overview

![Project Structure](https://i.imgur.com/5W09ELW.png)



* **Cell Class:** This class will be an object that holds all the properties of the cell. Each instance of cell class will be in charge of its location, neighbors, current state, next state, and the rules that would cause it to change states. Its state will also be associated with String that corresponds to an image file. This filename can then be called by the UI Class for front-end purposes. Its concrete subclasses will define the rules for that particular simulation. 
    * *Methods:*
        * findNextState(): this method will check the rules of the cell and then determine what the next state should be, but it will not actually update the state
        * updateState(): this method will switch the state of the cell to the next state, which was determined by the method findNextState()
        * findNeighbors(Cell[][]): this method will take in the grid of cells and determine which are its neighbors. It will then save these to later be used when determining its state.
        * getState(): this method will return the cell's current state, a String variable that will also correspond to an image filename, to the caller -- usually the neighboring cells in their findNextState() processes. It will also be called by the UI so the image displayed can be updated as needed. 
* **Simulation Class:** This is where the main application will be run by extending the application class. It will begin by creating a scene that allows the user to select the desired simulation. From there, it will read the XML file, initialize all the cells and the UI, update all the cells, and step through the animation.
    * *Methods:* 
        * start(): this will initialize and display the scene that allows the user to determine which simulation to run. Once the decision has been made, it will have the corresponding XML file read, create the desired cells from the file, and initialize and set the simulation scene.
        * initIntroScene(Group root): this method sets up the opening screen of the program, where user can click on buttons to select type of simulation to run. The user decision will then trigger the call readXML(File f) on certain XML file to load the simulation configuration. This will likely be called from start().
        * readXML(File f): this method parses the XML file to obtain configuration details of the simulation, such as grid width/height, types of stages for the simulation, each stage's visual representation and initial percentage distribution, etc. This will likely be called from initIntroScene() when a button is pressed.
        * initGrid(): this method is responsible for initializing a new grid of Cells after reading simulation configuration details from the XML file. New Cell objects in the cooresponding concrete subclass extending the abstract Cell class will be created and stored in a 2D array.
        * initUIScene(Group root): This method initializes the UI object for visually displaying the Cells in the grid and interacting with user.
        * updateGrid(): this method loops through all cells in the grid and calls findNextState() on each cell to apply updating rules and set the cell's nextState variable. Then it loops through all cells again and sets each cell's currentState variable with a updateState() call. The updating process is intentionally divided into two steps, so that a cell's current state can be preserved until all its neighboring cells have found their next states.
        * step(double delay): this is inherited from the Application class and will cause the cells to update when appropriate. It takes in a delay argument to control the speed of simulation, and continuously call updataGrid() to proceed the simulation when no flag for pausing/stepping simulation is raised.
        * pauseSimulation(): it will change the pause variable (which is boolean type) to raise the flag for pausing simulation. It causes the cells to stop updating until told to resume again. This will likely be called from the UI class when a button is pushed.
        * resumSimulation(): it will change the pause variable (which is boolean type) to raise the flag for continuing simulation. It causes the cells to begin updating again. This will also likely be called from the UI class when a button is pushed.
        * stepSimulation(): it will let the grid update for one step, and then change the pause variable (which is boolean type) to raise the flag for pausing simulation. This will cause the cells to go through one full generation of checking their next state and switching to that state. This will likely be called from the UI class when a button is pushed.
        * switchSimulation(): it will reset the simulation by reading in the new simulation's XML file and reinitializing the Cells and UI object. This will likely be called from the UI class when a button is pushed.
        * slowdown(): it will increase the delay time between simulation steps. 
        * speedup(): it will decrease the delay time between simulation steps.


* **Abstract UI Class:** This class will extend the Scene class and will be in charge of creating the visual simulation seen by the user as well as registering user input through keys/mouse. It will have concrete subclasses to allow for different UI setups and features.
    * *Methods:*
        * handleUserInput(): this will check for user input and will call methods to respond accordingly. Ex. If the user presses the speed up button, this method will call the speedup() method in the simulation class.
        * reportError(): this method will display errors to the user. For example, if a bad file is passed through this method would alert the user.

## User Interface

* The grid will be displayed in the top left corner of the window and be of set size regardless of the number of cells in the grid.
* Interactive components will appear as "buttons" that can be clicked to perform action such as restart the simulation, start, stop, or "step" to the next generation of states in the simulation. 
* "New simulation" option allows user to return to splash screen to choose the type of simulation that the user wans to run and contains option that prompts user for XML file
* There will be a section dedicated to displaying error messages.

![](https://i.imgur.com/ClTMpSf.png)

* Finally, it should describe any erroneous situations that are reported to the user (i.e., bad input data, empty data, etc.). 
    * Report bad input data
    * Report empty data

### Intro Scene
![](https://i.imgur.com/BXPKPS2.png)

The intro scene is the first scene that the user will see. They will be able to click on a button that will bring them to the specified simulation, or they can choose to upload an XML file that will specify the type of simulation that the user wants to run. 

## Design Details
### Components
#### Cell:
* The abstract Cell class can be extended to concrete subclasses that would conform to the rules of specific simulations.
* The classes extending the Cell superclass are instantiated in the Simulation class and stored in a 2D collection
* The Simulation class calls findNeighbors on each instantiated Cell to add Cells to each Cell's collection of neighbors
* The collection of Cells is passed to the UI class, which can then call getState() on each cell. This will return a string corresponding to an image representative of that state, which the UI can then add to the scene or update if already there.
* The findNeighbors method can be implemented differently depending on whether each cell requires 4 neighbors or 8
* The rules for each type of simulation that determine state changes in cells can be implemented in the findNextState method.
* The Cell class serves to provide a template for any type of Cell, regardless of the type of simulation. This allows flexibility in creating Cells and also allows Cell behavior in different implementations to change without needing to change anything in the Simulation class or UI class. 

#### Simulation:
* Contains methods that perform UI functions such as nextStep, stop, pause, start, restart, etc.
* Collaborates with UI class such that the UI class handles user input that calls the appropriate methods from the Simulation class
* Parses the XML file and use information to instantiate the appropriate implementations of the Cell class, specific to the simulation type specified in the XML file
* Instantiates the appropriate UI implementation
* Instantiates Cell objects according to simulation type and adds each to a collection of Cells
* Runs steps of simulation - calls on each cell to findNextState and updateState
* Can adjust to additional requirements by instantiating the appropriate Cell or UI classes - the Simulation class itself is not extensible 
* This component serves to use the other components of the project in order to present a working simulation with a visual and interactive component.

#### UI
* The UI class receives the grid of Cells from the Simulation class in order to add each Cell's image file that corresponds to its state to the scene's root
* The UI class handles user input events but needs to call methods from the Simulation class in order to perform the user-specified directives (start, pause, restart simulation, etc.)
* The UI class can be extended to create a variety of different interactive and graphical interfaces to present the simulation
* This component was created so that the presentation of the simulation and the interactive components can be kept separate from the back-end running of the Simulation. 
* The UI class abstract and extensible so that a change in graphical presentation of the simulation would not necessitate any coded changes in the Simulation or Cell classes and could likely be achieved by extending the UI Class.
### Use cases
* Apply the rules to a middle cell: set the next state of a cell to dead by counting its number of neighbors using the Game of Life rules for a cell in the middle (i.e., with all its neighbors)
    1) After the Simulation class reads in CA configuration details (the grid's width and height, type of simulation to run, etc.) from the XML file, the *initGrid* method is called for initializing the cells using concrete subclasses of Cell -- such as FireCell for simulating spreading of fire -- and storing them in a 2D array. Every Cell object holds a collection of Cells that are its neighbors, and after the whole 2D array has been initialized, the Cells looped through again for finding each Cell's neighbors.
    2) The abstract method *findNeighbors* belongs to the Cell class, and takes the whole grid (a 2D Cell array) as input parameter. With the grid and its own x, y coordinates (previously passed in via constructor when a Cell is initialized), a Cell will be able to identify the x, y coordinates of its neighboring Cells in the grid, retrieve them from the passed-in grid, and add them to *neighbors*, a Collection of Cells held by the Cell class.
    3) Updating the Cell's state is accomplished with two methods in the Cell class: *findNextState* and *updateState*. When *findNextState* is called, a Cell in the middle "with full neighbors" simply needs to loop through all neighboring Cells in the collection and count the total number of living neighbors by calling getState() on each neighbor, and can determine its next state according to the updating rules. Either a string or an int indicating the next state will be stored in the Cell's nextState variable, and when *updateState* is called, the Cell will update its currState variable to what's stored in nextState.
* Apply the rules to an edge cell: set the next state of a cell to live by counting its number of neighbors using the Game of Life rules for a cell on the edge (i.e., with some of its neighbors missing)
    1) As explained above, at initialization of the grid, each Cell establishes its own collection of neighboring Cells as an instance variable of the Cell class. Knowing the grid's height and width, as well as its own x/y coordinates, a Cell can easily identify its position in the grid and find its neighbors accordingly.
    2) Again, with *findNextState* defined in the concrete Game-Of-Life Cell class, the Cell can check the current states of its neighbors and apply the rules to figure out its next state. And the *updateState* call will enable the Cell to switch to its next State.

* Move to the next generation: update all cells in a simulation from their current state to their next state and display the result graphically
    1) The UI Class will extend Scene, and all of the cells will be added as children to that root. 
    2) The Simulation class will call *updateGrid()* when the cells need to be updated. When this is called, the Simulation class will cycle through the entire Cell[][] grid and call *findNextState()* on each Cell. This will have the cells determine, based on the rules set, what their next state will be. Each cell will save this, but not switch states. 
    3) Once the entire grid has been run through, the Simulation class will run through it again and call *updateState()* on every Cell. This will have the Cells actually switch to their next state, which will include adjusting their String variable that stores an image filename. This can then be accessed by the UI Class by calling *getState()*. The UI can then either add or update the image on the scene.

* Set a simulation parameter: set the value of a parameter, probCatch, for a simulation, Fire, based on the value given in an XML fire
    1. This can be accomplished when the Cell Class is extended. In the new subclass, we could override the constructor from the Abstract Class and add a new parameter to be passed through. This could be the probability of catching fire. When this is passed through, it can be assigned to an instance variable to hold that value. This can then later be used in the method that determines the rules of the Cell.
* Switch simulations: use the GUI to change the current simulation from Game of Life to Wator
    1. UI object will process user click on "New Simulation" button and call a method from the Simulation class.
    2. The method from the Simulation class sets the introScene as the stage, which displays the types of simulations available to run to the user, as well as the option to submit an XML file to change the simulation. pauseSimulation will also be called to pause the current simulation. 
    3. To change to Wator, the user can click on the "Wator" simulation button. This click will be processed in the Simulation class. It will call readXML() in the Simulation class that retrieves and reads from the Wator XML file from the project resource folder. Then, the appropriate Wator Cell and UI classes will be instantiated in the Simulation class. All instance variables in Simulation will be reassigned. The grid will be reassigned with initGrid() from Simulation class. 
    4. The user can also choose to submit their own Wator XML file by clicking on the appropriate button and submitting the file as prompted. 

## Design Considerations

* Design decisions
    * Handling User Input: Determining if the user input should be handled in the UI class or in the Simulation class was a big decision. By handling the user input in the UI class, the input handling method needs to call on public methods that belong to the Simulation class, which requires access to a Simulation object. The methods that carry out possible user interaction components--such as start, stop, step, or reset simulation--are kept in the Simulation class because the implementation of these methods is closely related to the implementation of the simulation itself. However, the user input is part of the UI class so that the possible visual component with which the user interacts could be easily changed with a different UI implementation without affecting the Simulation class.
    * We also debated between creating a Cell interface or abstract class. As an abstract class, Cell could implement methods, which could then be inherited and save time when extending the class. However, we are uncertain what type of Node the Cell would be required to be. Since a class could only extend one superclass, if a Cell were to extend Imageview or Rectangle, then it can only incorporate a Cell interface. We decided that the Cell will maintain a string that corresponds to an image that is associated with its current state. The cell can then return this string when called, and the UI can access the file and add/update the image to the scene.

* Assumptions or dependencies regarding the program that impact the overall design.
    * Simulation class depends on the UI class to handle user action events (button clicking, keyboard inputs, etc). The UI class processes a user input with the method *handleUserInputEvent*, in which it calls corresponding public method in the Simulation class, such as *pauseSimulation*, to respond to user requests.
    * On the other hand, UI class also depends on the Simulation class at its initialization. The constructor of UI is expected to take in configuration parameters about the CA, such as grid width and height, in order to set up the graphical scene for front-end display.
    * Initialization of Cells also depends on the Simulation class, as the constructor of Cell would require parameters indicating a Cell's location in the grid, which should be passed in when a Cell object is created in the Simulation class's *initGrid* method; the specific kind of Cell's concrete subclass is also determined based on contents of the XML file, which is ingested by the Simulation class.

## Team Responsibilities
*    Carrie: Creating the Cell superclass and implementing 3-4 concrete cell classes with the rules
*    Hsing: Developing the Simulation class: parsing XML file and controlling the flow of simulation 
*    Irene: UI class, setting up scene in Simulation, and 1-2 implementations of Cell
