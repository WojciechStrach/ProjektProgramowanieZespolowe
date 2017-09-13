package Components.Auth.Edit;

import Main.ParentsList;
import Main.ParentsLoader;
import Service.Session;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
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
    @FXML private PasswordField password;
    @FXML private PasswordField rpassword;
    @FXML private Button Back;


    public void editUser(){
        if(((!email.getText().isEmpty() && !username.getText().isEmpty() && !password.getText().isEmpty()) || username.getText().isEmpty() || email.getText().isEmpty() )) {
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

        Back.setOnAction(event -> {
            try {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.main)));
            }catch (Exception e){
                e.printStackTrace();
            }
        });

        edit.setOnAction(event -> {
            try {
                editUser();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Powodzenie");
                alert.setHeaderText(" ");
                alert.setContentText("Zmiana danych się powiodła!");
                alert.showAndWait();
                System.out.println("User edit success");
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });

        username.setText(Session.getDisplayName());
        email.setText(Session.getEmail());
        password.setText(Session.getPassword());
        rpassword.setText(Session.getPassword());

        delete.setOnAction(event -> {
            try {
                UsersDAO.deleteUser(Session.getUserId());
                try {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.splash)));
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }
}