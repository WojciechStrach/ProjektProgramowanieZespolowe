package zespolowe.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import zespolowe.Utilize.DatabaseHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectsMembersDAO {

    private static ObservableList<ProjectsMembers> getProjectsMembersList(ResultSet rs) throws SQLException, ClassNotFoundException {

        ObservableList<ProjectsMembers> projectsMebersList = FXCollections.observableArrayList();

        while (rs.next()) {
            ProjectsMembers projectMember = new ProjectsMembers();

            projectMember.setProjectMemberId(rs.getInt("projectMember_id"));
            projectMember.setProjectId(rs.getInt("project_id"));
            projectMember.setUserId(rs.getInt("user_id"));
            projectMember.setAdmin(rs.getInt("admin"));

            projectsMebersList.add(projectMember);
        }

        return projectsMebersList;
    }


}
