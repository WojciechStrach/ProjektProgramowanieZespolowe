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
public class UsersReport {

    public UsersReport() throws SQLException, ClassNotFoundException
    {
        build();
    }

    private JRDataSource createDataSource() throws SQLException, ClassNotFoundException {

        /*Connection connection = null;
        connection = DriverManager.getConnection("jdbc:mysql://localhost/programowaniegrupowe","mjaskot","sushijestsmaczne");

        Statement stmt = connection.createStatement();
        String querry = "Select distinct username from users u, tasks t where u.user_id = t.user_id";
        ResultSet rs = stmt.executeQuery(querry);
        int inprojects = 0;
        while(rs.next()) {
            inprojects++;
        }
        querry = "Select distinct username from users u, tasks t where u.user_id <> t.user_id;";
        rs = stmt.executeQuery(querry);
        int outprojects = 0;
        while(rs.next())
        {
            outprojects++;
        }*/
        DRDataSource dataSource = new DRDataSource("inProjects", "outProjects");
        dataSource.add(3,2);
        return  dataSource;
    }

    private void build() {
        TextColumnBuilder<Integer> inProjects = col.column("Users in projects", "inProjects", type.integerType());
        TextColumnBuilder<Integer> outProjects = col.column("Users not assigned", "outProjects", type.integerType());
        try {
            StyleBuilder boldStyle  = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);


            report()
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .columns(inProjects, outProjects)
                    .title(cmp.text("Users Summary").setStyle(boldCenteredStyle))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .setDataSource(createDataSource())
                    .show()
                    .toPdf(new FileOutputStream("UsersReport.pdf"));
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
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        UsersReport rep = new UsersReport();
        ProjectsReport rep2 = new ProjectsReport();
        TasksReport rep3 = new TasksReport();
    }
}
