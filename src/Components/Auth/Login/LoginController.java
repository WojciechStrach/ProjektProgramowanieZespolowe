package Components.Auth.Login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import Main.ParentsList;
import Main.ParentsLoader;
import Service.Auth;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController implements Initializable {
    Auth accounts = new Auth();

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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(" ");
                alert.setContentText("Autoryzacja nie powiodła się, sprawdź czy wprowadzone dane są poprawne");
                alert.showAndWait();
                System.out.println("Authorization failed");
            }
        });
    }
}
