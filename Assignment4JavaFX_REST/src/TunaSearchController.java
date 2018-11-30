/* File: TunaJavaFX.java
 * Author: Stanley Pieda
 * The code and methods in this file originally came from Edgar Martinez et. al. (n.d.)
 * They have been modified slightly to work with a DemoStuff application server for educational purposes.
 * Edgar Martinez, Susan Moxley, and Juan Quezada (n.d.). Developing an Enterprise Application with JavaFX 2.0 and Java EE 7. [online]. Retrieved from
 * http://www.oracle.com/webfolder/technetwork/tutorials/obe/java/javafx_json_tutorial/javafx_javaee7_json_tutorial.html
*/
import entity.Tuna;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;


/**
 * javaFX client for the assignment 4
 * Uses REST services
 * JEE Project
 * 
 * @author Vuong Dinh, Parthashokbh Chauhan, Khanh Vuviet, Aksharbhavin Chauhan, Shubham Sachdeva
 */
public class TunaSearchController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private TextField textFieldSearch;
    @FXML
    private Button buttonSearch;
    @FXML
    private TableView<Tuna> tableView;

    /*
    * Handles the search button action
    */
    @FXML
    private void handleSearchAction() {
        WebTarget clientTarget;
        ObservableList<Tuna> data = tableView.getItems();
        data.clear();
        Client client = ClientBuilder.newClient();
        client.register(TunaMessageBodyReader.class);
        if (textFieldSearch.getText().length() > 0) {
            //URL for the JEE Entity
            clientTarget = client.target("http://localhost:8080/Assignment4-war/webresources/entity.tuna/{beginBy}");
            clientTarget = clientTarget.resolveTemplate("beginBy", textFieldSearch.getText());
        } else {
            //URL for the JEE Entity
            clientTarget = client.target("http://localhost:8080/Assignment4-war/webresources/entity.tuna/");
        }
        GenericType<List<Tuna>> listc = new GenericType<List<Tuna>>() {
        };
        List<Tuna> customers = clientTarget.request("application/json").get(listc);

        //Add the tunas to the array
        for (Tuna c : customers) {
            data.add(c);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        handleSearchAction();
    }

}
