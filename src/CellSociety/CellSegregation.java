package CellSociety;

import java.util.ArrayList;

public class CellSegregation extends Cell {
    private final String GROUP1 = "Group1";
    private final String GROUP2 = "Group2";
    private final String EMPTY = "Empty";

    private double myThreshold;
    private double mySatisfaction;


    /**
     * @author Carrie Hunner (clh87)
     *
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters ArrayList of doubles with the threshold frequency as a decimal value
     */
    CellSegregation(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);
        setParams();
    }

    /**
     * Sets the parameters of the simulation.
     */
    @Override
    protected void setParams(){
        myThreshold = myParams.get(0);
    }


    @Override
    protected void initializeStatesList() {
        myStates.add(GROUP1);
        myStates.add(GROUP2);
        myStates.add(EMPTY);
    }

    /**
     * Finds and sets the next state of the cell by checking neighbors
     * and following the rules set.
     */
    @Override
    public void findNextState() {
        setParams();
        //if it's empty and hasn't had anything overwrite it yet
        if(myCurrentState.equals(EMPTY) && myNextState.equals("")){
            myNextState = EMPTY;
            //System.out.println("Empty:" + " Row: " + myRow + " Col: " + myCol);
            //System.out.println();
            return;
        }
        //if it's nextstate has already been set externally by another cell
        else if(!myNextState.equals("")){
            return;
        }
        calcSatisfaction();
        //System.out.println("Satisfaction: " + mySatisfaction + " Row: " + myRow + " Col: " + myCol);
        if(mySatisfaction >= myThreshold){
            myNextState = myCurrentState;
            //System.out.println("Above stayed put");
            //System.out.println();
            return;
        }
        else{
            findAndSetNewLocation();
            //myNextState = EMPTY;
        }
        System.out.println();
    }

    private void findAndSetNewLocation() {
        int tempRow = myRow;
        int tempCol;

        //dealing with getting to the end of the current row
        for(tempCol = myCol; tempCol < myGrid[0].length; tempCol++){
            if(foundAndSetNextLoc(tempRow, tempCol)){
                //System.out.println("Above moved in row to Row: " + tempRow + " Col: " + tempCol);
                //System.out.println();
                return;
            }
        }

        //dealing with getting to end of grid
        for(tempRow += 1; tempRow < myGrid.length; tempRow++){
            for(tempCol = 0; tempCol < myGrid[0].length; tempCol++){
                if(foundAndSetNextLoc(tempRow, tempCol)){
                    //System.out.println("Above moved down row to Row: " + tempRow + " Col: " + tempCol);
                    //System.out.println();
                    return;
                }
            }
        }

        //going to end of grid
        for(tempRow = 0; tempRow < myRow; tempRow++){
            for(tempCol = 0; tempCol < myCol; tempCol++){
                if(foundAndSetNextLoc(tempRow, tempCol)){
                    //System.out.println("Above moved to Row: " + tempRow + " Col: " + tempCol);
                    //System.out.println();
                    return;
                }
            }
        }
        //System.out.println("Above couldn't move");
        //System.out.println();
        switch(myCurrentState){
            case GROUP1:
                myNextState = GROUP1;
                //System.out.println("Above should stay " + GROUP1);
                break;
            case GROUP2:
                myNextState = GROUP2;
                //System.out.println("Above should stay " + GROUP2);
                break;

        }
    }

    private boolean isEmpty(Cell c){
        if(c.getState().equals(EMPTY)){
            //makes sure it's not already claimed
            if(c.getNextState().equals("") || c.getNextState().equals(EMPTY)){
                return true;
            }
        }
        return false;
    }

    private boolean foundAndSetNextLoc(int row, int col){
        Cell temp = myGrid[row][col];
        if(isEmpty(temp)){
            temp.setNextState(myCurrentState);
            myNextState = EMPTY;
            return true;
        }
        return false;
    }

    private void calcSatisfaction() {
        double numPop1 = 0;
        double numPop2 = 0;
        int index = 0;
        for(Cell c : myNeighbors){
            switch(c.getState()){
                case GROUP1:
                    numPop1 += 1.0;
                    //System.out.println("Neighbor " + index + " is Red and state is " + c.getState());
                    break;
                case GROUP2:
                    //System.out.println("Group2 is currently stored as: " + GROUP2);
                    numPop2 += 1.0;
                    //System.out.println("Neighbor " + index + " is Blue and state is " + c.getState());
                    break;
            }
            index++;
        }
        //System.out.println("Below Num Group1: " + numPop1 + " Num Group2: " + numPop2);
        double tot = numPop1 + numPop2;
        switch(myCurrentState){
            case GROUP1:
                mySatisfaction = numPop1/tot;
                break;


            case GROUP2:
                mySatisfaction = numPop2/tot;
                break;
        }
        if(!(mySatisfaction >= 0)){
            mySatisfaction = 0.0;
        }
    }

}
