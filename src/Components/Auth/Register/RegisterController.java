package Components.Auth.Register;

import java.awt.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Utilize.DatabaseHandler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.UsersDAO;


public class RegisterController implements Initializable {

    @FXML private Button register;
    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField repeatPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        DatabaseHandler.setDatabaseUrl("localhost");
        DatabaseHandler.setDatabaseName("programowaniegrupowe");
        DatabaseHandler.setUsername("mjaskot");
        DatabaseHandler.setPassword("sushijestsmaczne");
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
                System.out.println("Hasła nie zgadzają się.");
            }
        }
        else
        {
            System.out.println("Uzupełnij Pola.");
        }
    }
}
