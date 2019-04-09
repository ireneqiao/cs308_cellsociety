package CellSociety.Neighbors;

import CellSociety.Cell;

import java.util.ArrayList;

public class NeighborsSquare extends Neighbors {


    public NeighborsSquare(int row, int col, Cell[][] grid) {
        super(row, col, grid);
    }

    @Override
    protected void setIndexMap() {
        int[] dRow = new int[]{-1, 0, 1};
        int[] dCol = new int[]{-1, 0, 1};
        ////System.out.println("Setting Index map for Row: " + myRow + " Col: " + myCol);
        Integer key = -1;
        ////System.out.println("Row: " + myRow + " Col: " + myCol);
        for (int k = 0; k < dRow.length; k++) {
            for (int i = 0; i < dCol.length; i++) {

                key++;
                int tempRow = dRow[k] + myRow;
                int tempCol = dCol[i] + myCol;



                //ensures not to add self
                if (tempRow == myRow && tempCol == myCol) {
                    ////System.out.println("\t Was self and allegedly continues right after this");
                    ////System.out.println("\t Found Self and ignored (" + tempRow + ", " + tempCol + ")");
                    key--;  //key shouldn't increment for self
                    continue;
                }
                ////System.out.println("\t attempting handle edges");
                handleEdgesAndAddCoords(key, tempRow, tempCol);
                ////System.out.println("\t succeeded in handling edges");

            }

        }
    }




}
