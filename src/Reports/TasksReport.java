package Reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import Utilize.DatabaseHandler;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;

/**
 * Created by mjaskot on 2017-05-31.
 */
public class TasksReport {

    public TasksReport() throws SQLException, ClassNotFoundException
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

        DRDataSource dataSource = new DRDataSource("TasksTotal", "TasksFinished","TasksAssigned","TasksInProgress");
        dataSource.add(10, 2, 2 ,4 );
        return  dataSource;
    }

    private void build() {
        TextColumnBuilder<Integer> TasksTotal = col.column("Tasks total", "TasksTotal", type.integerType());
        TextColumnBuilder<Integer> TasksFinished = col.column("Tasks finished", "TasksFinished", type.integerType());
        TextColumnBuilder<Integer> TasksAssigned = col.column("Tasks Assigned", "TasksAssigned", type.integerType());
        TextColumnBuilder<Integer> TasksProgress = col.column("Tasks in progress", "TasksInProgress", type.integerType());

        try {
            StyleBuilder boldStyle  = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);

            report()
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .columns(TasksTotal,TasksAssigned,TasksProgress,TasksFinished)
                    .title(cmp.text("Projects Summary").setStyle(boldCenteredStyle))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .setDataSource(createDataSource())
                    .show()
                    .toPdf(new FileOutputStream("F:/TasksReport.pdf"));
        } catch (DRException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
