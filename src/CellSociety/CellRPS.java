package CellSociety;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Random;

public class CellRPS extends Cell implements Comparator<String> {
    private final String WHITE = "White";
    private final String RED = "Red";
    private final String GREEN = "Green";
    private final String BLUE = "Blue";

    private Random myRand;
    private ArrayList<Cell> myUneatenNeighbors;
    private Cell myNextLoc;

    /**
     * @param row          int index of the row of the cell in a grid of cells that will be passed through
     *                     when setting neighbors
     * @param col          int index of the column of the cell in a grid of cells that will be passed through when]
     *                     setting the neighbors
     * @param initialState String indicating the initial state of the cell
     * @param parameters   ArrayList of doubles containing any extra parameters needed e.g. probability
     * @author Carrie Hunner (clh87)
     */
    CellRPS(int row, int col, String initialState, ArrayList<Double> parameters) {
        super(row, col, initialState, parameters);
        myRand = new Random();
        myUneatenNeighbors = new ArrayList<>();
    }

    @Override
    protected void initializeStatesList() {
        myStates.add(WHITE);
        myStates.add(RED);
        myStates.add(GREEN);
        myStates.add(BLUE);
    }

    /**
     * Sets the next state of the cell using the rules provided that
     * red eats blue, green eats red, blue eats green, and everything eats white.
     */
    @Override
    public void findNextState() {
        //next state already been set
        if(!myNextState.equals("")){
            return;
        }
        setUneatenNeighbors();

        if(!myUneatenNeighbors.isEmpty()) {
            System.out.println("Uneaten neighbor size: " + myUneatenNeighbors.size());
            int neighborIndex = myRand.nextInt(myUneatenNeighbors.size());
            Cell cellNeighbor = myUneatenNeighbors.get(neighborIndex);
            String neighborState = myUneatenNeighbors.get(neighborIndex).getState();

            //current cell eats neighbor
            if (compare(myCurrentState, neighborState) == 1) {
                myNextLoc = cellNeighbor;
                myNextLoc.myNextState = myCurrentState;

                cellNeighbor.setNextState(myCurrentState);
                myNextState = myCurrentState;
            }
            //neighbor eats current cell
            else if (compare(myCurrentState, neighborState) == -1) {
                myNextState = cellNeighbor.getState();
            }
            else {
                myNextState = myCurrentState;
            }
        }
        else{
            myNextState = myCurrentState;
        }
    }

    //determines what neighbors
    private void setUneatenNeighbors(){
        for(Cell c : myNeighbors){
            if(c.myNextState.equals("") || c.myNextState.equals(WHITE)){
                myUneatenNeighbors.add(c);
            }
        }
    }

    private void setNextLoc(Cell cell){
        myNextLoc = cell;
    }

    /**
     * Used to compare the states of the cell.
     * Red eats blue, green eats red, blue eats green, and everything eats white.
     * @param s1    String of the state to compare
     * @param s2    String of the state to compare
     * @return int of -1 if the s2 is great, 1 if s1 is greater, and 0 if they are equal
     */
    @Override
    public int compare(String s1, String s2) {
        HashSet<String> states = new HashSet<>();
        states.add(s1);
        states.add(s2);

        //checks if the same
        if(s1.equals(s2)){
            return 0;
        }

        if(states.contains(RED) && states.contains(BLUE)){
            if(s1.equals(RED)){
                return 1;
            }
            else{
                return -1;
            }
        }
        else if(states.contains(RED) && states.contains(GREEN)) {
            if (s1.equals(RED)) {
                return -1;
            } else {
                return 1;
            }
        }
        else if(states.contains(GREEN) && states.contains(BLUE)){
            if (s1.equals(GREEN)){
                return -1;
            }
            else {
                return 1;
            }
        }
        //everything beats white
        else if(states.contains(WHITE)){
            if(s1.equals(WHITE)){
                return -1;
            }
            else{
                return 1;
            }
        }
        return 0;
    }
}
