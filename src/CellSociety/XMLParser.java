package CellSociety;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.*;

import static java.util.Map.entry;

/**
 * @author Robert C. Duvall
 * @author Rhondu Smithwick
 * @author Hsingchih Tang
 * Parser for XML files. Built based on XMLParser.java and XMLException.java in sample project
 * Read the simulation's size, type and initial configuration parameters from certain files
 * Has private methods for parsing contents in the file
 * and public methods for returning information to caller
 */
public class XMLParser {
    // Immutable file path and XML tags for parsing file
    static final String ALERT_CONFIG_PATH = "resources/XMLAlertText.txt";
    static final String SIM_TYPE_TAG = "Type";
    static final String WIDTH_TAG = "Width";
    static final String HEIGHT_TAG = "Height";
    static final String CONFIG_TAG = "SpecifiedConfig";
    static final String CELL_SHAPE_TAG = "CellShape";
    static final String CELL_NEIGHBOR_TAG = "NeighborStyle";
    static final String EDGE_TAG = "EdgeStyle";
    static final String STATE_TAG = "State";
    static final String STATE_NAME_TAG = "StateName";
    static final String STATE_IMG_TAG = "StateImage";
    static final String STATE_PERCENT_TAG = "StatePercentage";
    static final String PARAMETER_TAG = "Parameter";
    static final String CELL_TAG = "Cell";
    static final String CELL_ROW_TAG = "Row";
    static final String CELL_COL_TAG = "Col";
    static final String CELL_STATE_TAG = "CellState";
    private final Map<String, Integer> VALID_CELL_SHAPE_MAXNEIGHBOR = Map.ofEntries(
            entry("Square", 8),
            entry("Triangle", 12));
    private final List<String> VALID_EDGE_TYPE = List.of(
            "Finite",
            "Toroidal");

    // XMLAlerts to pop up when encountering mal-formatted XML file
    // package-private variables
    XMLAlert fileNotFoundAlert = new XMLAlert();
    XMLAlert parserConfigAlert = new XMLAlert();
    XMLAlert SAXAlert = new XMLAlert();
    XMLAlert gridErrAlert = new XMLAlert();
    XMLAlert modelErrAlert = new XMLAlert();
    XMLAlert paramErrAlert = new XMLAlert();
    XMLAlert configErrAlert = new XMLAlert();
    XMLAlert neighborErrAlert = new XMLAlert();
    XMLAlert stateErrAlert = new XMLAlert();
    XMLAlert cellIdxAlert = new XMLAlert();
    XMLAlert cellStateAlert = new XMLAlert();
    XMLAlert cellConfigAlert = new XMLAlert();
    XMLAlert cellInfoAlert = new XMLAlert();
    private XMLAlert[] myAlertArr = new XMLAlert[]{fileNotFoundAlert, parserConfigAlert, SAXAlert, gridErrAlert,
            modelErrAlert, paramErrAlert, configErrAlert, neighborErrAlert, stateErrAlert, cellIdxAlert,
            cellStateAlert, cellConfigAlert, cellInfoAlert};

    // private variables for storing parsing results
    private DocumentBuilder myDBuilder;
    private String mySimulationType = "";
    private String myCellShape = "Square";
    private String myEdgeType = "Finite";
    private Element mySimRoot;
    private Integer myWidth;
    private Integer myHeight;
    private HashMap<String, String> stateImage = new HashMap<>();
    private HashMap<String, Double> statePercent = new HashMap<>();
    private HashMap<List<Integer>, String> cellState = new HashMap<>();
    private ArrayList<Double> parameters = new ArrayList<>();
    private ArrayList<Integer> neighbors = new ArrayList<>();
    private boolean specConfig = false;
    private boolean parseSuccess = true;


