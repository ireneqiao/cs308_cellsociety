cell society
====

This project implements a cellular automata simulator.

Names: Hsingchih Tang, Irene Qiao, Carrie Hunner

### Timeline

Start Date: Jan.24, 2018

Finish Date: Feb.11, 2018

Hours Spent:
* First meeting (in class) ~ 2 hrs  
* Planning meeting ~ 3 hours  
* Putting it together the first time meeting ~3 hrs  
* Putting it together this past sunday ~3 hrs  
* Outside of meetings:  
    * Carrie: 28 hours  
    * Hsingchih: 35 hours  
    * Irene: 25 hours

### Primary Roles
**Hsingchih Tang - Configuration:** Created the Simulation, XMLParser and 
XMLAlert classes. Implemented configuration file parsing, simulation 
flow control, cell initialization and switching between scenes.

**Irene Qiao - Visualization:** Created IntroScene and UI classes, buttons to select simulation type/switch simulations, 
sliders to adjust simulation speed and parameters, interactive buttons to step, stop, start,
or reset the simulation, graph of the percentage of cells in each state, allowed users to click on cells in the grid 
to change their state, created a grid display for square and triangle shaped cells.

**Carrie Hunner - Simulation:** Created abstract cell class, 
 the concrete implementations for each simulation, 
 as well as the neighbor abstract class and its concrete implementations.

