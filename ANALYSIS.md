CompSci 308: Simulation Project Analysis
===================

> This is the link to the [assignment](http://www.cs.duke.edu/courses/compsci308/current/assign/02_simulation/):


Design Review
=======

### Overall Design

* Simulation (Cell classes, Neighbor classes):  
  This part of the project consists of an abstract cell class that can be extended for each new simualtion added. When a cell is created, it has its state information, its indexes for its location within the Cell grid, and any numerical parameters necessary to the specific simulation passed through its constructor. The Simulation class (configuration part of the project) then calls for the cell to locate its neighbors and provides the Cell grid as well as the indexes of the desired neighbors.  
  An abstract Neighbor superclass was constructed such that it can be extended for new neighbors as new shapes are added. The Cell creates an instance of the necessary Neighbor class and then uses that class to set the Cell's neighbors.  
  After the Neighbors are set, the cell waits to be called upon by the Simulation class to first set its next state, using the method that was specifically written in the concrete implementation of Cell, and then is called again by the Simulation class to update its current state to its next state.
* Configuration (Simulation, XMLParser, XMLAlert):  
  The configuration part consists of Simulation (despite the name of this class, it's still more on the configuration side), XMLParser and XMLAlert. Simulation class extends Application and is therefore the entry point of the whole program, and in the constructor it calls readConfig() to read in general configuration data, such as valid model types, associated number of states and required initialization parameters.  
  In the start() method (inherited from Application), Simulation invokes method for initializing the program's opening scene where user can select a specific simulation model type. This step would initialize an IntroScene object, which would notify Simulation once the user clicks on any simulation model's button. 
  After receiving user inputs, Simulation goes through a pipeline of initializing an XMLParser for parsing specific model's XML file, retrieving parsing results, initializing the grid of cells with certain concrete Cell class, setting up the animation timeline, and initializes the UI for displaying simulation visualization in real time.  
  The animation is accomplished by attaching a Frame to the timeline, and updating the cell states between delay intervals of the frame. The default delay value is calculated based on the minDelay and maxDelay data read from the general configuration file in the beginning, and can be adjusted based on user input. To update cell states over the whole grid, Simulation loops through each cell in the grid and calls findNextState() for the cell to determine the next state to switch to, based on the current states of its neighbors and itself; after a full iteration, the grid is looped through again, where Simulation calls switchState() on each cell to update the state. This implementation decision was made in order to preserve a cell's current-generation state for its neighbors for finding their next states.  
  After the grid has been updated, Simulation would then notify UI to update its visualization. The specific rules for determining a cell's next state and the procedures for UI to update its visualization are all internally implemented in Cell and UI classes, and the configuration part (i.e. Simulation, XMLParser, XMLAlert classes) has completely no knowledge about those details. 
  Simulation also has several methods for controlling the flow of simulation (running, pausing, speeding up/down, etc.), which are public and can be invoked by UI if any user action occurs.
* Visualization (IntroScene, UI):  
  The visualization consists of the UI and IntroScene classes, which inherit Scene. The IntroScene contains
  a splash page from which the user can select a button that brings up a specific simulation. The types of simulations that
  the user can choose from include Fire, Game of Life, Percolation, Rock Paper Scissors, Segregation, and WaTor World. Each button
  calls a method from Simulation class to configure the Cells for each specific simulation. 
  After the user selects a simulation, the UI scene is displayed in Simulation. The UI scene contains a grid of cells that can be square or triangle shaped and
  keeps track of current Cell states through a 2d array of Cells that are passed into the UI class from Simulation. The UI also
  contains information about the corresponding color of each state, passed from the Simulation class. Using this information
  UI creates a Polygon for a Cell at that location, filled with the color corresponding to its state.
  The UI class has 2 public methods, drawGraph and drawGrid that are called by the Simulation class every time the simulation updates, so the
  graph of cell states and grid of cells displayed are updated. The UI class also calls userSwitchState from the 
  Cell class so that the user can click on a cell displayed in the grid and change the cell state. 
  
#### Add new kind of simulation

* Simulation: New concrete subclass of Cell would need to be created to implement rules of new simulation type.
* Configuration: New XML file would be created with new type tag to be recognized in XML Parser and added to list of simulation
types. Additional configuration settings for number of states and parameters in the new simulation need to be written in the SimulationConfig file,
along with the model name. 
Switch case needs to be added to recognize the simulation type then instantiate the appropriate Cell subclass
that implements the rules. 
* Visualization: New button created for simulation and add simulation file to list of options in dropdown menu
used to switch simulations. 

#### Dependencies
* There are many dependencies between the parts (Simulation class, Cell class and subclasses, and UI/IntroScene)
* These dependencies are easy to find - objects/data is passed through parameters, such as through parameters in the 
constructors of the Cell class and UI class.
* Public methods are clear and purposeful - almost all public methods used in final project were
outlined and agreed upon during planning stage. Other public methods were made after new features were implemented in sprint 2.

#### Did not implement: Cell class and subclasses
* abstract Cell class is very readable - each Cell has a row and column, a current state, 
a list of neighboring Cells, parameters specific to the simulation type, and list of possible states. 
* Cell class is readable because each method performs a very defined and simple function - 
for example, the findNeighbors() method does exactly as its name implies - it finds the neighbors of the Cell
based on specific parameters, such as the grid shape, edge type, and indices of surrounding neighbor cells.
* Each subclass is also relatively readable because all methods perform actions specific to the type of simulation, 
so simpler simulation models such as Fire have a corresponding CellFire class that has very few lines. 
* The Cell subclasses are well encapsulated because they extend from Cell superclass, so as long as they still
implement the Cell methods, the rest of the program that uses Cell objects is not impacted. For example, 
each Cell must implement findNextState(), which is used in Simulation class. The implementation of
this method can vary based on the simulation type because the next state is calculated differently for each model. 
However, the Simulation class will always call the same method findNextState() to find the next state of a Cell. 
* I found that even though my teammate wrote readable code, they still included brief comments before each method, 
public and otherwise. This was not necessary but made it even easier for me to understand what was
happening in the code. 
* The total code is somewhat consistent in naming convention. However, upper case and underscores 
are used for non-final variables such as private SIM_PARAM_NUM hashmap in the Simulation class, while upper case with underscore is used for
only final variables in other classes. 
* Simulation class includes javadoc comments for private methods while UI and IntroScene classes
only contain javadoc comments for public methods. Cell classes include brief in-line comments for private methods,
javadoc comments for protected methods, and comments for all public methods.
* Simulation class and UI classes tend to have more method calls within methods than Cell classes, 
which makes debugging more complicated. However, this may be unavoidable because of the nature of these classes.

### Your Design
* The UI and IntroScene classes do not interact - each class does not call methods from the other. Rather,
both contain public methods that are called by the Simulation class. 
* The UI and IntroScene are instantiated by the Simulation class
* Simulation passes in simulation-specific data, such as state types, grid shape, and list of parameters. 
* UI class contain 2 public methods - drawGrid and drawGraph that are called by Simulation each time the entire
simulation is updated so that the graph and grid of cells displayed are updated.
* UI and IntroScene classes call public methods from the Simulation class to allow the user to interact with the 
simulation displayed. For example, UI calls methods startSimulation, playSimulation, pauseSimulation, stepSimulation, etc. 
that are triggered when the UI detects a user click on the associated button. 
* UI class also calls public method userSwitchState from Cell class, which is triggered when the user
clicks on a cell representation on the screen and tells UI which state to change the cell into.  
* UI class has access to back-end grid of cells and accesses these cells to update the appearance
of the displayed cells, changes these cells to the specified state when the user clicks on them.

#### Design Checklist
* Communication should be good - meaningful names were used, there are no magic values used at all, info 
stored instead in private final variables. Almost all methods are either private or protected except
for the 2 methods mentioned in UI class. No warnings appear in java compiler. 
*  No public instance variables were used, no public static variables (only private), all scene visualization data is 
kept within UI and IntroScene classes
    * Color of each state is passed into UI class through Simulation but this can not be avoided because
    the color is determined after parsing the XML file, which is done in Simulation class. 
* There is some duplicated in the UI class for methods that calculated the x and y coordinate values
for square or triangle shaped grids. However, I have since refactored cell shape code in UI class
to create a CellShape superclass with subclasses for specific shape implementations such as 
SquareCell and TriangleCell that override superclass methods to calculate x and y coordinates, which 
has resolved the duplicated code issue.
* Uses are general and variables are kept local when possible. 
* Methods are single purpose but when a series of actions is performed it is tedious to trace
the series of method calls, from short method to short method. 

#### Feature 1: User click cell to alter state real-time
* Implementation of this feature required calling a public method from Cell class
* Since UI class already contained map of Polygon representations of Cells and the Cells that they represent, it
was simple to loop through each Polygon on the map to add an event listener that would respond to
a user click by accessing the Cell in the same entryset as the clicked Polygon in the map
and calling the Cell method to determine and set the new state of the Cell, then updating the
Polygon representation to show the same state. 
* This feature depends on access to the 2d array grid of Cells from the Simulation class. This can not be 
avoided because the grid must be created based on the data parsed from the XML file, which is parsed in 
the Simulation class and not in the UI class. 
* This feature also depends on the userSwitchState method from Cell class but this could not be avoided
because this method depends on the specific rules of the type of simulation. The UI class 
performs the same functions regardless of simulation type, which is a benefit because there is less 
hard-coding.

#### Feature 2: Display different cell shapes
* This feature is hard-coded in the UI class - the cell shape is passed to UI and UI
contains switch cases that initialize different starting coordinate configurations based on shape. 
* Each method to find the appropriate x and y coordinates of a cell is also hardcoded based on 
shape, but this is unavoidable to some extent because shapes at least contain different numbers of coordinates - a 
triangle contains 6 while a square contains 8. These coordinates must be aligned in such a manner that they create
the appropriate shape as well.
* Even though some hard-coding was necessary, the UI treated all cases of different cell shapes as Polygons,
so regardless of cell shape case the UI would call the same methods that would fill the color
based on current cell state, display the cell, respond to clicks, etc. 
* When I refactored this code, I created an abstract CellShape class with concrete subclasses for
each shape, such as SquareCell and TriangleCell, which did not eliminate hardcoding but at least made
each switch case made in the UI class call fewer methods - most methods that set up each shape were
encapsulated within the specific shape class.
* This feature depended on the Simulation class to specify the type of cellshape that the UI 
would need to create, but this dependency could not be avoided because the cell shape is specified
in the XML file that is parsed by the Simulation class, and only after is the info passed to UI. 
these Polygons can each 
    
### Flexibilty
* This project overall is able to create new subclasses of Cell for new simulation types
relatively easily because the Cell superclass provides a template for almost all methods in 
the subclasses, and the only complexity would be determined by the logic of the particular simulation type. 
* However, adding new configuration features requires more changes in different places - 
the XML parser would need to add additional parsing for the new feature tags, add another instance variable
to store parsed data, and create methods to pass that parsed data to UI or Cell, where relevant. UI and Cell
would then need to create methods that would implement that feature.
* Adding a new shape would also require more coding in all classes - parsing would need to 
recognize the shape, pass the information to UI and Cell, which would need to create new subclasses for the 
shape. However, in UI and Cell there are templates for creating new shapes, so extending the 
abstract CellShape and Neigbors superclasses reduces amount of hardcoding. 

#### Feature 1: Support different neighbors configurations
* This code is interesting because the differing neighboring configurations is handled not only in Neighbors
classes and subclasses, but specified in the XML file. 
* For example, for square neighbors, all possible square neighbors are found (8 neighbors). However,
out of these neighbors not all are needed in all configurations, thus the neighbors that are counted in each 
simulation are specified by index number 0-7 in the XML file. 
* The Neighbors class and subclasses NeighborsSquare and NeighborsTriangle, XML parser support are needed to 
implement this feature. The UI is completely un involved. 
* The methods specific to each shape to find all possible neighbors are well encapsulated in the concrete 
subclasses of Neighbors. Although delegating index selection of specific neighbors to the XML file allows
the user more power over the specific type of configuration, this also leads to more hardcoding. 
These different configurations of shaped neighbors could instead be specified by some type of name and 
the specific Neighbors subclasses could handle the specific selection of the neighbors out of all possible 
neighbors. 
* The configuration of neighbors would be simple to change - simply change the indices chosen in the 
XML file - but this puts more responsibility on the user to specify the exact changes.

#### Feature 2: Allow simulations to be "styled"
* The code for this feature is interesting because adding styling to the simulation could require
diverse functions to be changed, in UI or in Cell or in Neighbors classes, depending on the styling. However,
this styling requires relatively little coding in the XMl file itself, only require a set of tags specifying
the new feature that can be styled. 
* Beyond the XML file, the XML file would need to parse for that tag and pass the information to the appropriate
classes that would contain methods to modify such a feature - if visual, then to UI and if related
to model logic, then to the Cell class. 
* This feature, without limiting the power or scope of the types of aspects that can be styled,
by nature may require significant coding and writing of additional methods in other classes.
However, the act of writing the styling and parsing the file for the new styled feature requires relatively little
coding.
* For example, adding the ability to style the grid shape was a matter of adding a 
tag in the XML file and then parsing for that tag. However, implementing the feature required
passing the shape information to the UI and Cell classes, leading to the creation of new classes and use of switch cases
to handle different grid shapes. This was a significant task to implement. 

### Alternate Designs
#### Changing Grid Data Structure
* All classes would be affected by changing the data structure because the project relies on a 2d array and access
and assigns elements using the indices within the 2d array. All classes contain for loops that loop through
items in the 2d array by index.
* If the data structure were changed into a Map, then the for loops that rely on index to assign/determine
location of each cell would be removed to instead iterate through all cells and retrieve/set cell row and column in order
to obtain/change location information. 

#### Handling project extensions
* The project was not able to implement some features without completely changing the project fundamentally - 
for example, the project could not run 2 simulations simultaneously because the Simulation class was responsible to 
reading and setting configurations for a simulation while also responsible for timeline/updating states. If these
two functions were kept separate, then we could instantiate different simulation configuration objects for each
new simulation and allow both to run, controlled by a separate class that managed the timeline and starts/stops/pauses.
* Implementing different grid shapes also required extensive changes in the simulation and UI components. In simulation
new class Neighbors was created to handle different ways of finding neighbors, while UI switched from using Rectangle objects
to represent cells to using Polygons, which could instantiate any shape by passing in its coordinates. 
* Since the UI implements the different cell shapes using Polygons and shows change in state by filling 
Polygon objects with different colors, the UI is not able to accomodate ImageView representations of cells
or changes in cell states without extensive modification again.
* Generally, new features required passing information from XML to the appropriate class that would implement
specific methods to create the features. This meant that as a group, we had to meet and decide on what data
would be passed through parameters to each class, how constructors may be changed, and also change access to methods/create 
new methods for other classes to use. 
* For example, the UI class required access to the list of parameters from the Simulation class in order to implement
parameter sliders that would interact dynamically with parameter values. This list was previously private and inaccessible by
the UI. 

#### Design decision 1:
* Early on, a State class was proposed, which would contain a string of the state name and an associated color
or image. Each Cell would have a State object. The state class could also contain a method that returned its next state.  
* This design idea was not implemented because the state class would be very sparse and the one method it would have
would not be practical when each state does not necessarily lead to the next - such as in Segregation. This was based
on the assumption that all states would act like states in Fire, in which burning trees became empty cells. 
* Instead, state was kept by each cell and its representation was specified in the XML file. This allowed greater flexibility
in the end because the user is able to easily specify/change the color of each state by changing a few lines in the XML file. 

#### Design decision 2: 
* Early on, there was discussion on where the user interaction methods would be implemented - methods
responsible for starting the simulation after the start button is pressed, or stepping through the simulation after the step 
button is pressed, etc.
* If these methods were implemented in the UI class, then the UI class would not need to have the Simulation
passed through its constructor in order to access Simulation class public methods that would implement these actions. Howevever,
many of these actions required interaction with the timeline and altering loops all within the Simulation class.
This means that the UI would not only need to access Simulation variables, but also create methods that could affected
Simulation class logic. 
* The methods are currently implemented in the Simulation class and they are relatively short and straightforward. However,
the drawback is that these methods do not seem to fit in with the purpose of the Simulation class, as they deal with UI and
would reasonably be expected to be found in the UI class.

### Conclusions
* The best feature of this project is the ability to create new rules for new models of simulations by creating new
subclasses of the Cell abstract class. From reading it, I learned that an abstract class could be created that contained
abstract and implemented methods such that each subclass could override the inherited method, without exception. The Cell
abstract class also contained methods that were used by other classes, but general enough that all Cells, regardless
of simulation type or rules, could be handled in the same way by the UI and Simulation classes. This meant that the UI and 
Simulation classes did not need to hardcode any rules specific to each simulation model. 
* The worst feature of this project was the handling of the different cell shapes by the UI class - there
was duplication in the code for creating square shapes and triangle shapes, and it was a large block of hard-coded methods
in the middle of a sea of button-creating methods. I created this mess because I initially could not figure out how to simultaneously
make calculations and instantiate the super in the constructor, but learned that I could simply not inherit the superclass, keep it as
an instance variable instead and be able to perform calculations anytime and then use those calculations the create
the instance variable. 
* To be a better designer in the next project, I should keep planning out all public methods that would be used by other classes.
All other methods I would make private or protected. 
* I should also continue keeping my methods short and simple by performing only a single task each. 
* I need to stop hard-coding features that should obviously belong in their own category, such as what I did with the
handling of different cell shapes. Especially when there is duplication - the code should not be ignored. When there is duplication
that means I should consider creating either a help method or contain that method as part of an inherited superclass.

