package Components.Auth.Edit;

import Service.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.UsersDAO;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by mjaskot on 2017-05-17.
 */

public class EditController implements Initializable {

    @FXML private Button edit;
    @FXML private Button delete;

    @FXML private TextField username;
    @FXML private TextField email;
    @FXML private TextField password;
    @FXML private TextField rpassword;


    public void editUser(){
        if((!email.getText().isEmpty() && !username.getText().isEmpty() && !password.getText().isEmpty()) ) {
            if (password.getText().equals(rpassword.getText())) {
                try {
                    UsersDAO.updateUser(email.getText(), password.getText(), username.getText());
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            else{
                System.out.println("Sprawdź Hasła");
            }
        }else
        {
            System.out.println("Uzupełnij Pola");
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        delete.setOnAction(event -> {
            try {
                UsersDAO.deleteUser(Session.getUserId());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}
