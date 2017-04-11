package Components.Auth.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Main.KiraStart;
import Main.ParentsList;
import Main.ParentsLoader;
import Service.Accounts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    Accounts accounts = new Accounts();

    @FXML private TextField loginInput;
    @FXML private TextField passwordInput;
    @FXML private Button logInButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logInButton.setOnAction(event -> {
            if(accounts.authorize(loginInput.getText(), passwordInput.getText())) {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                try {
                    stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.main)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Authorization failed");
            }
        });
    }
}
