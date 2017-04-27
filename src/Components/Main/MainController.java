package Components.Main;

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
import model.*;


import static model.ProjectsMembersDAO.searchProjectMembers;

public class MainController implements Initializable {

    private SimpleStringProperty clickedProject = new SimpleStringProperty();
    ObservableList<Projects> userProjects;
    ObservableList<Tasks> projectTasks;
    
    @FXML
    private Label label;
    @FXML
    private Label userName;
    @FXML
    private ComboBox<String> projects;
    
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
        }

        return matchedProjects;
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            try {

                userName.setText(Session.getDisplayName());

                clickedProject.bindBidirectional(projects.valueProperty());

                clickedProject.addListener((observable, oldValue, newValue) -> {
                    System.out.println(newValue);

                    try {

                        Projects getProject = ProjectsDAO.searchProject(newValue);
                        projectTasks = TasksDAO.getTaskById(getProject.getProjectId());

                        for (Tasks pT : projectTasks) {
                            System.out.println(pT.getDescription());
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });

                userProjects = matchProjects();

                for (Projects uP : userProjects) {
                    projects.getItems().add(uP.getTitle());
                }

                clickedProject.bindBidirectional(projects.valueProperty());

                clickedProject.addListener((observable, oldValue, newValue) -> {
                    System.out.println(newValue);

                    try {

                        Projects getProject = ProjectsDAO.searchProject(newValue);
                        projectTasks = TasksDAO.getTaskById(getProject.getProjectId());

                        for (Tasks pT : projectTasks) {
                            System.out.println(pT.getDescription());
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
