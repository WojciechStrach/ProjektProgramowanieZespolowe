package Reports;

import static net.sf.dynamicreports.report.builder.DynamicReports.*;

import model.ProjectsDAO;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.WhenNoDataType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class ProjectsUsersActivityReport {
//
    public ProjectsUsersActivityReport(HashMap<String, HashMap<String, Integer>> projectsWithDetails) throws SQLException, ClassNotFoundException
    {
        build(projectsWithDetails);
    }

    private JRDataSource createDataSource(HashMap<String, Integer> projectDetails) throws SQLException, ClassNotFoundException {
        DRDataSource dataSource = new DRDataSource("userName", "doneTasks");

        for(Map.Entry<String, Integer> projectDetail : projectDetails.entrySet()) {
            dataSource.add(projectDetail.getKey(), projectDetail.getValue());
        }


        return dataSource;
    }

    private void build(HashMap<String, HashMap<String, Integer>> projectsWithDetails) {
        TextColumnBuilder<String> userName = col.column("User name", "userName", type.stringType());
        TextColumnBuilder<Integer> doneTasks = col.column("User done tasks", "doneTasks", type.integerType());
        try {
            StyleBuilder boldStyle = stl.style().bold();
            StyleBuilder boldCenteredStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER);
            StyleBuilder columnTitleStyle = stl.style(boldCenteredStyle).setBorder(stl.pen1Point()).setBackgroundColor(Color.LIGHT_GRAY);

            SubreportBuilder[] reports = new SubreportBuilder[projectsWithDetails.size()];

            int i = 0;

            for(Map.Entry<String, HashMap<String, Integer>> projectWithDetails : projectsWithDetails.entrySet()) {
                String projectName = projectWithDetails.getKey();
                reports[i] = cmp.subreport(report()
                        .setColumnTitleStyle(columnTitleStyle)
                        .highlightDetailEvenRows()
                        .columns(userName, doneTasks)
                        .title(cmp.text("Finished tasks by users in project: " + projectName).setStyle(boldCenteredStyle))
                        .pageFooter(cmp.pageXofY().setStyle(boldCenteredStyle))
                        .summary(cht.pie3DChart().setKey(userName).series(cht.serie(doneTasks)))
                        .setDataSource(createDataSource(projectWithDetails.getValue()))
                );
                i++;
            }


            report()
                    .setWhenNoDataType(WhenNoDataType.ALL_SECTIONS_NO_DETAIL)
                    .title(reports)
                    .show()
                    .toPdf(new FileOutputStream("ProjectsUsersActivityReport.pdf"));
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
        /*UsersReport rep = new UsersReport();*/
//        ObservableList<Projects> projects = ProjectsDAO.getAllProjects();

//        ProjectsUsersActivityReport rep2 = new ProjectsUsersActivityReport(projects);
        new ProjectsUsersActivityReport(ProjectsDAO.getProjectsWithDetails());
        /*ProjectsUsersActivityReport rep3 = new ProjectsUsersActivityReport();*/
    }
}