package model;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utilize.DatabaseHandler;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Random;

public class TasksDAO {

    private static ObservableList<Tasks> getTasksList(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<Tasks> tasksList = FXCollections.observableArrayList();

        while (rs.next()) {
            Tasks task = new Tasks();

            task.setTaskId(rs.getInt("task_id"));
            task.setProjectId(rs.getInt("project_id"));
            task.setUserId(rs.getInt("user_id"));
            task.setAssignedUserId(rs.getInt("assigned_user_id"));
            if (rs.wasNull()) {
                task.setAssignedUserId(null);
            }
            task.setDescripition(rs.getString("description"));
            task.setDateAndTime(rs.getDate("dateAndTime"));
            task.setState(TaskState.valueOf(rs.getString("state")));
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
            e.printStackTrace();
            System.out.println("Nie udało się pobrać zadań: ");
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

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        String updateStmt =
                "INSERT INTO tasks" +
                        "(project_id, user_id, description, dateAndTime, state)" +
                        "VALUES" +
                        "( " +projectId+ "," +userId+ ",'" +description+ "','" +currentTime+ "'," +state+ ")";

        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (Exception e) {
            System.out.print("Nie udało się dodać zadania, nie patrz na stack trace bo dostaniesz raka " + e);
            e.printStackTrace();
        }
    }

    public static void updateTask (int taskId, String description, String state, Integer assigned_user_id) throws SQLException, ClassNotFoundException {

        String updateStmt =
                "UPDATE tasks " +
                    "SET description = '" + description + "'" +
                    ", state = '" + state + "'" +
                    ", assigned_user_id = " + assigned_user_id +
//                    (assigned_user_id != null ? ", assigned_user_id = '" + assigned_user_id + "'" : "") +
                " WHERE task_id = " + taskId;
        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLIntegrityConstraintViolationException e) {
            throw new SQLIntegrityConstraintViolationException();
        } catch (Exception e) {
            System.out.print("Nie udało się zmienić zadania, nie patrz na stack trace bo dostaniesz raka (przy dodawaniu taska podobno można dostać raka, zostawie tu też na wszelki wypadek, ufam kolegom z teamu)" + e);
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
