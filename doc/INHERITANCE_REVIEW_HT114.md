Part 1
===
1. What is an implementation decision that your design is encapsulating (i.e., hiding) for other areas of the program?
    The simulation runs by updating states of Cells to the next generation in each run of the step() method, which is repeatedly called with some time interval between each call. Pausing/Resuming simulation is accomplished by setting a boolean flag to enable/disable the call to step(), and speeding up/slowing down is accomplished by modifying the time interval parameter.
    The XMLParser class is independ from Simulation. The file parsing process will be initiated by a call from Simulation, and XMLParser is responsible for collecting all necessary configuration information.
    
2. What inheritance hierarchies are you intending to build within your area and what behavior are they based around?
    The Simulation class will extend the Application abstract class. This would allow Simulation to be the whole project's entry point by running the start() method when the program is launched and to switch the stage between different scenes.
    
3. What parts within your area are you trying to make closed and what parts open to take advantage of this polymorphism you are creating?
    Simulation class is expected to be as closed as possible so that the project's core won't change too much if extra changes are added later. XMLParser class will be open, and if new features are added to the project, they'll be reflected by changes in the parsing implementation.
    
    
4. What exceptions (error cases) might occur in your area and how will you handle them (or not, by throwing)?
    Simulation initiates the grid using configuration details read from the XML file. Missing of some pieces of information (cell states, visualizaiton image pattern, etc.) might lead to error cases when the grid is being initialized.
    Will probably deal with the issue with assertion statements and warning messages. 
    
5. Why do you think your design is good (also define what your measure of good is)?
    The XMLParser class is separated from Simulation class to make the closed/open implementation clear; the project will be flexible for adding on features while maintaining the skeleton basically unchanged.
    

Part 2
===
1. How is your area linked to/dependent on other areas of the project?
    Simulation knows that some user event occurs by communicating with the UI class, which has EventHandlers to deal with user requests of manipulating the simulation (pausing, resuming, adjusting speed, changing state distribution, etc.).
    
2. Are these dependencies based on the other class's behavior or implementation?
    Yes. When a user event occurs, UI calls the corresponding public method in Simulation class for handling the event.
    
3. How can you minimize these dependencies?
    The Simulation object will be passed into the constructor of UI when the UI is initialized, and UI will be able access any public method or public instance variables in Simulation.
    
4. Go over one pair of super/sub classes in detail to see if there is room for improvement.
    Superclass: Application
    Subclass: Simulation
    Application class provides the entry point for the JavaFX application that we are building. The main method calls launch(), init() and start() when the program is launched, and stop() when the program is terminated.
    On the other hand, Simulation class inherits the methods above, and has other methods (such as initGrid(), updateGrid(), etc.) on its own methods for configuring and running the project.
     
5. Focus on what things they have in common (these go in the superclass) and what about them varies (these go in the subclass).
    There is only one subclass in the implementation of Simulation. As mentioned above, the common methods include launch(), init(), start(), stop(), while project-specific methods such as initiating and updating the grid go into the subclass.

Part 3
===
1. Come up with at least five use cases for your part (most likely these will be useful for both teams).
    * Updating a cell's state: loop through all Cells in the grid and call findNextState() and updateState() on each Cell.
    * Restarting simulation
    * Stepping simulation (update Cell's state to the next generation and stop there)
    * Pausing and then resuming simulation
    * Exiting current simulation and return to home screen

3. What feature/design problem are you most excited to work on?
    * To design the XML file's format and implement the XMLParser class for parsing the file. Implementing communication between XMLParser and Simulation regarding data read from XML files.
    
5. What feature/design problem are you most worried about working on?
    * Responding to user events by communicatig with UI class -- specifically, handling user requests of switching simulation: how to implement methods for reinitializing the grid with the right new concrete Cell class?
    