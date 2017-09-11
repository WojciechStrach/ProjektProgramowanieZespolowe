package Main;

import Reports.ProjectsReport;
import Utilize.DatabaseHandler;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class KiraStart extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(ParentsLoader.getParent(ParentsList.splash));
        scene.getStylesheets().add("Components/Main/style.css");
        stage.setScene(scene);
        stage.show();
    }
    /*public static void main(String[] args) {

        launch(args);
    }*/
}
