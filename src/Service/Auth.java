package Service;

import Models.User;

import java.util.ArrayList;

public class Auth {

    public boolean authorize(String login, String password) {
        ArrayList<User> authorizeDbResult = new Database<User>().selectQuery("SELECT * FROM Users WHERE email='" + login + "' AND password='" + password + "'", User::parseUserDataSetToUserModel);
        return authorizeDbResult.size() == 1;
    }
}
