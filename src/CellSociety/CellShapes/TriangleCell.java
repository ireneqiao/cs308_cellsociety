package CellSociety.CellShapes;

import CellSociety.UI;
import javafx.scene.shape.Polygon;

/**
 * TriangleCell class extends CellShape so that it contains the basic features of a CellShape but assigns x and y
 * coordinates that form a triangle shape by overriding methods from CellShape superclass.
 *
 * This is well-designed because it simply overrides superclass methods to create a distinct triangle shape. All other
 * methods are handled by the superclass. All triangle-specific information is encapsulated in this subclass.
 */
public class TriangleCell extends CellShape{
    private final static int TRIANGLE_NUM_COORDINATES = 6;
    private final double[] STARTING_COORDINATES_UP_EVEN = new double[]{CELL_WIDTH/2, 0,
            0, CELL_HEIGHT,
            CELL_WIDTH, CELL_HEIGHT};
    private final double[] STARTING_COORDINATES_UP_ODD = new double[]{
            CELL_WIDTH, 0,
            CELL_WIDTH/2, CELL_HEIGHT,
            CELL_WIDTH + CELL_WIDTH/2, CELL_HEIGHT};
    private final double[] STARTING_COORDINATES_DOWN_EVEN = new double[]{
            CELL_WIDTH/2, 0,
            CELL_WIDTH, CELL_HEIGHT,
            CELL_WIDTH + CELL_WIDTH/2, 0};
    private final double[] STARTING_COORDINATES_DOWN_ODD = new double[]{
            0, 0,
            CELL_WIDTH/2, CELL_HEIGHT,
            CELL_WIDTH, 0};

    private double[] myCoordinates;

    public TriangleCell (UI ui, int row, int col){
        super(ui, row, col);
        myCoordinates = new double[TRIANGLE_NUM_COORDINATES];
        super.assignCoordinates(myCoordinates, TRIANGLE_NUM_COORDINATES);
        super.myShape = new Polygon(myCoordinates);

    }

    @Override
    protected double calcXCoordinate(int i){
        double x;
        if (ROW % 2 == 0){
            if (COL % 2 == 0){
                x = STARTING_COORDINATES_UP_EVEN[i] + COL * CELL_WIDTH/2;
            }
            else {
                x = STARTING_COORDINATES_DOWN_EVEN[i] + COL * CELL_WIDTH/2 - CELL_WIDTH/2;
            }
        }
        else {
            if (COL % 2 == 0){
                x = STARTING_COORDINATES_DOWN_ODD[i] + COL * CELL_WIDTH/2;
            }
            else {
                x = STARTING_COORDINATES_UP_ODD[i] + COL * CELL_WIDTH/2 + CELL_WIDTH/2 - CELL_WIDTH;
            }
        }
        return x;
    }

    @Override
    protected double calcYCoordinate(int i){
        double y;
        if (ROW % 2 == 0){
            if (COL % 2 == 0){
                y = STARTING_COORDINATES_UP_EVEN[i] + ROW * CELL_HEIGHT;
            }
            else {
                y = STARTING_COORDINATES_DOWN_EVEN[i] + ROW * CELL_HEIGHT;
            }
        }
        else {
            if (COL % 2 == 0){
                y = STARTING_COORDINATES_DOWN_ODD[i] + ROW * CELL_HEIGHT;
            }
            else {
                y = STARTING_COORDINATES_UP_ODD[i] + ROW * CELL_HEIGHT;
            }
        }
        return y;
    }
}
