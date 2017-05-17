package Components.Splash;

import Main.ParentsList;
import Main.ParentsLoader;
import Utilize.DatabaseHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.awt.*;

/**
 * Created by mjaskot on 2017-05-17.
 */
public class SplashController {

    public void openLogin(Stage stage) throws Exception{
        DatabaseHandler.setDatabaseUrl("localhost");
        DatabaseHandler.setDatabaseName("programowaniegrupowe");
        DatabaseHandler.setUsername("mjaskot");
        DatabaseHandler.setPassword("sushijestsmaczne");
        stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.login)));
        stage.show();
    }
    public void openRegister(Stage stage) throws Exception{
        stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.register)));
        stage.show();
    }
}
