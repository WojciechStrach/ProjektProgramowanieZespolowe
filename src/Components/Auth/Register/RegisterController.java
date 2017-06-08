package Components.Auth.Register;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Main.ParentsList;
import Main.ParentsLoader;
import Utilize.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.UsersDAO;


public class RegisterController implements Initializable {

    @FXML private Button register;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField repeatPassword;
    @FXML private Button Back;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Back.setOnAction(event -> {
            try {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.splash)));
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }
    public void createUser()
    {
        if((!email.getText().isEmpty() && !username.getText().isEmpty() && !password.getText().isEmpty()) ) {
            if (password.getText().equals(repeatPassword.getText())) {
                try {
                    UsersDAO.insertUser(email.getText(),password.getText(), username.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText(" ");
                alert.setContentText("Hasła nie zgadzają się");
                alert.showAndWait();
                System.out.println("Hasła nie zgadzają się.");
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText(" ");
            alert.setContentText("Uzupełnij pola");
            alert.showAndWait();
            System.out.println("Uzupełnij Pola.");
        }
    }
}
