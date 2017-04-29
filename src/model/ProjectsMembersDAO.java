package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import Utilize.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectsMembersDAO {

    private static ObservableList<ProjectsMembers> getProjectsMembersList(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<ProjectsMembers> projectsMembersList = FXCollections.observableArrayList();

        while (rs.next()) {
            ProjectsMembers projectMember = new ProjectsMembers();

            projectMember.setProjectMemberId(rs.getInt("projectMember_id"));
            projectMember.setProjectId(rs.getInt("project_id"));
            projectMember.setUserId(rs.getInt("user_id"));
            projectMember.setAdmin(rs.getBoolean("admin"));

            projectsMembersList.add(projectMember);
        }

        return projectsMembersList;
    }

    public static ObservableList<ProjectsMembers> getAllProjectsMembers () throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * FROM projectsmembers";

        try {

            ResultSet rsProjectsMembers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<ProjectsMembers> projectsMembersList = getProjectsMembersList(rsProjectsMembers);

            return projectsMembersList;

        } catch (Exception e) {
            System.out.println("Nie udało się pobrać listy uczestników projektów: " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static ObservableList<ProjectsMembers> searchProjectMembers (int data) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM projectsmembers " +
                "WHERE projectMember_id = " + data + " " +
                "OR project_id = " + data + " " +
                "OR user_id = " + data + " " +
                "OR admin = " + data;

        try {
            ResultSet rsProjectMembers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<ProjectsMembers> projectsMembersList = getProjectsMembersList(rsProjectMembers);

            return projectsMembersList;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania uczestników projektu " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static ObservableList<ProjectsMembers> searchProjectMembersByProjectId (int projectId) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * " +
                "FROM projectsmembers " +
                "WHERE project_id = " + projectId;


        try {
            ResultSet rsProjectMembers = DatabaseHandler.databaseExecuteQuery(selectStmt);

            ObservableList<ProjectsMembers> projectsMembersList = getProjectsMembersList(rsProjectMembers);

            return projectsMembersList;

        } catch (Exception e) {
            System.out.println("Wystąpił błąd podczas wyszukiwania uczestników projektu " + e);
            e.printStackTrace();

            return null;
        }
    }

    public static void insertProjectMember (int projectId, int userId, int admin) throws SQLException, ClassNotFoundException {

        String updateStmt =
                "INSERT INTO projectsmembers" +
                        "(project_id, user_id, admin)" +
                        "VALUES" +
                        "( " +projectId+ "," +userId+ "," +admin+ ")";

        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (Exception e) {
            System.out.print("Nie udało się dodać członka projektu, nie patrz na stack trace bo dostaniesz raka " + e);
            e.printStackTrace();
        }
    }

    public static void deleteProjectMember (int id) throws SQLException, ClassNotFoundException {

        String updateStmt =
                "DELETE FROM projectsmembers" +
                        "WHERE projectMember_id =" + id;
        try {
            DatabaseHandler.databaseExecuteUpdate(updateStmt);
        } catch (SQLException e) {
            System.out.print("Nie udało się usunąć członka projektu: " + e);
            e.printStackTrace();
        }
    }


}