    /**
     * Constructor of the XMLParser
     *
     * @param f the file to parse
     * @throws Exception ParserConfigurationException to be handled in Simulation class,
     *                   which will terminate the program and print error message to console
     */
    public XMLParser(File f) throws Exception {
        try {
            setupAlert();
        } catch (Exception e) {
            throw new Exception("Alert Setup went wrong", e);
        }
        try {
            this.myDBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            // Error case: ParserConfigurationException handling
            throw new Exception("ParserConfigurationException occurs", e);
        }
        try {
            this.mySimRoot = getRootElement(f);
        } catch (SAXException e) {
            callAlert(SAXAlert);
        } catch (IOException e) {
            callAlert(fileNotFoundAlert);
        }

        if (this.mySimRoot != null) {
            this.parseSimConfig();
            specConfig = this.parseSpecConfig();
            this.parseCellShape();
            this.parseEdgeType();
            this.parseCellNeighbor();
            this.parseState();
            this.parseParam();
            if (this.specConfig) {
                this.parseCell();
            }
        }

    }


    /**
     * Set up error messages of XMLAlert
     * Alert dialogue boxes will pop up if an XML configuration file is mal-formatted
     *
     * @throws IOException if the source file storing error message text is not found
     */
    private void setupAlert() throws IOException {
        Scanner sc = new Scanner(new File(ALERT_CONFIG_PATH));
        int idx = 0;
        while (sc.hasNextLine() && idx < myAlertArr.length) {
            String[] alertText = sc.nextLine().split(";");
            myAlertArr[idx].setText(alertText[0], alertText[1], alertText[2]);
            idx++;
        }
    }

    /**
     * Pop up an XMLAlert's dialogue box and set flag for notifying Simulation of the parsing failure
     *
     * @param a the corresponding XMLAlert to pop up
     */
    private void callAlert(XMLAlert a) {
        a.showAlert();
        this.parseSuccess = false;
    }


    // Get root element of an XML file
    private Element getRootElement(File xmlFile) throws IOException, SAXException {
        Document myDoc;
        myDBuilder.reset();
        try {
            myDoc = myDBuilder.parse(xmlFile);
            return myDoc.getDocumentElement();
        }
        // Error case: exception handling
        catch (SAXException e) {
            throw new SAXException("SAX Exception occurs", e);
        } catch (IOException e) {
            throw new IOException("IO Exception occurs", e);
        }
    }


    /**
     * Parse the simulation's type, grid width and grid height
     * Display alert dialogue box if information is missing
     */
    private void parseSimConfig() {
        NodeList simTypeNode = this.mySimRoot.getElementsByTagName(SIM_TYPE_TAG);
        // Error case: missing simulation type information
        if (simTypeNode.getLength() == 0) {
            callAlert(modelErrAlert);
            return;
        }
        NodeList widthNode = this.mySimRoot.getElementsByTagName(WIDTH_TAG);
        NodeList heightNode = this.mySimRoot.getElementsByTagName(HEIGHT_TAG);
        // Error case: missing simulation grid size information
        if (widthNode.getLength() == 0 || heightNode.getLength() == 0) {
            callAlert(gridErrAlert);
            return;
        }
        mySimulationType = simTypeNode.item(0).getTextContent();
        myWidth = Integer.valueOf(widthNode.item(0).getTextContent());
        myHeight = Integer.valueOf(heightNode.item(0).getTextContent());
    }


    /**
     * Parse the flag indicating whether cells initial states are explicitly defined in file
     * or should be randomly generated based on distribution percentage
     *
     * @return
     */
    private boolean parseSpecConfig() {
        NodeList specNode = this.mySimRoot.getElementsByTagName(CONFIG_TAG);
        // Error case: Missing file parsing specification info
        if (specNode.getLength() == 0) {
            callAlert(configErrAlert);
            return false;
        }
        return Boolean.valueOf(specNode.item(0).getTextContent());
    }


    /**
     * Parse the cell's visualization shape: Rectangle or Triangle
     * Defaulted to Rectangle
     */
    private void parseCellShape() {
        NodeList shapeNode = this.mySimRoot.getElementsByTagName(CELL_SHAPE_TAG);
        if (shapeNode.getLength() != 0) {
            String shape = shapeNode.item(0).getTextContent();
            if (VALID_CELL_SHAPE_MAXNEIGHBOR.keySet().contains(shape)) {
                myCellShape = shape;
            }
        }
    }


