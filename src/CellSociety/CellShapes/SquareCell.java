package CellSociety.CellShapes;

import CellSociety.UI;
import javafx.scene.shape.Polygon;

/**
 * SquareCell class extends CellShape so that it contains the basic features of a CellShape but assigns x and y
 * coordinates that form a square shape by overriding methods from CellShape superclass.
 *
 * This is well-designed because it simply overrides superclass methods to create a distinct triangle shape.
 * All other methods are handled by the superclass. All square-specific information is encapsulated in this subclass.
 */
public class SquareCell extends CellShape {
    private final static int SQUARE_NUM_COORDINATES = 8;
    private final double[] STARTING_COORDINATES = new double[]{
            0, 0,
            0, CELL_HEIGHT,
            CELL_WIDTH, CELL_HEIGHT,
            CELL_WIDTH, 0
    };

    private double[] myCoordinates;

    public SquareCell(UI ui, int row, int col){
        super(ui, row, col);
        myCoordinates = new double[SQUARE_NUM_COORDINATES];
        assignCoordinates(myCoordinates, SQUARE_NUM_COORDINATES);
        myShape = new Polygon(myCoordinates);
    }

    @Override
    protected double calcXCoordinate(int i){
        double x = STARTING_COORDINATES[i] + COL * CELL_WIDTH;
        return x;
    }

    @Override
    protected double calcYCoordinate(int i){
        double y = STARTING_COORDINATES[i] + ROW * CELL_HEIGHT;
        return y;
    }
}
