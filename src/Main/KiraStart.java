package Main;

import Models.User;
import Utilize.DatabaseHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KiraStart extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseHandler.setDatabaseUrl("localhost");
        DatabaseHandler.setDatabaseName("programowaniegrupowe");
        DatabaseHandler.setUsername("chujwieco");
        DatabaseHandler.setPassword("sushijestsmaczne");
        stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.login)));
        stage.show();


    }

    public static void main(String[] args) { launch(args); }
    
}
