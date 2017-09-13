package Tests;


import Models.User;
import Utilize.DatabaseHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.Users;
import model.UsersDAO;
import org.junit.Test;


import javax.imageio.ImageIO;
import javax.swing.text.html.ImageView;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by f3cro on 13.09.2017.
 */

public class AddUserTest {

    String email = "twoj.stary@example.com";
    String password = "twojstary";
    String displayName = "twojastara";

    @Test
    public void addToDatabaseCheck() {
        try {

            UsersDAO.insertUser(this.email,this.password,this.displayName);
            assertTrue(true);

        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }

    }

    @Test
    public void checkIfExistAfterAdd() {

        String selectStmt = "SELECT * " +
                "FROM users " +
                "WHERE email =" + this.email +
                "AND password =" + this.password +
                "AND display_name =" + this.displayName;

        try {

            ResultSet rsUsers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            assertFalse(!rsUsers.isBeforeFirst());

            /*if (!rsUsers.isBeforeFirst() ) {
                return false;
            }else {
                return true;
            }*/


        } catch (Exception e) {
            e.printStackTrace();


        }

    }

    @Test
    public void checkAddedValue(){
        String selectStmt = "SELECT * " +
                "FROM users " +
                "WHERE email =" + this.email +
                "AND password =" + this.password +
                "AND display_name =" + this.displayName;

        try {

            ResultSet rsUsers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            Users user = null;

            if (rsUsers.next()) {

                user = new Users();

                user.setUserId(rsUsers.getInt("user_id"));
                user.setEmail(rsUsers.getString("email"));
                user.setPassword(rsUsers.getString("password"));
                user.setDisplayName(rsUsers.getString("display_name"));
                InputStream x = rsUsers.getBinaryStream("avatar");
                try {
                    if (x == null) {
                        user.setAvatar(null);
                    } else {
                        BufferedImage bImageFromConvert = ImageIO.read(x);
                        Image image = SwingFXUtils.toFXImage(bImageFromConvert, null);
                        javafx.scene.image.ImageView imageView = new javafx.scene.image.ImageView(image);
                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);
                        user.setAvatar(imageView);
                    }
                } catch (IOException e) {

                }
            }

            assertTrue(this.email == user.getEmail().toString() &&
                    this.password == user.getPassword().toString() &&
                    this.displayName == user.getDisplayName().toString());

            /*if(this.email == user.getEmail().toString() && this.password == user.getPassword().toString() &&
                    this.displayName == user.getDisplayName().toString()){
                return true;
            }else {
                return false;
            }*/


        } catch (Exception e) {
            e.printStackTrace();


        }

    }
}
