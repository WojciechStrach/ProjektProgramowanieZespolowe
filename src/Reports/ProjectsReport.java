package Reports;

import javafx.collections.ObservableList;
import model.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import java.sql.SQLException;
import java.util.Observable;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

/**
 * Created by mjaskot on 2017-05-31.
 */
public class ProjectsReport {

    public ProjectsReport() throws SQLException, ClassNotFoundException
    {
        build();
    }

    private JRDataSource createDataSource() throws SQLException, ClassNotFoundException {
        DRDataSource dataSource = new DRDataSource("projectsTotal", "projectsOngoing", "projectsFinished");
        dataSource.add(1, 2, 3 );
        return  dataSource;
    }

    private void build() throws SQLException, ClassNotFoundException {

        TextColumnBuilder<Integer> projectsTotal = col.column("Projects total", "projectsTotal", type.integerType());
        TextColumnBuilder<Integer> projectsOngoing = col.column("Projects ongoing", "projectsOngoing", type.integerType());
        TextColumnBuilder<Integer> projectsFinished = col.column("Projects finished", "projectsFinished", type.integerType());

        try {
            report()
                    .columns(projectsTotal, projectsOngoing, projectsFinished)
                    .title(cmp.text("Projects Summary"))
                    .pageFooter(cmp.pageXofY())
                    .setDataSource(createDataSource())
                    .show();
        } catch (DRException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        ProjectsReport rep = new ProjectsReport();
    }
}
