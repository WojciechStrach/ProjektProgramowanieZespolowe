package Models;

import Service.Session;
import javafx.beans.property.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    public static User parseUserDataSetToUserModel(ResultSet resultSet) {
        User user = new User();
        try {
            Session.setUserId(resultSet.getInt("user_id"));
            Session.setEmail(resultSet.getString("email"));
            Session.setDisplayName(resultSet.getString("display_name"));
            user.setEmail(resultSet.getString("email"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    private IntegerProperty user_id = new SimpleIntegerProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty password = new SimpleStringProperty();
    private StringProperty display_name = new SimpleStringProperty();

    public int getUserId() { return user_id.get(); }
    public void setUserId(int userId) { this.user_id.set(userId); }
    public IntegerProperty userIdProperty() { return user_id; }

    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public  StringProperty emailProperty() { return email; }

    public String getPassword() { return password.get(); }
    public void setPassword(String password) { this.password.set(password); }
    public  StringProperty passwordProperty() { return password; }

    public String getDisplayName() { return display_name.get(); }
    public void setDisplay_name(String displayName) { this.display_name.set(displayName); }
    public  StringProperty displayNameProperty() { return display_name; }
}