    /**
     * Parse the grid's edge type: Finite or Toroidal
     * Defaulted to Finite
     */
    private void parseEdgeType() {
        NodeList edgeNode = this.mySimRoot.getElementsByTagName(EDGE_TAG);
        if (edgeNode.getLength() != 0) {
            String edge = edgeNode.item(0).getTextContent();
            if (VALID_EDGE_TYPE.contains(edge)) {
                myEdgeType = edge;
            }
        }
    }


    /**
     * Parse the configuration of neighboring cells for a cell
     * Neighbors are numbered by integers (0-7 for Rectangle shape; 0-11 for Triangle shape) and stored in a list
     */
    private void parseCellNeighbor() {
        NodeList neighborNode = this.mySimRoot.getElementsByTagName(CELL_NEIGHBOR_TAG);
        if (neighborNode.getLength() != 0) {
            String[] neighborsInString = neighborNode.item(0).getTextContent().split(";");
            for (String s : neighborsInString) {
                Integer neighborIdx = Integer.valueOf(s);
                if (neighborIdx >= VALID_CELL_SHAPE_MAXNEIGHBOR.get(myCellShape)) {
                    callAlert(neighborErrAlert);
                    return;
                }
                neighbors.add(neighborIdx);
            }
        } else {
            callAlert(neighborErrAlert);
        }
    }


    /**
     * Parse the names, associated visualization colors, and percentage distribution of the states in this simulation
     */
    private void parseState() {
        NodeList stateList = this.mySimRoot.getElementsByTagName(STATE_TAG);
        // Error case: missing state information
        if (stateList.getLength() == 0) {
            callAlert(stateErrAlert);
            return;
        }
        for (int i = 0; i < stateList.getLength(); i++) {
            Node stateNode = stateList.item(i);
            NodeList currName = ((Element) stateNode).getElementsByTagName(STATE_NAME_TAG);
            NodeList currImg = ((Element) stateNode).getElementsByTagName(STATE_IMG_TAG);
            // Error case: missing image for the specified state
            if (currImg.getLength() == 0 || currName.getLength() == 0) {
                callAlert(stateErrAlert);
                return;
            }
            String currStateName = currName.item(0).getTextContent();
            stateImage.put(currStateName, currImg.item(0).getTextContent());

            NodeList currPercent = ((Element) stateNode).getElementsByTagName(STATE_PERCENT_TAG);
            if (currPercent.getLength() != 0) {
                statePercent.put(currStateName, Double.valueOf(currPercent.item(0).getTextContent()));
            }
        }
        // Error case: number of states does not match state percentage map size
        if (stateImage.keySet().size() != statePercent.keySet().size() && !statePercent.keySet().isEmpty()) {
            callAlert(stateErrAlert);
        }
    }


    /**
     * Parse any possible parameter for this simulation (e.g. threshold, cell reproduce time, etc.)
     */
    private void parseParam() {
        NodeList paramList = this.mySimRoot.getElementsByTagName(PARAMETER_TAG);
        for (int i = 0; i < paramList.getLength(); i++) {
            Node paramNode = paramList.item(i);
            parameters.add(Double.valueOf(paramNode.getTextContent()));
        }
    }


