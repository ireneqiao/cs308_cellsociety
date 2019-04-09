package CellSociety;

import java.util.ArrayList;

public class CellPercolation extends Cell {
    private final String OPEN  = "Open";
    private final String BLOCKED = "Blocked";
    private final String PERCOLATED = "Percolated";


    /**
     * @author Carrie Hunner (clh87)
     *
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters empty arraylist that is not needed for this concrete implementation
     */
    CellPercolation(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(OPEN);
        myStates.add(BLOCKED);
        myStates.add(PERCOLATED);
    }

    /**
     * Finds and sets the next state of the cell by checking neighbors
     * and following the rules set.
     */
    @Override
    public void findNextState() {
        if(myCurrentState.equals(OPEN)) {
            checkNeighborsAndPercolate();
        }
        else{
            myNextState = myCurrentState;
        }

    }

    //tests neigh
    private void checkNeighborsAndPercolate() {
        for (Cell c : myNeighbors) {
            if (c.getState().equals(PERCOLATED)) {
                myNextState = PERCOLATED;
                break;
            }
            else{
                myNextState = OPEN;
            }
        }
    }
}
