package zespolowe.model;

import javafx.beans.property.*;
import java.sql.Date;

public class Users {

    private IntegerProperty user_id;
    private StringProperty email;
    private StringProperty password;
    private StringProperty display_name;

    public Users(){
        this.user_id = new SimpleIntegerProperty();
        this.email = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.display_name = new SimpleStringProperty();
    }

    //

    public int getUserId() { return user_id.get(); }

    public void setUserId(int userId) { this.user_id.set(userId); }

    public  IntegerProperty userIdProperty() { return user_id; }

    //

    public String getEmail() { return email.get(); }

    public void setEmail(String email) { this.email.set(email); }

    public  StringProperty emailProperty() { return email; }

    //

    public String getPassword() { return password.get(); }

    public void setPassword(String password) { this.password.set(password); }

    public  StringProperty passwordProperty() { return password; }

    //

    public String getDisplayName() { return display_name.get(); }

    public void setDisplay_name(String displayName) { this.display_name.set(displayName); }

    public  StringProperty displayNameProperty() { return display_name; }
}
