Inheritance Review - Visualization
===================

# Part 1

1. We are encapsulating the simulation updates from the visualization implementation.
   The representation of the Cells is mostly determined in visualization (such as the shape),
   but the Color is part of the Cell's state. Everything pertaining to layout in the 
   GUI is encapsulated from everything, as is handling input.
   
2. For my (Anna) will be an inheritance hierarchies of DefaultControls and SpecificControls
   for controls that are specific to a particular simulation. The GUI class (Irene) will be abstract
   and the formatting would be different entirely (default would have grid, start, 
   stop, etc.). (Harry) Grids of different shapes will also require another hierarchy for display
   
3. We all generally agree that changes strictly relating to aesthetics will
    be completely closed to the simulations (default controls, shape of cells, etc.)
    It should be open to adding a new simulation that requires different parameters
    
4. If there was an input parameter that did not make sense, such as inputting a negative
    number for the number of rows, it should notify the user that this is not possible.
    We are considering adding an Error Class and using the Alert object in JavaFX
    to notify the user of the issue.
    
5. It's closed to other changes in other parts of the project but still open to adding
    new simulations. The functions are all cohesive and focused on visualization.



