package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utilize.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;


public class ProjectsDAO {

    private static Projects getProjectFromResultSet(ResultSet rs) throws SQLException {
        Projects project = null;

        if (rs.next()) {

            project = new Projects();

            project.setProjectId(rs.getInt("project_id"));
            project.setTitle(rs.getString("title"));

        }
        return project;
    }

    public static Projects searchProject (String title) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                            "FROM projects " +
                            "WHERE title = '"+ title +"';";

        try {
            ResultSet rsProject = DatabaseHandler.databaseExecuteQuery(selectStmt);

            Projects project = getProjectFromResultSet(rsProject);

            return project;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania frazy " + title + "Błąd: " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static Projects searchProject (int id) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                            "FROM projects " +
                            "WHERE project_id =" + id;

        try {
            ResultSet rsProject = DatabaseHandler.databaseExecuteQuery(selectStmt);

            Projects project = getProjectFromResultSet(rsProject);

            return project;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania projektu o identyfikatorze " + id + "Błąd: " + e);
            e.printStackTrace();

            return null;
        }
    }

    private static ObservableList<Projects> getProjectsList(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<Projects> projectsList = FXCollections.observableArrayList();

        while (rs.next()) {
            Projects project = new Projects();

            project.setProjectId(rs.getInt("PROJECT_ID"));
            project.setTitle(rs.getString("TITLE"));

            projectsList.add(project);
        }

        return projectsList;
    }

    public static ObservableList<Projects> getAllProjects () throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * FROM projects";

        try {

            ResultSet rsProjects = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<Projects> projectsList = getProjectsList(rsProjects);

            return projectsList;

        } catch (Exception e) {
            System.out.println("Nie udało się pobrać listy projektów: " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static void deleteProject (String title) throws SQLException, ClassNotFoundException {

        String updateStmt =
                        "DELETE FROM projects" +
                        "WHERE title ='"+ title +"';";

        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Nie udało się usunąć projektu: " + e);
            e.printStackTrace();
        }
    }

    public static void deleteProject (int id) throws SQLException, ClassNotFoundException {

        String updateStmt =
                        "DELETE FROM projects" +
                        "WHERE project_id =" + id;
        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Nie udało się usunąć projektu: " + e);
            e.printStackTrace();
        }
    }

    public static void insertProject (String title) throws SQLException, ClassNotFoundException {

        String updateStmt =
                        "INSERT INTO projects" +
                        "(title)" +
                        "VALUES" +
                        "('"+title+"')";

        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (Exception e) {
            System.out.print("Nie udało się dodać projektu, nie patrz na stack trace bo dostaniesz raka " + e);
            e.printStackTrace();
        }
    }

}
