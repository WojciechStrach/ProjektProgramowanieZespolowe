package Components.Main;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import Main.ParentsList;
import Main.ParentsLoader;
import Models.User;
import Service.Session;
import com.sun.javafx.tk.Toolkit;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.*;


import javax.swing.*;

import static model.ProjectsMembersDAO.searchProjectMembers;

public class MainController implements Initializable {

    private SimpleStringProperty clickedProject = new SimpleStringProperty();
    private Projects currentProject;
    private boolean isProjectSet = false;
    private ObservableList<Projects> userProjects;
    private ObservableList<Tasks> projectTasks;
    private ObservableList<Users> projectMembers;
    //private ObservableList<String> projectTaskString;
    private int numberOfUsersObjects = 0;
    private int numberOfTasksObjects = 0;
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(0);
    private ScheduledFuture<?> exec;
    private boolean executorStatus = false;


    @FXML
    private ObservableList<String> projectTaskCollection;
    @FXML
    private Label label;
    @FXML
    private Label userName;
    @FXML
    private ComboBox<String> projects;
    @FXML
    private ListView<String> projectTaskList = new ListView<>(projectTaskCollection);
    @FXML
    private ListView<String> projectUserList;
    @FXML
    private Label projectName;
    @FXML
    private Button addTask;
    @FXML
    private Button removeTask;
    @FXML
    private Button addUser;
    @FXML
    private Button editUser;
    @FXML
    private Button newProject;
    @FXML
    private Button addMember;
    @FXML
    private Button removeMember;

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
                    projectTaskCollection.clear();

                    projectTaskList.setItems(projectTaskCollection);

                    numberOfTasksObjects = 0;
                    numberOfUsersObjects = 0;

                    try {

                        currentProject = ProjectsDAO.searchProject(newValue);

                        Runnable refreshValues = new Runnable() {
                            public void run() {
                                try {

                                    projectTasks = TasksDAO.getTaskById(currentProject.getProjectId());

                                    /*for (Tasks pT : projectTasks) {
                                        projectTaskCollection.clear();
                                        System.out.println(projectTaskCollection);
                                        projectTaskCollection.add(pT.getDescription());
                                    }*/

                                    //projectTaskList.setItems(projectTaskCollection);



                                    if(numberOfTasksObjects < projectTasks.size()){
                                        for(int i = numberOfTasksObjects; i<projectTasks.size(); i++){
                                            Tasks temp = projectTasks.get(i);
                                            projectTaskCollection.add(temp.getDescription());

                                        }
                                        numberOfTasksObjects = projectTasks.size();
                                    }

                                    if(numberOfTasksObjects > projectTasks.size()){
                                        //ObservableList<String> temp = projectTaskList.getItems();
                                        for(int j=0; j<projectTaskCollection.size(); j++){
                                            boolean isPresent = false;
                                            for (int k=0; k<projectTasks.size(); k++){
                                                if(projectTaskCollection.get(j).equals(projectTasks.get(k).getDescription())){
                                                    isPresent = true;
                                                }
                                            }
                                            if (isPresent == false){
                                                //System.out.println(temp.get(j));
                                                projectTaskCollection.remove(j);
                                                //projectTaskList
                                                //projectTaskList.setItems(temp);
                                                //projectTaskList.getItems().remove(j);
                                                /*projectTaskList.getItems().clear();
                                                for (Tasks pT : projectTasks){
                                                    projectTaskList.getItems().add(pT.getDescription());
                                                }*/

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
                                        numberOfUsersObjects = projectMembers.size();
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
                        exec = executor.scheduleAtFixedRate(refreshValues, 0, 1, TimeUnit.SECONDS);
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

                /*addUser.setOnAction(new EventHandler<ActionEvent>(){
                    public void handle(ActionEvent){
                        TextInputDialog dialog = new TextInputDialog("Dodaj użytkownika do projektu");
                        dialog.setTitle("Dodawanie użytkownika");
                        dialog.setHeaderText("Wybierz użytkownika z listy aby go dodać");
                    }
                });*/

                removeTask.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        if(isProjectSet){
                            String selected = projectTaskList.getSelectionModel().getSelectedItem();
                            if (selected != null) {
                                try {
                                    TasksDAO.deleteTaskByDescription(selected);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Błąd");
                                alert.setHeaderText("Nie zaznaczono zadania");
                                alert.setContentText("Zaznacz zadanie aby je usunąć");

                                alert.showAndWait();
                            }

                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd");
                            alert.setHeaderText("Nie wybrano projektu");
                            alert.setContentText("Aby usunąć zadanie wybierz projekt z listy a następnie zaznacz zadanie");

                            alert.showAndWait();
                        }
                    }
                });

                editUser.setOnAction(event -> {
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    try {
                        stage.setScene(new Scene(ParentsLoader.getParent(ParentsList.edit)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    stage.show();
                });

                newProject.setOnAction(event -> {
                    TextInputDialog dialog = new TextInputDialog("Podaj nazwę projektu");
                    dialog.setTitle("Nowy projekt");
                    dialog.setHeaderText("Wypełnij poniższe pole aby stworzyć nowy projekt");
                    dialog.setContentText("Wpisz nazwę projektu ");

                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()) {
                        try {
                            ProjectsDAO.insertProject(result.get());
                            Projects temp = ProjectsDAO.searchProject(result.get());
                            ProjectsMembersDAO.insertProjectMember(temp.getProjectId(), Session.getUserId(), 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                addMember.setOnAction(event -> {
                    if(isProjectSet){
                        ProjectsMembers adminCheck = ProjectsMembersDAO.adminGetter(currentProject.getProjectId());
                        int adminCheckId = adminCheck.getUserId();
                        if(adminCheckId == Session.getUserId()){
                            
                        }else {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd");
                            alert.setHeaderText("Brak uprawnień");
                            alert.setContentText("Musisz być właścicielem projektu aby dodawać i usuwać jego członków");

                            alert.showAndWait();
                        }

                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Nie wybrano projektu");
                        alert.setContentText("Aby dodawać i usuwać członków projektu wybierz projekt z listy");

                        alert.showAndWait();
                    }
                });

                removeMember.setOnAction(event -> {
                    if(isProjectSet){


                    }else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Nie wybrano projektu");
                        alert.setContentText("Aby dodawać i usuwać członków projektu wybierz projekt z listy");

                        alert.showAndWait();
                    }
                });



            }catch (Exception e){
                e.printStackTrace();
            }
        // TODO
    }
}
