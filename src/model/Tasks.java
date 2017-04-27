package model;


import javafx.beans.property.*;
import java.sql.Date;

public class Tasks {

    private IntegerProperty task_id;
    private IntegerProperty project_id;
    private IntegerProperty user_id;
    private StringProperty  descripition;
    private SimpleObjectProperty<Date> dateAndTime;
    private IntegerProperty state;

    public Tasks(){
        this.task_id = new SimpleIntegerProperty();
        this.project_id = new SimpleIntegerProperty();
        this.user_id = new SimpleIntegerProperty();
        this.descripition = new SimpleStringProperty();
        this.dateAndTime = new SimpleObjectProperty<Date>();
        this.state = new SimpleIntegerProperty();
    }

    //

    public int getTaskId() { return task_id.get(); }

    public void setTaskId(int taskId) { this.task_id.set(taskId); }

    public  IntegerProperty taskIdProperty() { return task_id; }

    //

    public int getProjectId() { return project_id.get(); }

    public void setProjectId(int projectId) { this.project_id.set(projectId); }

    public  IntegerProperty projectIdProperty() { return project_id; }

    //

    public int getUserId() { return user_id.get(); }

    public void setUserId(int userId) { this.user_id.set(userId); }

    public  IntegerProperty userIdProperty() { return user_id; }

    //

    public String getDescription() { return descripition.get(); }

    public void setDescripition(String descripition) { this.descripition.set(descripition); }

    public  StringProperty descriptionProperty() { return descripition; }

    //

    public Object getDateAndTime() { return dateAndTime.get(); }

    public void setDateAndTime(Date dateAndTime) { this.dateAndTime.set(dateAndTime); }

    public  SimpleObjectProperty<Date> dateAndTimeProperty() { return dateAndTime; }

    //

    public int getState() { return state.get(); }

    public void setState(int state) { this.state.set(state); }

    public  IntegerProperty stateProperty() { return state; }
}
