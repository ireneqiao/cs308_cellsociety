package CellSociety;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CellWATOR extends Cell {

    private static final String FISH = "Fish";
    private static final String SHARK = "Shark";
    private static final String EMPTY = "Empty";

    private double myFishReproTime;
    private double mySharkEnergy;
    private double myNewSharkEnergy;
    private double mySharkEatingEnergy;
    private double mySharkReproTime;

    private double myNextTurnsSurvived;
    private double myNextSharkEnergy;

    private double myTurnsSurvived;
    private Random myRand;
    private List<CellWATOR> myEmptyNeighbors;
    private List<CellWATOR> myFishNeighbors;
    private CellWATOR myNextLocCell;

    /**
     * @author Carrie Hunner (clh87)
     *
     * @param row int index of the row of the cell in a grid of cells that will be passed through
     *            when setting neighbors
     * @param col int index of the column of the cell in a grid of cells that will be passed through when]
     *            setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters ArrayList of doubles containing the turns it takes for a fish to reproduce,
     *                   the turns it takes for a shark to reproduce, the initial energy of a shark,
     *                   and the energy a shark gets by eating a fish
     */
    CellWATOR(int row, int col, String initialState, ArrayList<Double> parameters){
        super(row, col, initialState, parameters);

        myRand = new Random();
        myEmptyNeighbors = new ArrayList<>();
        myFishNeighbors = new ArrayList<>();

        setParams();
        mySharkEnergy = myNewSharkEnergy;

        myTurnsSurvived = 0;
    }

    @Override
    protected void setParams(){
        myFishReproTime = myParams.get(0);
        mySharkReproTime = myParams.get(1);

        myNewSharkEnergy = myParams.get(2);
        mySharkEatingEnergy = myParams.get(3);
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(FISH);
        myStates.add(EMPTY);
        myStates.add(SHARK);
    }

    @Override
    public void updateState(){
        myCurrentState = myNextState;
        myNextState = "";

        myTurnsSurvived = myNextTurnsSurvived;
        mySharkEnergy = myNextSharkEnergy;

        myNextTurnsSurvived = 0;
        myNextSharkEnergy = 0;
    }
    /**
     * Finds and sets the next state of the cell by checking neighbors
     * and following the rules set.
     */
    @Override
    public void findNextState() {
        setParams();
        myFishNeighbors.clear();
        myEmptyNeighbors.clear();

        myTurnsSurvived += 1;
        myNextLocCell = null;

        switch(myCurrentState){
            case FISH:
                //making sure fish wasn't eaten
                if(myNextState.equals("")){
                    findEmptyNeighbors();
                    setFishNextState();
                }
                return;
            case SHARK:
                findFishNeighbors();
                findEmptyNeighbors();

                //checks if died
                if (sharkDied()) return;
                //eating fish
                if(!myFishNeighbors.isEmpty()){
                    moveToFishNeighbor();
                }
                else if(!myEmptyNeighbors.isEmpty()){
                    moveToEmptyNeighbor();
                }
                else{
                    sharkStays();
                }
                return;
            case EMPTY:
                if(myNextState.equals("")){
                    myNextState = EMPTY;
                }
        }
    }

    private void moveToEmptyNeighbor() {
        checkForBaby(SHARK);
        moveSharkToEmptyNeighbor();
        resetCellIfNecessary();
    }

    private void moveToFishNeighbor() {
        checkForBaby(SHARK);
        eatFish();
        resetCellIfNecessary();
    }

    private boolean sharkDied() {
        if(mySharkEnergy <= 0){
            myNextState = EMPTY;
            return true;
        }
        return false;
    }

    private void resetCellIfNecessary() {
        //no baby
        if(myNextState.equals("")){
            myNextState = EMPTY;
        }
        myNextTurnsSurvived = 0;
    }

    private void setFishNextState() {
        findEmptyNeighbors();
        //can't move
        if(myEmptyNeighbors.isEmpty()){
            fishStays();
            return;
        }
        else{
            checkForBaby(FISH);
            moveFish();
            resetCellIfNecessary();
            return;
        }
    }

    private void sharkStays() {
        myNextState = SHARK;
        mySharkEnergy--;
    }

    private void moveSharkToEmptyNeighbor() {
        int nextLocationIndex;
        nextLocationIndex = myRand.nextInt(myEmptyNeighbors.size());
        CellWATOR nextCell = myEmptyNeighbors.get(nextLocationIndex);
        //System.out.println("\tAbove wants to move to empty row: " + nextCell.myRow + " Col: " + nextCell.myCol);
        nextCell.setNextState(SHARK);
        ////System.out.println("\tAbove new state is: " + nextCell.getNextState());
        nextCell.setNextSharkEnergy(mySharkEnergy - 1);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
    }

    private void eatFish() {
        int nextLocationIndex;
        nextLocationIndex = myRand.nextInt(myFishNeighbors.size());
        CellWATOR nextCell = myFishNeighbors.get(nextLocationIndex);
        //System.out.println("\tAbove going to eat Row: " + nextCell.myRow + " Col: " + nextCell.myCol);
        ////System.out.println("\tShark Energy: " + mySharkEnergy);

        //make sure if fish had already planned on moving, that cell will be empty instead
        if(nextCell.getNextLocCell() != null){
            nextCell.getNextLocCell().setNextState(EMPTY);
        }
        nextCell.setNextState(SHARK);

        nextCell.setNextSharkEnergy(mySharkEnergy - 1 + mySharkEatingEnergy);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
        ////System.out.println("\tSet above to EMPTY cuz eating fish");
        ////System.out.println("\tEaten Fish cell state: " + nextCell.getNextState());
    }

    private void fishStays() {
        myNextState = FISH;
        ////System.out.println("\tFish Stayed row: " + myRow + "");
        return;
    }

    public CellWATOR getNextLocCell(){
        return myNextLocCell;
    }

    //moves fish to empty spot
    private void moveFish() {
        int numEmptyNeighbors = myEmptyNeighbors.size();
        int nextLocationIndex = myRand.nextInt(numEmptyNeighbors);
        CellWATOR nextCell = myEmptyNeighbors.get(nextLocationIndex);
        ////System.out.println("\tAbove wants to move to row: " + nextCell.myRow + " Col: " + nextCell.myCol);
        myNextLocCell = nextCell;
        nextCell.setNextState(FISH);
        nextCell.setNextTurnsSurvived(myTurnsSurvived);
    }

    private void checkForBaby(String s){
        switch(s){
            case SHARK:
                if(myTurnsSurvived > mySharkReproTime){
                    myNextState = SHARK;
                    //System.out.println("\t Turns: " + myTurnsSurvived + " > Repro " + mySharkReproTime + " = BABY");
                    myNextTurnsSurvived = 0;
                    myTurnsSurvived = 0;    //reset turns survived
                    myNextSharkEnergy = myNewSharkEnergy;
                }
                return;
            case FISH:
                if(myTurnsSurvived > myFishReproTime){
                    //System.out.println("\t Turns: " + myTurnsSurvived + " > Repro " + myFishReproTime + " = BABY");
                    myNextTurnsSurvived = 0;
                    myNextState = FISH;
                    myTurnsSurvived = 0;
                    //System.out.println("\t BABY");

                    return;
                }
                return;
        }
    }

    private void findFishNeighbors(){
        for(Cell c: myNeighbors){
            if(c.getState().equals(FISH)){
                //make sure not claimed by other shark
                if(!c.getNextState().equals(SHARK)){
                    myFishNeighbors.add((CellWATOR)c);
                }
                else{
                    ////System.out.println("\tFish neighbor claimed by other shark");
                }
            }
        }
    }

    /**
     * Sets the number of turns survived.
     * This value is used in determining the reproduction of the animal.
     * @param turnsSurvived double of the number of turns survived.
     */
    public void setNextTurnsSurvived(double turnsSurvived){
        myNextTurnsSurvived = turnsSurvived;
    }

    /**
     * Sets the shark's energy.
     * Used in determining if shark dies.
     * @param energy double of the shark's current energy.
     */
    public void setNextSharkEnergy(double energy){
        myNextSharkEnergy = energy;
    }

    //locates and stores empty neighbors
    private void findEmptyNeighbors(){
        for(Cell c: myNeighbors){
            if(c.getState().equals(EMPTY)){
                if(c.getNextState().equals("") || c.getNextState().equals(EMPTY)){
                    ////System.out.println("\t \tNeighbors next state it: " + c.getNextState());
                    myEmptyNeighbors.add((CellWATOR)c);
                }
                else{
                    ////System.out.println("\t \tAbove has a claimed neighbor: " + c.getNextState());
                }

            }
        }
    }

    @Override
    public void userSwitchState(){
        super.userSwitchState();
        myTurnsSurvived = 0;

        if(myCurrentState.equals(SHARK)){
            mySharkEnergy = myNewSharkEnergy;
        }
    }

}
