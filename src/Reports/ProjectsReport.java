package Reports;

import Utilize.DatabaseHandler;
import javafx.collections.ObservableList;
import model.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.jfree.ui.HorizontalAlignment;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
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

        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost/programowaniegrupowe","mjaskot","sushijestsmaczne");

        Statement stmt = connection.createStatement();
        String querry = "SELECT * from projects";
        ResultSet rs = stmt.executeQuery(querry);
        int count = 0;
        while(rs.next()) {
          count++;
        }

        querry = "select count(*) \n" +
                "from \n" +
                "(\n" +
                "select sum(t.state), count(t.description), p.project_id\n" +
                "from projects p, tasks t \n" +
                "where p.project_id = t.project_id\n" +
                "group by p.project_id\n" +
                "having sum(t.state) <> 2*count(t.description)\n" +
                ") as y";
        rs = stmt.executeQuery(querry);
        int ongoing = 0;
        while(rs.next())
        {
            ongoing++;
        }

        querry = "select count(*) \n" +
                "from \n" +
                "(\n" +
                "select sum(t.state), count(t.description), p.project_id\n" +
                "from projects p, tasks t \n" +
                "where p.project_id = t.project_id\n" +
                "group by p.project_id\n" +
                "having sum(t.state) = 2*count(t.description)\n" +
                ") as y;";
        rs = stmt.executeQuery(querry);
        int finished = 0;
        while(rs.next())
        {
            finished++;
        }

        querry = "select count( distinct p.project_id)\n" +
                "from projects p, tasks t \n" +
                "where p.project_id \n" +
                "\tnot in(select t.project_id from tasks t where p.project_id = t.project_id )";
        rs=stmt.executeQuery(querry);
        int empty = 0;
        while(rs.next())
        {
            empty++;
        }

        DRDataSource dataSource = new DRDataSource("projectsTotal", "projectsOngoing","projectsEmpty","projectsFinished");
        dataSource.add(count, ongoing, empty ,finished );
        return  dataSource;
    }

    private void build() throws SQLException, ClassNotFoundException {

        TextColumnBuilder<Integer> projectsTotal = col.column("Projects total", "projectsTotal", type.integerType());
        TextColumnBuilder<Integer> projectsOngoing = col.column("Projects ongoing", "projectsOngoing", type.integerType());
        TextColumnBuilder<Integer> projectsEmpty = col.column("Projects empty", "projectsEmpty", type.integerType());
        TextColumnBuilder<Integer> projectsFinished = col.column("Projects finished", "projectsFinished", type.integerType());

        try {
            StyleBuilder boldStyle  = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);


            report()
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .columns(projectsTotal, projectsOngoing,projectsEmpty,projectsFinished)
                    .title(cmp.text("Projects Summary").setStyle(boldCenteredStyle))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .setDataSource(createDataSource())
                    .show()
                    .toPdf(new FileOutputStream("F:/ProjectsReport.pdf"));
        } catch (DRException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
