
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * javaFX client for the assignment 4
 * Uses REST services
 * JEE Project
 * 
 * Permits clients to 
 *  view the data
 * 
 * @author Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva
 */
public class Assignment4JavaFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("TunaSearch.fxml"));
        
        //Set the title
        stage.setTitle("Assignment4 JavaFX REST Client");
        //as well as the height and width
        stage.setWidth(650);
        stage.setHeight(650);
        
        Scene scene = new Scene(root);
        
        //Set the scene and display the window
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
