package Service;

import java.util.ArrayList;

public class Accounts {
    public boolean authorize(String login, String password) {
        ArrayList<Object[]> authorizeDbResult = Database.selectQuery("SELECT email FROM Users WHERE email='" + login + "' AND password='" + password + "'");
        return authorizeDbResult.size() == 1;
    }
}
