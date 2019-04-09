package CellSociety.CellShapes;

import CellSociety.UI;
import javafx.scene.shape.Polygon;

/**
 * CellShape class provides a template for future cell shape types so that new grid shapes can easily extend CellShape while
 * retaining core features of a CellShape.
 * All CellShape objects have a width, height, row, column, and associated Polygon.
 * Each concrete subclass will calculate the x and y coordinates differently, but all CellShape
 * objects will use the assignCoordinates method.
 * All CellShape objects contain a method to return a Polygon to be used in the UI class. This allows the UI class to
 * simply call the same method regardless of the specific concrete subclass, reducing the amount of hard-coding.
 *
 */
abstract public class CellShape {
    protected final int CELL_WIDTH;
    protected final int CELL_HEIGHT;
    protected final int ROW;
    protected final int COL;
    protected Polygon myShape;

    public CellShape (UI myUI, int row, int col){
        CELL_WIDTH = myUI.GRID_HEIGHT/myUI.GRID_ROW_NUM;
        CELL_HEIGHT = myUI.GRID_WIDTH/myUI.GRID_COL_NUM;
        ROW = row;
        COL = col;
    }

    protected void assignCoordinates(double[] coordinates, int numCoordinates) {
        for (int i = 0; i < numCoordinates; i++) {
            if (i % 2 == 0){ //assign x coordinate
                coordinates[i] = calcXCoordinate(i);
            }
            else {
                coordinates[i] = calcYCoordinate(i);
            }
        }
    }

    abstract protected double calcXCoordinate(int i);

    abstract protected double calcYCoordinate(int i);

    /**
     * Returns the Polygon created by the CellShape based on the input row, column, and cell dimensions
     * @return myShape
     */
    public Polygon getMyShape(){
        return myShape;
    }
}
