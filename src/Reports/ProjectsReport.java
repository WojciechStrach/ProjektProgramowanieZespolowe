package Reports;

import Utilize.DatabaseHandler;
import javafx.collections.ObservableList;
import model.*;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
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

        DRDataSource dataSource = new DRDataSource("dataType", "quantity");
        dataSource.add("Projects ongoing",4);
        dataSource.add("Finished finished",2);
        dataSource.add("Projects with users",1);
        dataSource.add("Project without users",0);
        return  dataSource;
    }

    private void build() throws SQLException, ClassNotFoundException {

        TextColumnBuilder<String> dataType = col.column("DataType", "dataType", type.stringType());
        TextColumnBuilder<Integer> quantity = col.column("Quantity", "quantity", type.integerType());

        try {
            StyleBuilder boldStyle  = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle  = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);
            FontBuilder boldFont = stl.fontArialBold().setFontSize(12);
            report()
                    .setColumnTitleStyle(columnTitleStyle)
                    .highlightDetailEvenRows()
                    .columns(dataType,quantity)
                    .title(cmp.text("Projects Summary").setStyle(boldCenteredStyle))
                    .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                    .setDataSource(createDataSource())
                    .summary(cht.pie3DChart().setTitle("Pie Chart").setTitleFont(boldFont).setKey(dataType).series(cht.serie(quantity)))
                    .toPdf(new FileOutputStream("F:/ProjectsReport.pdf"));
        } catch (DRException e) {
            e.printStackTrace();
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
