package zespolowe.model;

import javafx.beans.property.*;
import java.sql.Date;

public class Projects {

    private IntegerProperty project_id;
    private StringProperty title;

    public Projects(){
        this.project_id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
    }

    //

    public int getProjectId() {
        return project_id.get();
    }

    public void setProjectId(int projectId){
        this.project_id.set(projectId);
    }

    public IntegerProperty projectIdProperty(){
        return project_id;
    }

    //

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title){
        this.title.set(title);
    }

    public StringProperty titleProperty(){
        return title;
    }


}
