package Components.Main;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.*;
import Main.ParentsList;
import Main.ParentsLoader;
import Service.Session;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import com.sun.javafx.scene.control.skin.TableViewSkinBase;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import model.*;

public class MainController implements Initializable {

    private SimpleStringProperty clickedProject = new SimpleStringProperty();
    private Projects currentProject;
    private boolean isProjectSet = false;
    private int numberOfUsersObjects = 0;
    private ScheduledExecutorService executor = new ScheduledThreadPoolExecutor(0);
    private ScheduledFuture<?> exec;
    private boolean executorStatus = false;

    private ObservableList<Tasks> projectDoneTaskCollection = FXCollections.observableArrayList();
    private ObservableList<Tasks> projectToDoTaskCollection = FXCollections.observableArrayList();
    private ObservableList<Tasks> projectToReviewTaskCollection = FXCollections.observableArrayList();
    @FXML
    private Label label;
    @FXML
    private Label userName;
    @FXML
    private ComboBox<String> projects;
    @FXML
    private TableView<Tasks> tasksDoneTableView = new TableView<>();
    @FXML
    private TableView<Tasks> tasksToDoTableView = new TableView<>();
    @FXML
    private TableView<Tasks> tasksToReviewTableView = new TableView<>();
    @FXML
    private ListView<String> projectUserList;
    @FXML
    private Label projectName;
    @FXML
    private Button addTask;
    @FXML
    private Button removeTask;
    @FXML
    private Button editTask;
    @FXML
    private Button editUser;
    @FXML
    private Button newProject;
    @FXML
    private Button addMember;
    @FXML
    private Button removeMember;
    @FXML
    private Button logOut;
    
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


        } catch (Exception e){
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

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

        return matchedMembers;

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initUserActions();
        initTasksTableView(tasksToDoTableView, projectToDoTaskCollection);
        initTasksTableView(tasksToReviewTableView, projectToReviewTaskCollection);
        initTasksTableView(tasksDoneTableView, projectDoneTaskCollection);
        userName.setText(Session.getDisplayName());
        clickedProject.bindBidirectional(projects.valueProperty());
        ObservableList<Projects> userProjects = matchProjects();
        for (Projects uP : userProjects) {
            projects.getItems().add(uP.getTitle());
        }
    }

    private void initUserActions() {
        logOut.setOnAction(event -> {
            try {
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(ParentsLoader.getParent(ParentsList.splash));
                stage.setScene(scene);
            } catch (Exception e){
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
        editTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Tasks selected = null;
                Tasks temp = tasksToDoTableView.getSelectionModel().getSelectedItem();
                if (temp != null) {
                    selected = temp;
                }
                temp = tasksToReviewTableView.getSelectionModel().getSelectedItem();
                if (temp != null) {
                    selected = temp;
                }
                temp = tasksDoneTableView.getSelectionModel().getSelectedItem();
                if (temp != null) {
                    selected = temp;
                }
                final Tasks selectedFinal = selected;
                if(isProjectSet) {
                    Dialog dialog = updateTaskDialog(selectedFinal);
                    Optional<String[]> result = dialog.showAndWait();

                    result.ifPresent(task -> {
                        try {
                            TasksDAO.updateTask(selectedFinal.getTaskId(), task[0], task[1]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Nie wybrano projektu");
                    alert.setContentText("Aby dodać zadanie najpierw wybierz projekt z menu");

                    alert.showAndWait();
                }
            }
        });
        removeTask.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(isProjectSet) {
                    Tasks selected = null;
                    Tasks temp = tasksToDoTableView.getSelectionModel().getSelectedItem();
                    if (temp != null) {
                        selected = temp;
                    }
                    temp = tasksToReviewTableView.getSelectionModel().getSelectedItem();
                    if (temp != null) {
                        selected = temp;
                    }
                    temp = tasksDoneTableView.getSelectionModel().getSelectedItem();
                    if (temp != null) {
                        selected = temp;
                    }

                    if (selected != null) {
                        try {
                            TasksDAO.deleteTaskByDescription(selected.getDescription());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd");
                        alert.setHeaderText("Nie zaznaczono zadania");
                        alert.setContentText("Zaznacz zadanie aby je usunąć");

                        alert.showAndWait();
                    }
                } else {
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

                    try {

                        ObservableList<ProjectsMembers> projectMembers = ProjectsMembersDAO.searchProjectMembersByProjectId(currentProject.getProjectId());
                        List<String>temp = new ArrayList<>();
                        for (ProjectsMembers pM : projectMembers){
                            Users userTemp = UsersDAO.searchUsers(pM.getUserId());
                            temp.add(userTemp.getDisplayName());
                        }
                        ObservableList<Users> allUsers = UsersDAO.getAllUsers();

                        for (int i=0; i<allUsers.size(); i++){
                            for(int j=0; j<temp.size(); j++){
                                if(allUsers.get(i).getDisplayName().equals(temp.get(j))){
                                    allUsers.remove(i);
                                }
                            }
                        }

                        List<String> choices = new ArrayList<>();
                        for(int k=0; k<allUsers.size(); k++){
                            choices.add(allUsers.get(k).getDisplayName());
                        }

                        ChoiceDialog<String> dialog = new ChoiceDialog<>(choices.get(0), choices);
                        dialog.setTitle("Wybierz użytkownika którego chcesz dodać do projektu");
                        dialog.setHeaderText("wybierz użytkownika z listy poniżej");
                        dialog.setContentText("Wybierz użytkownika:");

                        Optional<String> result = dialog.showAndWait();
                        if (result.isPresent()) {

                            System.out.println("Your choice: " + result.get());
                            Users addUser = UsersDAO.searchUsers(result.get());
                            ProjectsMembersDAO.insertProjectMember(currentProject.getProjectId(), addUser.getUserId(), 0);

                        }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

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
                ProjectsMembers adminCheck = ProjectsMembersDAO.adminGetter(currentProject.getProjectId());
                int adminCheckId = adminCheck.getUserId();
                if(adminCheckId == Session.getUserId()){

                    try {

                        String selected = projectUserList.getSelectionModel().getSelectedItem();
                        Users removeUser = UsersDAO.searchUsers(selected);
                        ProjectsMembersDAO.deleteProjectMember(currentProject.getProjectId(), removeUser.getUserId());

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd");
                    alert.setHeaderText("Brak uprawnień");
                    alert.setContentText("Musisz być właścicielem projektu aby dodawać i usuwać jego członków");

                    alert.showAndWait();
                }


            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd");
                alert.setHeaderText("Nie wybrano projektu");
                alert.setContentText("Aby dodawać i usuwać członków projektu wybierz projekt z listy");

                alert.showAndWait();
            }
        });
        clickedProject.addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            projectName.setText(newValue);
            isProjectSet = true;

            if(executorStatus){
                exec.cancel(false);
            }

            projectUserList.getItems().clear();
            projectDoneTaskCollection.clear();
            projectToDoTaskCollection.clear();

//                    projectTaskList.setItems(projectTaskCollection);

            numberOfUsersObjects = 0;

            try {
                currentProject = ProjectsDAO.searchProject(newValue);
                Runnable refreshValues = () -> {
                    try {
//                            System.out.println("ŁADOWANIE DANYCH");
                        ObservableList<Tasks> projectTasks = TasksDAO.getTaskById(currentProject.getProjectId());
                        ObservableList<Users> projectMembers = projectMembers();
                        Platform.runLater(() -> {
                            updateTasksCollections(projectTasks);
                            updateMembersCollection(projectMembers);
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                exec = executor.scheduleAtFixedRate(refreshValues, 0, 1, TimeUnit.SECONDS);
                executorStatus = true;
            } catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    private Dialog<String[]> updateTaskDialog(Tasks task) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Login Dialog");
        dialog.setHeaderText("Look, a Custom Login Dialog");

        ButtonType loginButtonType = new ButtonType("Update", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameTextField = new TextField(task.getDescription());
        usernameTextField.setPromptText("Task description");

        ComboBox<TaskState> stateComboBox = new ComboBox();
        stateComboBox.getSelectionModel().select(task.getState());
        stateComboBox.setItems( FXCollections.observableArrayList( TaskState.values()));
        stateComboBox.setPromptText("Task stateComboBox");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(usernameTextField, 1, 0);
        grid.add(new Label("State:"), 0, 1);
        grid.add(stateComboBox, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(() -> usernameTextField.requestFocus());

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new String[]{usernameTextField.getText(), stateComboBox.getSelectionModel().getSelectedItem().name()};
            }
            return null;
        });

        return dialog;
    }

    private ObservableList<Tasks> updateAndRemoveFromCollection(ObservableList<Tasks> collection, ObservableList<Tasks> newData) {
        for (int i = 0; i < collection.size(); i++) {
            Tasks currentTask = collection.get(i);
            boolean currentTaskToRemove = true;
            for (int j = 0; j < newData.size(); j++) {
                Tasks newTask = newData.get(j);
                if(currentTask.getTaskId() == newTask.getTaskId()) {
                    if(! currentTask.getState().equals(newTask.getState())) {
                        // update task with change collection
                        addTaskToSuitableColletion(newTask);
                        collection.remove(currentTask);
                    } else {
                        if(! currentTask.equals(newTask)) {
                            // update task
                            collection.set(i, newTask);
                        }
                        currentTaskToRemove = false;
                    }
                    newData.remove(newTask);
                    break;
                }
            }
            if(currentTaskToRemove) {
                collection.remove(currentTask);
                i--;
            }
        }
        return newData;
    }

    private void addTaskToSuitableColletion(Tasks task) {
        if(task.getState().name().equals(TaskState.TODO.name())) { // todo
            projectToDoTaskCollection.add(task);
        } else if(task.getState().name().equals(TaskState.DONE.name())) {
            projectDoneTaskCollection.add(task);
        } else if(task.getState().name().equals(TaskState.TOREVIEW.name())) {
            projectToReviewTaskCollection.add(task);
        } else {
            System.out.println("Undefined task state");
            System.exit(0);
        }
    }

    private void updateTasksCollections(ObservableList<Tasks> newData) {
        newData = updateAndRemoveFromCollection(projectToDoTaskCollection, newData);
        newData = updateAndRemoveFromCollection(projectToReviewTaskCollection, newData);
        newData = updateAndRemoveFromCollection(projectDoneTaskCollection, newData);
        for (int i = 0; i < newData.size(); i++) {
            addTaskToSuitableColletion(newData.get(i));
        }
    }

    private void updateMembersCollection(ObservableList<Users> newData) {
        if (numberOfUsersObjects < newData.size()) { // todo
            for (int i = numberOfUsersObjects; i < newData.size(); i++) {
                Users temp = newData.get(i);
                projectUserList.getItems().add(temp.getDisplayName());
            }
        }
        else if (numberOfUsersObjects > newData.size()) {
            numberOfUsersObjects = newData.size();
            projectUserList.getItems().clear();
            for (int j = 0; j < newData.size(); j++) {
                Users userRemoveTemp = newData.get(j);
                projectUserList.getItems().add(userRemoveTemp.getDisplayName());
            }
        }
        numberOfUsersObjects = newData.size();
    }

    private void initTasksTableView(TableView tableView, ObservableList<Tasks> data) {
        int avatarColumnWidth = 55;

        TableColumn<Tasks, String> taskNameColumn = new TableColumn<>("Task");
        taskNameColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        taskNameColumn.setPrefWidth((tableView.getPrefWidth() - avatarColumnWidth)/2 - 5); // todo

        TableColumn<Tasks, String> taskUserNameColumn = new TableColumn<>("Task");
        taskUserNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tasks, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Tasks, String> cd) {
                Tasks task  = cd.getValue();
                return Bindings.createObjectBinding(() -> UsersDAO.searchUsers(task.getUserId()).getDisplayName());
            }
        });
        taskUserNameColumn.setPrefWidth((tableView.getPrefWidth() - avatarColumnWidth)/2 - 5); // todo

        TableColumn<Tasks, ImageView> avatarColumn = new TableColumn<>("Avatar");
        avatarColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Tasks, ImageView>, ObservableValue<ImageView>>() {
            @Override
            public ObservableValue<ImageView> call(TableColumn.CellDataFeatures<Tasks, ImageView> cd) {
                Tasks task  = cd.getValue();
                return Bindings.createObjectBinding(() -> UsersDAO.searchUsers(task.getUserId()).getAvatar());
            }
        });
        avatarColumn.setPrefWidth(avatarColumnWidth);


        tableView.getColumns().setAll(taskNameColumn, taskUserNameColumn, avatarColumn);
        tableView.setItems(data);
        hideTableViewHeader(tableView);

        tableView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected and set value of selected item to Label
                if(tableView.getSelectionModel().getSelectedItem() != null)
                {
                    if (tableView != tasksToDoTableView) {
                        tasksToDoTableView.getSelectionModel().select(null); // todo
                    }
                    if (tableView != tasksDoneTableView) {
                        tasksDoneTableView.getSelectionModel().select(null);
                    }
                    if (tableView != tasksToReviewTableView) {
                        tasksToReviewTableView.getSelectionModel().select(null);
                    }
                }
            }
        });
    }

    private void hideTableViewHeader(TableView tableView) {
        tableView.skinProperty().addListener((a, b, newSkin) -> {
            TableHeaderRow header = ((TableViewSkinBase) newSkin).getTableHeaderRow();
            if (header.isVisible()){
                header.setMaxHeight(0);
                header.setMinHeight(0);
                header.setPrefHeight(0);
                header.setVisible(false);
            }
        });
    }
}
