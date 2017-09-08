package model;

import Service.Session;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utilize.DatabaseHandler;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.stream.Stream;

public class UsersDAO {

    private static ObservableList<Users> getUsersList(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<Users> usersList = FXCollections.observableArrayList();

        while (rs.next()) {
            Users user = new Users();

            user.setUserId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setDisplayName(rs.getString("display_name"));
            InputStream x = rs.getBinaryStream("avatar");
            try {
                BufferedImage bImageFromConvert = ImageIO.read(x);
                user.setAvatar(SwingFXUtils.toFXImage(bImageFromConvert, null));
            } catch (IOException e) {
                e.printStackTrace();
            }
            usersList.add(user);
        }

        return usersList;
    }

    private static Users getUserFromResultSet(ResultSet rs) throws SQLException {

        Users user = null;

        if (rs.next()) {

            user = new Users();

            user.setUserId(rs.getInt("user_id"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setDisplayName(rs.getString("display_name"));
            InputStream x = rs.getBinaryStream("avatar");
            try {
                BufferedImage bImageFromConvert = ImageIO.read(x);
                user.setAvatar(SwingFXUtils.toFXImage(bImageFromConvert, null));

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return user;
    }

    public static ObservableList<Users> getAllUsers () throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * FROM users";

        try {

            ResultSet rsTasks = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Users> usersList = getUsersList(rsTasks);

            return usersList;

        } catch (Exception e) {
            System.out.println("Nie udało się pobrać użytkowników: " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static Users searchUsers (int data) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM users " +
                "WHERE user_id =" + data;

        try {
            ResultSet rsUsers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            Users user = getUserFromResultSet(rsUsers);

            return user;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania użytkowników " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static Users searchUsers (String data) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM users " +
                "WHERE display_name = '"+ data +"';";

        try {
            ResultSet rsUsers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            Users user = getUserFromResultSet(rsUsers);

            return user;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania użytkowników " + e);
            e.printStackTrace();

            return null;
        }
    }

    /*public static ObservableList<Users> searchUsers (String data) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM users " +
                "WHERE email =" + data +
                "OR display_name =" + data;

        try {
            ResultSet rsUsers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Users> usersList = getUsersList(rsUsers);

            return usersList;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania użytkowników " + e);
            e.printStackTrace();

            return null;
        }
    }*/

    public static void updateUser (String email, String password, String displayName) throws  SQLException, ClassNotFoundException {

        String updateStm =
                    "UPDATE Users SET email= '"+email+"', password='"+password+"', display_name='"+displayName+"' WHERE user_id="+Session.getUserId();
        try {
            DatabaseHandler.databaseExecuteUpdate(updateStm);
        }catch (Exception e){
            System.out.print("Nie udało się edytować użytkownika, nie patrz na stack trace bo dostaniesz raka " + e);
            e.printStackTrace();
        }
    }

    public static void insertUser (String email, String password, String displayName) throws SQLException, ClassNotFoundException {

        String updateStmt =
            "INSERT INTO users" +
                        "(email, password, display_name)" +
                        "VALUES" +
                        "( '" +email+ "','" +password+ "','" +displayName+ "')";

        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (Exception e) {
            System.out.print("Nie udało się dodać użytkownika, nie patrz na stack trace bo dostaniesz raka " + e);
            e.printStackTrace();
        }
    }

    public static void deleteUser (int id) throws SQLException, ClassNotFoundException {

        String updateStmt =
                "DELETE FROM users WHERE user_id =" + id;
        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Nie udało się usunąć zadania: " + e);
            e.printStackTrace();
        }
    }
}
