package Reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;
import Utilize.DatabaseHandler;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
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

        String tasksStmt = "SELECT * " +
                "FROM tasks " +
                "WHERE state =" + "TODO";

        ResultSet rsTasks = DatabaseHandler.databaseExecuteQuery(tasksStmt);

        DRDataSource dataSource = new DRDataSource("dataType", "quantity");
        dataSource.add("Tasks ongoing",1);
        dataSource.add("Tasks finished",1);
        dataSource.add("Tasks assigned",1);
        dataSource.add("Tasks unassigned",1);
        return  dataSource;
    }

    private void build() {
        TextColumnBuilder<String> dataType = col.column("DataType", "dataType", type.stringType());
        TextColumnBuilder<Integer> quantity = col.column("Quantity", "quantity", type.integerType());

        try {
            FontBuilder boldFont = stl.fontArialBold().setFontSize(12);
            StyleBuilder boldStyle  = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);

            report()
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .columns(dataType,quantity)
                    .title(cmp.text("Tasks Summary").setStyle(boldCenteredStyle))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .setDataSource(createDataSource())
                    .summary(cht.pie3DChart().setTitle("Pie Chart").setTitleFont(boldFont).setKey(dataType).series(cht.serie(quantity)))
                    .toPdf(new FileOutputStream("TasksReport.pdf"));
        } catch (DRException e) {
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
