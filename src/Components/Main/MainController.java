package Components.Main;

import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import Models.User;
import Service.Session;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import model.*;


import static model.ProjectsMembersDAO.searchProjectMembers;

public class MainController implements Initializable {

    private SimpleStringProperty clickedProject = new SimpleStringProperty();
    private Projects currentProject;
    private boolean isProjectSet = false;
    private ObservableList<Projects> userProjects;
    private ObservableList<Tasks> projectTasks;
    private ObservableList<Users> projectMembers;
    private int numberOfUsersObjects = 0;
    private int numberOfTasksObjects = 0;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);
    private ScheduledFuture<?> exec;
    private boolean executorStatus = false;

    
    @FXML
    private Label label;
    @FXML
    private Label userName;
    @FXML
    private ComboBox<String> projects;
    @FXML
    private ListView<String> projectTaskList;
    @FXML
    private ListView<String> projectUserList;
    @FXML
    private Label projectName;
    @FXML
    private Button addTask;
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    }
    
    private ObservableList<Projects> matchProjects(){
        ObservableList<Projects> matchedProjects = FXCollections.observableArrayList();
        try {
            ObservableList<ProjectsMembers> membersProjects = ProjectsMembersDAO.searchProjectMembers(Session.getUserId());
            ObservableList<ProjectsMembers> matchedMembers = FXCollections.observableArrayList();

            for(ProjectsMembers member : membersProjects){
                int rowUserId = member.getUserId();
                int sessionId = Session.getUserId();
                if (rowUserId == sessionId ){
                    matchedMembers.add(member);
                }
            }

            List<Integer> projectsIds = new ArrayList<>();
            for(ProjectsMembers member : matchedMembers){
                projectsIds.add(member.getProjectId());
            }

            for (int i=0; i<projectsIds.size(); i++){
                matchedProjects.add(ProjectsDAO.searchProject(projectsIds.get(i)));
            }


        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return matchedProjects;
    }

    private ObservableList<Users> projectMembers(){
        ObservableList<Users> matchedMembers = FXCollections.observableArrayList();
        try {
            ObservableList<ProjectsMembers> membersProject = ProjectsMembersDAO.searchProjectMembersByProjectId
                    (currentProject.getProjectId());

            List<Integer> usersIds = new ArrayList<>();
            for (ProjectsMembers member : membersProject){
                usersIds.add(member.getUserId());
            }

            for (int i=0; i<usersIds.size(); i++){
                matchedMembers.add(UsersDAO.searchUsers(usersIds.get(i)));
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return matchedMembers;

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            try {

                userName.setText(Session.getDisplayName());

                clickedProject.bindBidirectional(projects.valueProperty());

                userProjects = matchProjects();

                for (Projects uP : userProjects) {
                    projects.getItems().add(uP.getTitle());
                }

                clickedProject.bindBidirectional(projects.valueProperty());


                clickedProject.addListener((observable, oldValue, newValue) -> {
                    System.out.println(newValue);
                    projectName.setText(newValue);
                    isProjectSet = true;

                    if(executorStatus){
                        exec.cancel(true);
                    }

                    projectUserList.getItems().clear();
                    projectTaskList.getItems().clear();

                    numberOfTasksObjects = 0;
                    numberOfUsersObjects = 0;


                    try {

                        currentProject = ProjectsDAO.searchProject(newValue);

                        Runnable refreshValues = new Runnable() {
                            public void run() {
                                try {

                                    projectTasks = TasksDAO.getTaskById(currentProject.getProjectId());

                                    if(numberOfTasksObjects < projectTasks.size()){
                                        for(int i = numberOfTasksObjects; i<projectTasks.size(); i++){
                                            Tasks temp = projectTasks.get(i);
                                            projectTaskList.getItems().add(temp.getDescription());

                                        }
                                        numberOfTasksObjects = projectTasks.size();
                                    }

                                    if(numberOfTasksObjects > projectTasks.size()){
                                        numberOfTasksObjects =  projectTasks.size();
                                        ObservableList<String> temp = projectTaskList.getItems();
                                        for(int j=0; j<temp.size(); j++){
                                            boolean isPresent = false;
                                            for (int k=0; k<projectTasks.size(); k++){
                                                if(temp.get(j).equals(projectTasks.get(k).getDescription())){
                                                    isPresent = true;
                                                }
                                            }
                                            if (isPresent == false){
                                                System.out.println(temp.get(j));
                                                projectTaskList.getItems().clear();
                                                for (int l=0; l<projectTasks.size(); l++){
                                                    Tasks removeTemp = projectTasks.get(l);
                                                    projectTaskList.getItems().add(removeTemp.getDescription());
                                                }

                                            }
                                        }
                                    }

                                    for (Tasks pT : projectTasks) {
                                        System.out.println(pT.getDescription());


                                    }


                                    projectMembers = projectMembers();

                                    if(numberOfUsersObjects < projectMembers.size()){
                                        for(int i = numberOfUsersObjects; i<projectMembers.size(); i++){
                                            Users temp = projectMembers.get(i);
                                            projectUserList.getItems().add(temp.getDisplayName());

                                        }
                                        numberOfUsersObjects = projectMembers.size();
                                    }

                                    if(numberOfUsersObjects > projectMembers.size()){
                                        numberOfTasksObjects = projectMembers.size();
                                        projectUserList.getItems().clear();
                                        for (int j=0; j<projectMembers.size(); j++){
                                            Users userRemoveTemp = projectMembers.get(j);
                                            projectUserList.getItems().add(userRemoveTemp.getDisplayName());
                                        }
                                    }

                                    for (Users pU : projectMembers) {
                                        System.out.println(pU.getDisplayName());

                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        };


                        exec = executor.scheduleAtFixedRate(refreshValues, 0, 500, TimeUnit.MILLISECONDS);
                        executorStatus = true;


                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });

                addTask.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(isProjectSet) {
                            TextInputDialog dialog = new TextInputDialog("Wpisz treść zadania");
                            dialog.setTitle("Dodawanie zadania");
                            dialog.setHeaderText("Wypełnij poniższe pole aby dodać nowe zadanie");
                            dialog.setContentText("Wpisz treść zadania");

                            Optional<String> result = dialog.showAndWait();
                            if (result.isPresent()) {
                                try {
                                    TasksDAO.insertTask(currentProject.getProjectId(), Session.getUserId(), result.get(), 1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd");
                            alert.setHeaderText("Nie wybrano projektu");
                            alert.setContentText("Aby dodać zadanie najpierw wybierz projekt z menu");

                            alert.showAndWait();
                        }
                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }





        // TODO
    }
    
}
