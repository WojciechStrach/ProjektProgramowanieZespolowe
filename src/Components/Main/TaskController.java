package Components.Main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import model.Tasks;
import model.UsersDAO;

import java.io.IOException;
import java.sql.SQLException;

public class TaskController {
    @FXML
    private HBox hBox;
    @FXML
    private Label descriptionLabel;
    @FXML
    private ImageView taskAssignedAvatarImageView;
    public TaskController(Tasks task)
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Task.fxml"));
        fxmlLoader.setController(this);
        try
        {
            fxmlLoader.load();
            if(task != null) {
                descriptionLabel.setText(task.getDescription() + " : " + UsersDAO.searchUsers(task.getUserId()).getDisplayName());
//                Image image = UsersDAO.searchUsers(task.getUserId()).getAvatar();
//                if (image != null) {
//                    taskAssignedAvatarImageView.setImage(image);
//                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public HBox getBox()
    {
        return hBox;
    }
}
