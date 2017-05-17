package Main;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Created by kamil on 4/12/17.
 */
public class ParentsLoader {
    public static Parent getParent(ParentsList parent) throws IOException {
        switch (parent) {
            case login:
                return FXMLLoader.load(ParentsLoader.class.getResource("../Components/Auth/Login/Login.fxml"));
            case register:
                return FXMLLoader.load(ParentsLoader.class.getResource("../Components/Auth/Register/Register.fxml"));
            case main:
                return FXMLLoader.load(ParentsLoader.class.getResource("../Components/Main/Main.fxml"));
            case splash:
                return FXMLLoader.load(ParentsLoader.class.getResource("../Components/Splash/Splash.fxml"));
            case edit:
                return FXMLLoader.load(ParentsLoader.class.getResource("../Components/Auth/Edit/Edit.fxml"));
            default:
                throw new Error("Not recognized parent: " + parent);
        }
    }
}
