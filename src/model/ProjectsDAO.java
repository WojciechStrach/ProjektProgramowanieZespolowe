package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utilize.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import org.json.*;

public class ProjectsDAO {

    private static Projects getProjectFromResultSet(ResultSet rs) throws SQLException {
        Projects project = null;

        if (rs.next()) {

            project = new Projects();

            project.setProjectId(rs.getInt("project_id"));
            project.setTitle(rs.getString("title"));
            project.setProjectDateAndTime(rs.getDate("dateAndTime"));

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

    public static HashMap<String, HashMap<String, Integer>> getProjectsWithDetails() {
        String selectStmt =
            "SELECT title,  CONCAT('{\"userTasks\": [', GROUP_CONCAT(json_array(display_name, countY)), ']}') as userTasks FROM" +
            "    (" +
            "        SELECT" +
            "            title," +
            "            display_name," +
            "            count(*) AS countY" +
            "        FROM Tasks AS t" +
            "            INNER JOIN Projects AS p ON p.project_id = t.project_id" +
            "            INNER JOIN ProjectsMembers AS pm ON pm.project_id = t.project_id AND pm.user_id = t.user_id" +
            "            INNER JOIN Users AS u ON pm.user_id = u.user_id" +
            "        GROUP BY display_name" +
            "    ) AS x " +
            "GROUP BY title";

        try {
            HashMap<String, HashMap<String, Integer>> projectWithDetails = new HashMap<>();

            ResultSet rsProject = DatabaseHandler.databaseExecuteQuery(selectStmt);
            while(rsProject.next()) {
                String projectTitle = rsProject.getString("title");

                HashMap<String, Integer> projectUsersAndTaskCount = new HashMap<>();
                JSONArray projectUsersAndTaskCountArray = new JSONObject(rsProject.getString("userTasks")).getJSONArray("userTasks");

                for (int i = 0; i < projectUsersAndTaskCountArray.length(); i++) {
                    JSONArray projectUserAndTaskCount = projectUsersAndTaskCountArray.getJSONArray(i);
                    String userName = (String)projectUserAndTaskCount.get(0);
                    Integer userTasksCount = (Integer)projectUserAndTaskCount.get(1);
                    projectUsersAndTaskCount.put(userName, userTasksCount);
                }

                projectWithDetails.put(projectTitle, projectUsersAndTaskCount);

            }
            return projectWithDetails;

        } catch (Exception e) {
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
            project.setProjectDateAndTime(rs.getDate("dateAndTime"));

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

        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);

        String updateStmt =
                        "INSERT INTO projects" +
                        "(title, dateAndTime)" +
                        "VALUES" +
                        "('"+title+"','" +currentTime+ "')";

        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (Exception e) {
            System.out.print("Nie udało się dodać projektu, nie patrz na stack trace bo dostaniesz raka " + e);
            e.printStackTrace();
        }
    }

}
