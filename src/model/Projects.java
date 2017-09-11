package model;

import javafx.beans.property.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class Projects {

    private IntegerProperty project_id;
    private StringProperty title;
    private SimpleObjectProperty<Date> dateAndTime;

    public Projects(){
        this.project_id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.dateAndTime = new SimpleObjectProperty<>();
    }

    //
    public Date getProjectDateAndTime() {
        return dateAndTime.get();
    }

    public void setProjectDateAndTime(Date dateAndTime){
        this.dateAndTime.set(dateAndTime);
    }

    public ObjectProperty<Date> projectDateAndTimeProperty(){
        return this.dateAndTime;
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
