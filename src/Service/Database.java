package Service;

import java.sql.*;
import java.util.ArrayList;

public class Database {
    private static String databaseUrl = "jdbc:mysql://sql11.freemysqlhosting.net:3306/sql11167655";
    private static String databaseLogin = "sql11167655";
    private static String databasePassword = "d7Wr67dWJQ";

    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList<Object[]> selectQuery(String query) {
        ArrayList<Object[]> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(databaseUrl, databaseLogin, databasePassword)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columnsCount = resultSetMetaData.getColumnCount();
                    while (resultSet.next()) {
                        Object[] row = new Object[columnsCount];
                        for (int columnIndex = 1; columnIndex <= columnsCount; columnIndex++) {
                            row[columnIndex - 1] = resultSet.getObject(columnIndex);
                        }
                        result.add(row);
                    }
                    return result;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Error("");
        }
    }
}