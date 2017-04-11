package Main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KiraStart extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.login)));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
