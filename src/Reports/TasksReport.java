package Reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import Utilize.DatabaseHandler;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import java.sql.ResultSet;

/**
 * Created by mjaskot on 2017-05-31.
 */
public class TasksReport {

    private ResultSet getTasksTotal()
    {
        String selectStatement = "SELECT count(*) as tasksTotal FROM Tasks ";
        try{
            ResultSet rsProjects = DatabaseHandler.databaseExecuteQuery(selectStatement);
            return rsProjects;
        }
        catch (Exception e){
            System.out.println("Wystąpił błąd podczas wczytywania projektów z bazy.");
            e.printStackTrace();
            return null;
        }
    }
    private void build() {
        TextColumnBuilder<Integer> projectsTotal = col.column("Tasks total", "projectsTotal", type.integerType());
        TextColumnBuilder<Integer> projectsOngoing = col.column("Tasks ongoing", "projectsTotal", type.integerType());
        TextColumnBuilder<Integer> projectsFinished = col.column("Tasks finished", "projectsTotal", type.integerType());

        try {
            report()
                    .columns(projectsTotal, projectsOngoing, projectsFinished)
                    .title(cmp.text("Projects Summary"))
                    .pageFooter(cmp.pageXofY())
                    .setDataSource(getTasksTotal())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
}
