package Service;

import javafx.beans.property.*;
import javafx.scene.image.ImageView;

public class Session {

    private static IntegerProperty user_id;
    private static StringProperty email;
    private static SimpleObjectProperty<ImageView> avatar;
    private static StringProperty display_name;

    //

    public static int getUserId() { return user_id.get(); }

    public static void setUserId(int userId) {
        Session.user_id = new SimpleIntegerProperty();
        Session.user_id.set(userId);
    }

    public static IntegerProperty userIdProperty() { return user_id; }

    //

    public static String getEmail() { return email.get(); }

    public static void setEmail(String email) {
        Session.email = new SimpleStringProperty();
        Session.email.set(email);
    }

    public  static StringProperty emailProperty() { return email; }

    //

    public static String getDisplayName() { return display_name.get(); }

    public static void setDisplayName(String displayName) {
        Session.display_name = new SimpleStringProperty();
        Session.display_name.set(displayName);
    }

    public  StringProperty displayNameProperty() { return display_name; }

    //

    public static ImageView getAvatar() { return avatar.get(); }

    public static void setAvatar(ImageView avatar) {
        Session.avatar = new SimpleObjectProperty<ImageView>();
        Session.avatar.set(avatar);
    }

    public static SimpleObjectProperty avatarProperty() { return avatar; }

}