### Resources Used
[stack overflow](https://stackoverflow.com/)

[segregation rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/nifty/mccown-schelling-model-segregation/)  
[Wa-Tor rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/nifty/scott-wator-world/)  
[spreading fire rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/nifty/shiflet-fire/)  
[percolation rules](https://www2.cs.duke.edu/courses/spring19/compsci308/assign/02_cellsociety/PercolationCA.pdf)  
[game of life rules](https://en.wikipedia.org/wiki/Conway's_Game_of_Life)  
[rocks paper scissors rules](https://www.gamedev.net/blogs/entry/2249737-another-cellular-automaton-video/)  

[Javafx documentation](https://docs.oracle.com/javase/8/javafx/api/toc.htm)
### Running the Program

Main class: Simulation.java

Data files needed:
* English.properties
* SimulationConfig.txt - setting up default parameters 
and defining simulation models.
* XMLAlertText.txt - providing content of alert messages 
to display when encountering mal-formatted xml files.
* Specific model configuration files - providing detailed information 
of cell states and parameters for initializing a simulation process.
    * Fire.xml
    * Game of Life.xml
    * Percolation.xml
    * RPS.xml
    * Segregation.xml
    * WaTor.xml


# Features implemented:
### Simulation
* **Allowing for a different number of neighbor rearrangements:**  
The XML file can contain a list of integers that act
as indices to indicate which possible neighbors for a cell
should be included. The list of integers is stored in the 
Simulation class after parsing the xml file, and is passed into 
the cell's findNeighbors() method for generating neighbors.
* **Allowing for a different variety of grid location shapes:**  
We currently have functionality to support either square or
triangle cells. The Neighbors superclass is easy to extend
and add other subclasses to accommodate more shapes in the future.
* **Allowing for different grid edge types:**  
Our program currently allows either finite or toroidal edge types.
Adding another new edge type would also be doable, needing only
a new method within the abstract Neighbors class to find the
grid coordinates of all possible neighbors when an edge is reached.
* **Implement additional simulations:**  
In addition to the five simulations (Fire, Game of Life, Percolation, 
Segregation and WaTor) implemented in the first sprint, we further 
implemented a new RPS simulation. 

### Configuration
* **Implement error checking for incorrect file data:**
    * Pop up alert dialogue boxes when mal-formatted xml configuration
     files are loaded (e.g. missing configuration information, out-of-bound 
     cell index given, invalid model type, etc.). Alert messages for different 
     mal-formatting issues differ and are loaded from XMLAlertText.txt file at 
     the initialization of the XMLParser.
    * Handle FileNotFoundException, SAXException and ParserConfigurationException
    by throwing the exception to the next-level method, printing error message to 
    console, and eventually terminating the whole program by calling Platform.exit().
    * Set default cell shape to Square and edge type to Toroidal when the values are 
    not specified in a xml configuration file.
* **Different simulation initial configuration style:**
    * The initial states of cells are assigned completely randomly if no information 
    is specified about each state's distribution percentage or each cell's specific 
    initial state.
    * Cell initial states are assigned based on distribution (accuracy to 0.01) when 
    the percentage distribution of each state is specified.
    * If all above information is missing, and that the file explicitly specifies the 
    initial state of each cell by row and column indices, the XMLParser will read in 
    the data, which will be passed to the Simulation class, where initial state of each 
    cell is assigned based on location indices.
    

### Visualization
* **Display a graph of the populations of all of the "kinds" of cells over the time of the simulation:**
    * UI scene displays a lineChart that contains multiple series that each correspond to one 
    of the cell states in the specific simulation type
    * Since the number of states varies from simulation to simulation, the number of series
    graphed on the lineChart also varies depending on simulation type.
    * The percentage of cells that are in each state is graphed on the y-axis, while the 
    number of steps passed in the simulation is graphed on the x-axis
    * The graph also contains a key with the name of each state corresponding to its series
* **Allow users to interact with the simulation dynamically to change the values of its parameters**
    * For each parameter, there is a slider on the UI scene that the user can adjust to change the values
    of the parameter, which will immediately affect the simulation
    * The number of sliders available will depend on the number of parameters for the specific type 
    of simulation
    * The values for the parameters range from 0 to 1
* **Allow users to interact with the simulation dynamically to create or change a state at a grid location**
    * Users can click on any cell in the grid and immediately see the cell change state
    * The visual change in state of the cell also corresponds to the change in cell behavior 
    appropriate to its new state
    * The state to which the cell changes is determined by the specific cell class implementation for the
    simulation type


## Assumptions or Simplifications:
* **Burning Simulation:** a cell will check if it should catch on fire
once for every burning neighbor it has. This means that more burning neighbors
increases the chance of a tree catching. This was decided because
in a real forest fire situation, more fire nearby would also increase
the likelihood of catching.
* **WATOR Simulation:** It was assumed that each cell could only
house a maximum of one animal per step. This mimics reality, as
two things cannot exist in the same space. Additionally, had more than one
animal been allowed, it would become exceedingly difficult to
keep track of all who was housed in a cell and how to handle
the movements of each inhabitant. Thus, for simplicity and attempting
to remain realistic, cells can only host one animal.
* **Segregation:** The rules for the movement of a cell, when unsatisfied,
were left rather vague. As a result, it was decided that when a cell
became dissatisfied, it would scroll from its starting location
down each row until it either found an empty cell or looped all the
way to its starting coordinates. If it made it back to where it began,
the cell would not move, as there were no empty spaces that hadn't been
claimed by other cells. This was a fairly easy way to ensure that
the cell would only check each other cell once, as opposed to being completely
random. While this does result in the first few steps displaying
extremely large groups moving to the bottom and then the top of the grid,
it quickly disperses and still results in a successful simulation.
* **Rocks Paper Scissors:** If, when a cell is checked to set its next
state, the next state has already been set, it is assumed that
it was "eaten" by a neighbor and it is not able to affect its neighbors
in this step. Also, white is considered to be able to be "eaten" by any other cell,
whether white tries to "eat" another color or is attempted to be "eaten"
by another color, it always loses. Lastly, the "gradient" component
of the simulation was not added due to time constraints, however
it would be easy to add in the future. Within the CellRPS class
a gradient variable would need to be created and incremented and decremented
as appropriate, and then the XML file would need to add a parameter 
and the UI would need to add a parameter slider. All of these are
extremely doable with our current structure.

## Known Bugs:
* **Reading user-uploaded file:**   
    The IntroScene displays a button where user can click and select his/her 
    own configuration xml file for any of the simulation types that we've 
    implemented (Fire, Game of Life, Percolation, Rock Paper Scissors, 
    Segregation, WaTor). However, we still encounter some error when trying 
    to load the file, so this feature still has some space for improvements.


## Extra credit:
* **Setting up Simulation and XMLParser error messages without hard-coding:**  
    Messages/information that are displayed to users directly are set up in the 
    Simulation class and XMLAlerts by reading from source files. The data being 
    read in include: error messages to be displayed in pop-up Alert dialogue 
    boxes if the XML simulation model configuration file is of invalid format or 
    has information missing, implemented model names and associated numbers of 
    states as wel as parameters, etc. With such an implementation, developers (we) 
    can flexibly modify the whole program's configuration by editing the relevant 
    source files without touching source codes.
    
* **Real-time adjustment on simulation speed:**  
    At the end of the first sprint, we used buttons for the user to speed up or 
    slow down the simulation flow, which internally decrease/increase the delay 
    time between animation frames by a factor of 2. In the second sprint, we 
    managed to implement the feature of controlling simulation speed using 
    a sliding bar, where the delay time is adjusted proportionally between the 
    default maximum and minimum delay that were read form the program's 
    configuration file.

## Notes
**Wator Simulation:** A shark is checked for death at the beginning
of a step, before attempting to move or eat.
 This can result in a shark dying with a fish next to it
if its energy is too low. This may look like a glitch, or sometimes
like a fish is eating a shark, but it is not.

## Impressions
**Carrie:** Compared to Breakout, far more planning happened initially
and resulted in less adjustments to the structure of the program mid-
coding. This was essential, as each of our respective components
had dependencies on each other. Making decisions ahead of time about
certain public methods as well as class constructors enabled us each
to work when we had time. Then we were able to integrate everything
in a reasonable amount of time. Through this project, I got a better
sense of what sort of things need to be planned ahead of time when
working in a group, as well as grew far more adept at debugging and 
locating the source of errors through print statements.

**Hsing:** This project is much larger in terms of scale compared to Breakout, 
which required greater efforts spent in planning out the project structure 
and figuring out how different classes would interact with each other 
appropriately. In additional, as this project was completed in team, we also 
had the great chance of practicing team-work skills such as negotiating on 
disagreements, synchronizing our paces and collaborating to resolve bugs or 
to realize features. On the other hand, when working on the configuration parts 
I also spent many efforts figuring out how to handle exception cases without 
hard-coding, which was a brand new experience for me. And with the help of 
the online design checklist tool, we were able to keep track of issues in 
our code structures and could make improvements accordingly, which was extremely 
helpful for us learning about project design. Many thanks to people who 
developed this awesome tool!

**Irene:** Most of the visualization work was pretty straightforward. However, I still spent
a lot of time on the UI and IntroScene classes because I was not familiar enough with 
javafx and spent a lot of time reading documentation and debugging. My part of the project
was also very dependent on both the Simulation and Cell classes, so I had to communicate a
lot with my teammates to figure out what data needed to be passed, which methods to be called,
etc. The visualization work was not algorithmically intensive. Overall, this project helped me
understand the importance of encapsulation - I could easily see how the alteration of certain variables
could affect the work of all team members if not properly encapsulated. It was also interesting
to have to call methods that I did not write myself - especially when these methods had not yet
been created. This taught me the importance of planning - otherwise, I would not have been able to 
know which methods would become available to use or how they would have worked. I enjoyed this project - 
especially because the visual component is so cool to look at when the grid size is large.  