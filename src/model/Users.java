package model;

import javafx.beans.property.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.sql.Date;

public class Users {

    private IntegerProperty user_id;
    private StringProperty email;
    private StringProperty password;
    private StringProperty display_name;
    private SimpleObjectProperty<ImageView> avatar;
    private SimpleObjectProperty<Date> dateAndTime;

    public Users() {
        this.user_id = new SimpleIntegerProperty();
        this.email = new SimpleStringProperty();
        this.password = new SimpleStringProperty();
        this.display_name = new SimpleStringProperty();
        this.avatar = new SimpleObjectProperty<>();
        this.dateAndTime = new SimpleObjectProperty<>();
    }

    //
    public Date getProjectDateAndTime() {
        return dateAndTime.get();
    }

    public void setProjectDateAndTime(Date dateAndTime){
        this.dateAndTime.set(dateAndTime);
    }

    public ObjectProperty<Date> projectDateAndTimeProperty(){
        return this.dateAndTime;
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

    public void setDisplayName(String displayName) { this.display_name.set(displayName); }

    public  StringProperty displayNameProperty() { return display_name; }

    //

    public ImageView getAvatar() {
        if (avatar.get() == null) {
            return new ImageView(new Image("images/defaultAvatar.png"));
        }
        return avatar.get();
    }

    public void setAvatar(ImageView avatar) { this.avatar.set(avatar); }

    public SimpleObjectProperty avatarProperty() { return avatar; }

    @Override
    public String toString() {
        return this.getDisplayName();
    }
}
