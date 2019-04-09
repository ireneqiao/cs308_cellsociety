package CellSociety;

import java.util.ArrayList;


public class CellGameOfLife extends Cell {

    private final String DEAD = "Dead";
    private final String ALIVE = "Alive";

    private int numNeighborsAlive;

    /**
     * @author Carrie Hunner (clh87)
     *
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters empty ArrayList that is not needed for this concrete implementation
     */
    CellGameOfLife(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
    }

    //adds all possible states to instance variable
    @Override
    protected void initializeStatesList() {
        myStates.add(DEAD);
        myStates.add(ALIVE);
    }


    /**
     * Calls the cell to look at its neighbors' states and determine its next state according to that and
     * its rules.
     */
    @Override
    public void findNextState() {
        findNumNeighborsAlive();
        for(String s : myStates){
            switch(s){
                case DEAD:
                    if(numNeighborsAlive == 3){
                        myNextState = ALIVE;
                    }
                case ALIVE:
                    if(numNeighborsAlive < 2){
                        myNextState = DEAD;
                    }
                    else if(numNeighborsAlive <= 3){
                        myNextState = ALIVE;
                    }
                    else if(numNeighborsAlive > 3){
                        myNextState = DEAD;
                    }
            }
        }
    }

    private void findNumNeighborsAlive(){
        numNeighborsAlive = 0;
        for(Cell c : myNeighbors){
            switch(c.getState()){
                case ALIVE:
                    numNeighborsAlive++;
            }
        }
    }
}
