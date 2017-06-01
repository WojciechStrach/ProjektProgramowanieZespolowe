package Reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import Utilize.DatabaseHandler;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.exception.DRException;
import java.sql.ResultSet;

/**
 * Created by mjaskot on 2017-05-31.
 */
public class UsersReport {

    private ResultSet getUsersTotal()
    {
        String selectStatement = "SELECT count(*) as UsersTotal FROM Users ";
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
        TextColumnBuilder<Integer> projectsTotal = col.column("Users with projects", "userswp", type.integerType());
        TextColumnBuilder<Integer> projectsOngoing = col.column("Users without projects", "projectswop", type.integerType());
        try {
            report()
                    .columns(projectsTotal, projectsOngoing)
                    .title(cmp.text("Projects Summary"))
                    .pageFooter(cmp.pageXofY())
                    .setDataSource(getUsersTotal())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
}
