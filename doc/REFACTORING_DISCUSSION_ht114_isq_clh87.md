Code Review and Refactoring
==
###### ht114, clh87, isq

## Duplication Refactoring
**XMLAlert:** Duplicate codes occurred as XMLAlert was designed as enum with hard-coded error messages for different types of XMLAlerts that could possibly come up in the process of parsing an XML configuration file.

Solution:
Instead of using enum to define XMLAlert, we'll read in the error messages from a source file and store those in the Simulation class or XMLParser class. Any caller will be able to pass in the appropriate error message into the constructor of XMLAlert based on the specific error case, which would resolve the code duplication.



## Checklist Refactoring

### Communication
* **Simulation:** Use of hard-coded magic numbers & public static final variables: will replace these hard-coded values by reading in from source file


### Flexibility
* **Simulation** and **XMLParser:** parseCell() and initGrid() methods too complex. Will separate the methods into smaller single-purpose pieces and execute as a pipeline instead of doing everything all in one method.
* **Simulation:** make method declaration as general as possible (i.e. return List instead of ArrayList, etc). Problem fixed.

### Modularity
* private variable is only used as local variable in methods so should become local variable only
    * myFilePath
    * myAlert
    * myUIRoot
    * myIntroRoot
    * myParser
    * myDoc
    * myIntroScene
* **Simulation:** Change instance variables only used in local methods to local variables. Problem fixed.
* **Simulation:** Change unneccessary static variables to protected variables. Problem fixed.
* mutable fields should not be public static:
    * SIM_TYPE_LIST
    * SIM_PARAM_NUM
    * SIM_STATE_NUM
* private Timeline myTimeline should have constructor or have default values

## General Refactoring

### Java notes
* Using Collections.isEmpty() to check for emptiness
* Mutable members should not be stored or returned directly
    * myGrid of Cell objects is returned in get method to UI class
    * Refactor: change 2D array myGrid to nested ArrayList so that myGrid can be returned as an unmodifiable object using Collections.unmodifiableList
    * Alternatives considered:
    * parameters list is also returned in modifiable form, can modify method to return parameters in unmodifiable form
* XMLException should be a static class since it does not reference its owning class
* **XMLParser:** getRootElement() not passing caught Exceptions (SAXException, IOException, etc) down to caller. Will fix by creating a new ParsingException class extending RuntimeException and pass the original exception to caller (initGrid() in **Simulation**) for exception handling (Popping up alert dialogue box, etc).
* Iterate entrySet() instead of keySet() when both key and value are used in method

### Code smells
* Should not use more than 3 levels of nesting - found in Cell class, for loops and if statements
* Refactor: pull out if/else statements to create private helper methods used in original method

## Longest Method Refactoring

* CellWATOR: has an extremely long method for setting the next state, because it has so many rules. This method has a switch case to determine its current state and then handles the determining of the next state accordingly. This code can be refactored to have a singular method called within each case, thus breaking it into several smaller methods. This was done because it will result in code that is more readable and broken down.

* CellSegregation: Also has an extremely long setNextState method that can be broken into smaller methods to make it smaller. This will also make it more readable and broken up.