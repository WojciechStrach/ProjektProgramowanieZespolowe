package Components.Main;

import javafx.scene.control.ListCell;
import model.Tasks;

public class TaskListCell extends ListCell<Tasks>
{
    @Override
    public void updateItem(Tasks task, boolean empty)
    {
        super.updateItem(task, empty);
        if(task != null)
        {
            setGraphic(new Components.Main.TaskController(task).getBox());
        }
    }
}