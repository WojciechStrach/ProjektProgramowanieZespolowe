package Components.Splash;

import Main.ParentsList;
import Main.ParentsLoader;
import Utilize.DatabaseData;
import Utilize.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;

import java.beans.EventHandler;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by mjaskot on 2017-05-17.
 */

public class SplashController implements Initializable {

    @FXML private Button login;
    @FXML private Button register;

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {



        DatabaseHandler.setDatabaseUrl(DatabaseData.databaseUrl);
        DatabaseHandler.setDatabaseName(DatabaseData.databaseName);
        DatabaseHandler.setUsername(DatabaseData.databaseLogin);
        DatabaseHandler.setPassword(DatabaseData.databasePassword);


        login.setOnAction(event -> {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try {
                    stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.login)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                stage.show();
        });
        register.setOnAction(event -> {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            try {
                stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.register)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            stage.show();
        });
    }
}