    /**
     * Parse cell's initial state if specConfig flag is raised
     * All cells' states are stored in a map and associated with row/column indices
     */
    private void parseCell() {
        NodeList cellList = this.mySimRoot.getElementsByTagName(CELL_TAG);
        // Error case: file parsing specification does not match cell info
        if (cellList.getLength() == 0) {
            callAlert(cellConfigAlert);
            return;
        }
        for (int i = 0; i < cellList.getLength(); i++) {
            Node currCellNode = cellList.item(i);
            // Error case: missing cell information
            if (!validateCellInfo(currCellNode)) {
                callAlert(cellInfoAlert);
                return;
            }
            int currRow = Integer.valueOf(((Element) currCellNode).getElementsByTagName(CELL_ROW_TAG).item(0).getTextContent());
            int currCol = Integer.valueOf(((Element) currCellNode).getElementsByTagName(CELL_COL_TAG).item(0).getTextContent());
            // Error case: cell index out of bounds
            if (!validateCellIdx(currRow, currCol)) {
                callAlert(cellIdxAlert);
                return;
            }
            String currState = ((Element) currCellNode).getElementsByTagName(CELL_STATE_TAG).item(0).getTextContent();
            // Error case: invalid cell state configuration
            if (!this.stateImage.containsKey(currState)) {
                callAlert(cellStateAlert);
                return;
            }
            cellState.put(Arrays.asList(currRow, currCol), currState);
        }
        // Error case: number of cells does not match grid width/height configuration
        if (cellState.keySet().size() != myWidth * myHeight) {
            callAlert(cellInfoAlert);
        }
    }


    /**
     * Check whether cell's row/column indices and initial state are all given
     *
     * @param cellNode the current cell to validate
     * @return boolean value indicating information valid or not
     */
    private boolean validateCellInfo(Node cellNode) {
        return !(((Element) cellNode).getElementsByTagName(CELL_ROW_TAG).getLength() == 0
                || ((Element) cellNode).getElementsByTagName(CELL_COL_TAG).getLength() == 0
                || ((Element) cellNode).getElementsByTagName(CELL_STATE_TAG).getLength() == 0);
    }


    /**
     * Check whether cell's row/column indices are out of bounds of the grid
     *
     * @param row row index of the cell
     * @param col column index of the cell
     * @return boolean value indicating validity of the cell's indices
     */
    private boolean validateCellIdx(int row, int col) {
        return row >= 0 && col >= 0 && row < myHeight && col < myWidth;
    }


    /**
     * @return a string indicating the simulation type
     * Can be Fire, Game of Life, Percolation, Segregation, WaTor, RPS
     */
    public String getSimType() {
        return this.mySimulationType;
    }


    /**
     * @return String indicating the cell's visualization shape
     * Can be Rectangle or Triangle
     */
    public String getCellShape() {
        return this.myCellShape;
    }


    /**
     * @return String indicating the edge type
     * Can be Finite or Toroidal
     */
    public String getEdgeType() {
        return this.myEdgeType;
    }


    /**
     * @return immutable map indicating the visualization color for each state
     */
    public Map<String, String> getStateImg() {
        return Collections.unmodifiableMap(this.stateImage);
    }


    /**
     * @return immutable map indicating the percentage (if any) associated with each state in initial configuration
     */
    public Map<String, Double> getStatePercent() {
        return Collections.unmodifiableMap(this.statePercent);
    }


    /**
     * @return immutable list storing simulation-specific parameters
     * This list will be modifiable through user interaction with the UI scene
     */
    public List<Double> getParameters() {
        return this.parameters;
    }


    /**
     * @return immutable list defining "neighbors" of a cell in the grid with location-based indices
     */
    public List<Integer> getNeighbors() {
        return Collections.unmodifiableList(this.neighbors);
    }


    /**
     * @return immutable list explicitly defining the initial state of each cell
     */
    public Map<List<Integer>, String> getCellState() {
        return Collections.unmodifiableMap(this.cellState);
    }


    /**
     * @return Integer indicating number of cells per row in the grid
     */
    public Integer getWidth() {
        return this.myWidth;
    }


    /**
     * @return Integer indicating number of cells per column in the grid
     */
    public Integer getHeight() {
        return this.myHeight;
    }


    /**
     * @return boolean flag indicating whether the XML parsing process is completed successfully
     */
    public boolean isParseSuccess() {
        return this.parseSuccess;
    }


    /**
     * @return boolean flag indicating whether cells' initial states are explicitly defined in the XML file
     * Simulation will assign cell states completely randomly or based on distribution percentage if this flag is false
     */
    public boolean isSpecConfig() {
        return this.specConfig;
    }



}
