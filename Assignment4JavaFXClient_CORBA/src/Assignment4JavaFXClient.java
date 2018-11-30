
import business.TunaFacadeRemote;
import entity.Tuna;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * javaFX client for the assignment 4
 * JEE Project
 * 
 * Permits clients to 
 *  view the data
 *  insert a tuna
 * 
 * @author Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva
 */
public class Assignment4JavaFXClient extends Application{


    //Labels for the attribute names
    private Label recordNumLbl = new Label();
    private Label omegaLbl = new Label();
    private Label deltaLbl = new Label();
    private Label uuidLbl = new Label();
    private Label thetaLbl = new Label();
    private Label idLbl = new Label();
    
    //TfextFields for the attributes
    private TextField recordNumTxtField = new TextField();
    private TextField omegaTxtField = new TextField();
    private TextField deltaTxtField = new TextField();
    private TextField uuidTxtField = new TextField();
    private TextField thetaTxtField = new TextField();
    private TextField idTxtField = new TextField();
    private TextArea TunaTxtArea = new TextArea();
    
    //Thre buttons, one to insert and one to view data, one for clean
    private Button viewButton = new Button();
    private Button addButton = new Button();
    
    private static TunaFacadeRemote remoteTuna = null;

    @Override
    public void start(Stage primaryStage) {

        //Set the label text
        recordNumLbl.setText("Record Number");
        omegaLbl.setText("Omega");
        deltaLbl.setText("Delta");
        idLbl.setText ("ID");
        thetaLbl.setText ("Theta");
        uuidLbl.setText ("UUID");
        
        //Set the view button text
        viewButton.setText("Show Tunas");
        //Set the action on pressed
        viewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (remoteTuna != null) {
                        //Clear the text area
                        TunaTxtArea.clear();
                        //For each tuna append it in the text area
                        for (Tuna s : remoteTuna.findAll())
                            TunaTxtArea.appendText(s.toString()+"\n");

                    } else {
                        //If their is no connection
                        showAlert(AlertType.ERROR, "No remote object available");
                    }
                } catch (Exception ex) {
                    showAlert(AlertType.ERROR, ex.getMessage());
                }
            }
        });

        //Set the insert button button text
        addButton.setText("Add Tuna");
        //Set the action on pressed
        addButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (remoteTuna != null) {
                        //Create a new tuna
                        Tuna Tuna = new Tuna();
                        //Set the attributes
                        Tuna.setRecordNumber(Integer.parseInt(recordNumTxtField.getText()));
                        Tuna.setOmega(omegaTxtField.getText());
                        Tuna.setTheta(thetaTxtField.getText());
                        Tuna.setDelta(thetaTxtField.getText());
                        Tuna.setUuid(uuidTxtField.getText());
                        Tuna.setId(Long.valueOf(idTxtField.getText()));
                        
                        
                        //Insert it
                        remoteTuna.create(Tuna);
                    } else {
                        showAlert(AlertType.ERROR, "No remote object available");
                    }
                } catch (Exception ex) {
                    showAlert(AlertType.ERROR, ex.getMessage());
                }
            }
        });

        //HBox to contain the buttons
        HBox buttonsHBox = new HBox();
        buttonsHBox.setSpacing(10);
        buttonsHBox.setPadding(new Insets(10, 10, 10, 10));
        buttonsHBox.setAlignment(Pos.CENTER);
        buttonsHBox.getChildren().addAll(addButton, viewButton);

        //Root Pane for the application
        GridPane root = new GridPane();
        //Adding all components in the root pane
        root.add(recordNumLbl, 1, 1); // column 1, row 1
        root.add(recordNumTxtField, 2, 1); // column 2, row 1
        root.add(omegaLbl, 1, 2); // column 1, row 2
        root.add(omegaTxtField, 2, 2); // column 2, row 2
        root.add(thetaLbl, 1, 3); // column 1, row 3
        root.add(thetaTxtField, 2, 3); // column 2, row 3
        root.add(deltaLbl, 1, 4); // column 1, row 4
        root.add(deltaTxtField, 2, 4); //column 2, row 4
        root.add(uuidLbl, 1, 5);
        root.add(uuidTxtField, 2, 5);
        root.add(idLbl, 1, 6); 
        root.add(idTxtField, 2, 6);
        
        root.add(buttonsHBox, 2, 7); // column 2, row 7
        root.add(TunaTxtArea, 2, 8); // column 2, row 8
        root.setHgap(10);
        root.setVgap(10);
        //As well as the margin
        root.setMargin(TunaTxtArea, new Insets(0, 10, 10, 0)); // top, right, bottom, left
        root.setMargin(recordNumTxtField, new Insets(0, 10, 0, 0));
        root.setMargin(omegaTxtField, new Insets(0, 10, 0, 0));
        root.setMargin(thetaTxtField, new Insets(0, 10, 0, 0));
        root.setMargin(deltaTxtField, new Insets(0, 10, 0, 0));
        root.setMargin(uuidTxtField, new Insets(0, 10, 0, 0));
        root.setMargin(idTxtField, new Insets(0, 10, 0, 0));
        Scene scene = new Scene(root);

        //Application title
        primaryStage.setTitle("JavaFX Client Demo - CORBA");
        primaryStage.setScene(scene);
        primaryStage.show();
        showAlert(AlertType.INFORMATION, "Trying for a session...");
        //Grab a session
        remoteTuna = getRemoteSession();
        if(remoteTuna != null){
            showAlert(AlertType.INFORMATION, "Got a session :)");
        }
        else{
            showAlert(AlertType.ERROR, "No remote object available");
        }
    }

    //
    /**
     * Helper method to display alerts
     * 
     * @param alertType Type of alert. INFORMATION, ALERT etc.
     * @param message Message to display
     */
    public void showAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Grabs a session from the assignment 4
     * url.
     * 
     * @return Remote TunaFacade
     */
    private static TunaFacadeRemote getRemoteSession() {
        TunaFacadeRemote session = null;

        // CORBA properties and values and lookup taken after earlier work provided by
        // Todd Kelley (2016) Personal Communication
        System.setProperty("org.omg.CORBA.ORBInitialHost", "127.0.0.1");
        System.setProperty("org.omg.CORBA.ORBInitialPort", "3700");
        try {
            InitialContext ic = new InitialContext();
            session = (TunaFacadeRemote) ic.lookup("java:global/Assignment4/Assignment4-ejb/TunaFacade");
        } catch (NamingException e) {
            System.out.println("Problem Cause: \n" + e.getMessage());
        } catch (Exception e) {
            System.out.println("Problem Cause: \n" + e.getMessage());
        }
        return session;
    }

    public static void main(String[] args) {
        Application.launch(Assignment4JavaFXClient.class);
    }
    
}
