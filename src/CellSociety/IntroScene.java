package CellSociety;

import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.PopupWindow;
import javafx.stage.Window;

import java.io.File;
import java.security.spec.ECField;
import java.util.ResourceBundle;

/** @author Irene Qiao
 * Presents splash screen to user displaying buttons that the user can click to open a type of simulation or to upload
 * the user's own XML file
 */
public class IntroScene extends Scene {
    private static final int WINDOW_HEIGHT = 600;
    private static final int WINDOW_WIDTH = 600;
    private static final int BUTTONS_VBUFFER_LEFT = 78;
    private static final int BUTTONS_VBUFFER_RIGHT = 130;

    private static final int VBOX_BUFFER_TOP = 100;
    private static final int VBOX_BUFFER_SIDE = 100;

    private ResourceBundle myResources;

    private Simulation mySimulation;
    private Group myRoot;

    /**
     *
     * @param root root of Scene
     * @param width unused
     * @param height unused
     * @param s the simulation in which IntroScene is instantiated
     *          Creates Scene object with specified size
     *          Access resource bundle that contains text to be displayed
     *          Sets up buttons to begin each simulation type or to upload user XML file
     */
    public IntroScene(Group root, double width, double height, Simulation s){ //width and height params unused
        super(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        myRoot = root;
        this.mySimulation = s;
        myResources = ResourceBundle.getBundle("English");
        setupButtons();
    }

    private void setupButtons(){
            BorderPane borderPane = new BorderPane();
            borderPane.setPrefSize(WINDOW_WIDTH, WINDOW_HEIGHT);
            borderPane.setStyle("-fx-background-color: #a3beff");
            borderPane.setLeft(addVBoxLeft());
            borderPane.setRight(addVBoxRight());
            myRoot.getChildren().add(borderPane);
    }

    private VBox addVBoxLeft(){
            VBox vbox = new VBox();
            vbox.setStyle("-fx-background-color: #b5f7ff");
            vbox.setPadding(new Insets(VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE, VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE));
            vbox.setSpacing(BUTTONS_VBUFFER_LEFT);
            vbox.getChildren().addAll(fireSimButton(), GOLSimButton(), PercSimButton(), RPSSimButton());
            return vbox;
    }

    private VBox addVBoxRight(){
        VBox vbox = new VBox();
        vbox.setStyle("-fx-background-color: #f3aaff");
        vbox.setPadding(new Insets(VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE, VBOX_BUFFER_TOP, VBOX_BUFFER_SIDE));
        vbox.setSpacing(BUTTONS_VBUFFER_RIGHT);
        vbox.getChildren().addAll(SegSimButton(), WaTorSimButton(), uploadXML());
        return vbox;
    }

    private Button fireSimButton(){
        Button fireSimButton = new Button(myResources.getString("Fire"));
            fireSimButton.setOnMouseClicked(e -> {
                mySimulation.setSimType(mySimulation.FIRE_XML);
                mySimulation.startSimulation();
            });
        return fireSimButton;
    }

    private Button GOLSimButton(){
        Button GOLSimButton = new Button(myResources.getString("GOL"));
            GOLSimButton.setOnMouseClicked(e -> {
                mySimulation.setSimType(mySimulation.GOL_XML);
                mySimulation.startSimulation();
            });
        return GOLSimButton;
    }

    private Button PercSimButton(){
        Button PercSimButton = new Button(myResources.getString("Perc"));
        PercSimButton.setOnMouseClicked(e -> {
            mySimulation.setSimType(mySimulation.PERC_XML);
            mySimulation.startSimulation();
        });
        return PercSimButton;
    }

    private Button SegSimButton(){
        Button SegSimButton = new Button(myResources.getString("Seg"));
        SegSimButton.setOnMouseClicked(e -> {
            mySimulation.setSimType(mySimulation.SEG_XML);
            mySimulation.startSimulation();
        });
        return SegSimButton;
    }

    private Button WaTorSimButton(){
        Button WaTorSimButton = new Button(myResources.getString("WaTor"));
        WaTorSimButton.setOnMouseClicked(e -> {
            mySimulation.setSimType(mySimulation.WATOR_XML);
            mySimulation.startSimulation();
        });
        return WaTorSimButton;
    }

    private Button RPSSimButton(){
        Button rps = new Button(myResources.getString("RPS"));
        rps.setOnMouseClicked(e -> {
            mySimulation.setSimType(mySimulation.RPS_XML);
            mySimulation.startSimulation();
        });
        return rps;
    }

    private Button uploadXML(){
        Button uploadXML = new Button(myResources.getString("PromptUploadXML"));
        uploadXML.setOnMouseClicked(e -> chooseFile());
        return uploadXML;
    }

    private void chooseFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(myResources.getString("FileChooserTitle"));
        File file = fileChooser.showOpenDialog(new PopupWindow() {
            @Override
            public void show(Window window) {
                super.show(window);
            }
        });
        if (file.toString().equals(
                mySimulation.GOL_XML)
            || file.toString().equals(mySimulation.WATOR_XML)
            || file.toString().equals(mySimulation.FIRE_XML)
                || file.toString().equals(mySimulation.SEG_XML)
                || file.toString().equals(mySimulation.PERC_XML
    )){
            mySimulation.setSimType(file.toString());
            mySimulation.startSimulation();
        }
        else {
            badDataAlert();
        }
    }

    private void badDataAlert(){
        Alert badAlert = new Alert(Alert.AlertType.ERROR);
        badAlert.setTitle(myResources.getString("BadDataAlert"));
        badAlert.setContentText(myResources.getString("ChooseAnotherFile"));
        badAlert.showAndWait();
    }
}
