package CellSociety;

import java.util.ArrayList;
import java.util.Random;

public class CellFire extends Cell {

    private final String BURNING = "Burning";
    private final String TREE = "Tree";
    private final String EMPTY = "Empty";

    private Random myRand;

    private double myProbBurning;

    /**
     * @author Carrie Hunner (clh87)
     *
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters ArrayList of doubles containing the probability of a tree catching on fire
     */
    CellFire(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
        setParams();
        myRand = new Random();
    }

    /**
     * Sets the parameters of the simulation.
     */
    @Override
    protected void setParams(){
        myProbBurning = myParams.get(0);
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(BURNING);
        myStates.add(TREE);
        myStates.add(EMPTY);
    }

    /**
     * Calls the cell to look at its neighbors' states and determine its next state according to that and
     * its rules for burning.
     */
    @Override
    public void findNextState() {
        setParams();
        switch(myCurrentState){
            case BURNING:
                myNextState = EMPTY;
                break;
            case EMPTY:
                myNextState = EMPTY;
                break;
            case TREE:
                checkNeighborsAndBurning();
        }
    }

    //handles burning neighbors and setting next state of tree
    private void checkNeighborsAndBurning() {
        for(Cell c : myNeighbors){
            if(c.getState().equals(BURNING) && myRand.nextFloat() < myProbBurning){
                myNextState = BURNING;
                break;
            }
            myNextState = TREE;
        }
    }

}
