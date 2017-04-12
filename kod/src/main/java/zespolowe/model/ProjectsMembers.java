package zespolowe.model;

import javafx.beans.property.*;
import java.sql.Date;

public class ProjectsMembers {

    private IntegerProperty projectMember_id;
    private IntegerProperty project_id;
    private IntegerProperty user_id;
    private IntegerProperty admin;

    public ProjectsMembers(){
        this.projectMember_id = new SimpleIntegerProperty();
        this.project_id = new SimpleIntegerProperty();
        this.user_id = new SimpleIntegerProperty();
        this.admin = new SimpleIntegerProperty();
    }

    //

    public int getProjectMemberId() {
        return projectMember_id.get();
    }

    public void setProjectMemberId(int projectMemberId){
        this.projectMember_id.set(projectMemberId);
    }

    public IntegerProperty projectMemberIdProperty(){
        return projectMember_id;
    }

    //

    public int getProjectId() { return project_id.get(); }

    public void setProjectId(int projectId) { this.project_id.set(projectId); }

    public  IntegerProperty projectIdProperty() { return project_id; }

    //

    public int getUserId() {
        return user_id.get();
    }

    public void setUserId(int userId){
        this.user_id.set(userId);
    }

    public IntegerProperty userIdProperty(){
        return user_id;
    }

    //

    public int getAdmin() {
        return admin.get();
    }

    public void setAdmin(int admin){
        this.admin.set(admin);
    }

    public IntegerProperty adminProperty(){
        return admin;
    }


}
