package Main;

import Utilize.DatabaseHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KiraStart extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        DatabaseHandler.setDatabaseUrl("localhost");
        DatabaseHandler.setDatabaseName("projektprogramowaniezespolowe");
        DatabaseHandler.setUsername("root");
        DatabaseHandler.setPassword("wojtek123");
        stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.login)));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
