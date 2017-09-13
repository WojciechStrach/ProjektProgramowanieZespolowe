package Tests;

import Utilize.DatabaseHandler;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import model.Projects;
import model.ProjectsDAO;
import model.Users;
import model.UsersDAO;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * Created by f3cro on 13.09.2017.
 */
public class AddProjectTest {

    String projectTitle = "projekttwojegostarego";

    @Test
    public void addToDatabaseCheck() {
        try {

            ProjectsDAO.insertProject(this.projectTitle);
            assertTrue(true);

        }catch (Exception e){
            e.printStackTrace();
            assertTrue(false);
        }

    }

    @Test
    public void checkIfExistAfterAdd() {

        String selectStmt = "SELECT * " +
                "FROM projects " +
                "WHERE title = '"+ this.projectTitle +"';";

        try {

            ResultSet rsProjects = DatabaseHandler.databaseExecuteQuery(selectStmt);

            assertFalse(!rsProjects.isBeforeFirst());

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
                "FROM projects " +
                "WHERE title = '"+ this.projectTitle +"';";

        try {

            ResultSet rsProjects = DatabaseHandler.databaseExecuteQuery(selectStmt);

            Projects project = null;

            if (rsProjects.next()) {

                project = new Projects();

                project.setProjectId(rsProjects.getInt("project_id"));
                project.setTitle(rsProjects.getString("title"));
                project.setProjectDateAndTime(rsProjects.getDate("dateAndTime"));
            }

            String cond = project.getTitle().toString();

            assertFalse(cond == this.projectTitle);

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
