package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utilize.DatabaseHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TasksDAO {

    private static ObservableList<Tasks> getTasksList(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<Tasks> tasksList = FXCollections.observableArrayList();

        while (rs.next()) {
            Tasks task = new Tasks();

            task.setTaskId(rs.getInt("task_id"));
            task.setProjectId(rs.getInt("project_id"));
            task.setUserId(rs.getInt("user_id"));
            task.setDescripition(rs.getString("description"));
            task.setState(rs.getInt("state"));

            tasksList.add(task);
        }

        return tasksList;
    }

    public static ObservableList<Tasks> getAllTasks () throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * FROM tasks";

        try {

            ResultSet rsTasks = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Tasks> tasksList = getTasksList(rsTasks);

            return tasksList;

        } catch (Exception e) {
            System.out.println("Nie udało się pobrać zadań: " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static ObservableList<Tasks> searchTasks (int data) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM tasks " +
                "WHERE task_id =" + data +
                "OR project_id =" + data +
                "OR user_id =" + data +
                "OR state =" + data;

        try {
            ResultSet rsTasks = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Tasks> tasksList = getTasksList(rsTasks);

            return tasksList;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania zadań " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static ObservableList<Tasks> searchTasks (String data) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM tasks " +
                "WHERE description =" + data;

        try {
            ResultSet rsTasks = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Tasks> tasksList = getTasksList(rsTasks);

            return tasksList;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania zadań " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static ObservableList<Tasks> searchTasks (Date data) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM tasks " +
                "WHERE dateAndTime =" + data;

        try {
            ResultSet rsTasks = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Tasks> tasksList = getTasksList(rsTasks);

            return tasksList;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania zadań " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static ObservableList<Tasks> getTaskById(int id) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM tasks " +
                "WHERE project_id =" + id;

        try {
            ResultSet rsTasks = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Tasks> tasksList = getTasksList(rsTasks);

            return tasksList;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania zadań " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static void insertTask (int projectId, int userId, String description, int state) throws SQLException, ClassNotFoundException {

        String updateStmt =
                "INSERT INTO tasks" +
                        "(project_id, user_id, description, state)" +
                        "VALUES" +
                        "( " +projectId+ "," +userId+ ",'" +description+ "'," +state+ ")";

        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (Exception e) {
            System.out.print("Nie udało się dodać zadania, nie patrz na stack trace bo dostaniesz raka " + e);
            e.printStackTrace();
        }
    }

    public static void deleteTask (int id) throws SQLException, ClassNotFoundException {

        String updateStmt =
                "DELETE FROM tasks " +
                        "WHERE task_id =" + id;
        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Nie udało się usunąć zadania: " + e);
            e.printStackTrace();
        }
    }

    public static void deleteTaskByDescription (String desc) throws SQLException, ClassNotFoundException {

        String updateStmt =
                "DELETE FROM tasks " +
                        "WHERE description = '"+ desc +"';";
        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Nie udało się usunąć zadania: " + e);
            e.printStackTrace();
        }
    }
}
