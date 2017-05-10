package Components.Main;

import java.awt.*;
import java.awt.Button;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import Service.Session;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.*;


import static model.ProjectsMembersDAO.searchProjectMembers;

public class MainController implements Initializable {

    private SimpleStringProperty clickedProject = new SimpleStringProperty();
    private Projects currentProject;
    private ObservableList<Projects> userProjects;
    private ObservableList<Tasks> projectTasks;
    private ObservableList<Users> projectMembers;
    
    @FXML
    private Label label;
    @FXML
    private Label userName;
    @FXML
    private ComboBox<String> projects;
    @FXML
    private Pane parentpane;

    
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

                    try {

                        currentProject = ProjectsDAO.searchProject(newValue);
                        projectTasks = TasksDAO.getTaskById(currentProject.getProjectId());
                        int x = 15;
                        int y = 40;

                        for (int i = 0; i <=1 ; i++){

                            Pane task1 = new Pane();
                            task1.setLayoutX(x);
                            task1.setLayoutY(y);
                            task1.setPrefHeight(50);
                            task1.setPrefWidth(660);
                            task1.setStyle("-fx-background-color: darkgrey;");
                            task1.setId(" "+i);
                        }

                        projectMembers = projectMembers();

                        for (Users pU : projectMembers){
                            System.out.println(pU.getDisplayName());
                            //TODO
                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });


            }catch (Exception e){
                e.printStackTrace();
            }





        // TODO
    }
    
}
