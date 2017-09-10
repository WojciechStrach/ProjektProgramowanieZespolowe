package Service;


import java.sql.*;
import java.util.ArrayList;

class Database<T> {
    private static String databaseUrl = "jdbc:mysql://mysql5.gear.host:3306/programowanie";
    private static String databaseLogin = "programowanie";
    private static String databasePassword = "<surowe>";
//    private static String databaseUrl = "jdbc:mysql://localhost/kira";
//    private static String databaseLogin = "kamil";
//    private static String databasePassword = "qwe123";
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<T> selectQuery(String query, ParseRowToModel<T> parseRowToModel) {
        ArrayList<T> result = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(databaseUrl, databaseLogin, databasePassword)) {
            try (Statement statement = connection.createStatement()) {
                try (ResultSet resultSet = statement.executeQuery(query)) {
                    while (resultSet.next()) {
                        T modelObject = parseRowToModel.parseRowToModel(resultSet);
                        result.add(modelObject);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Error("");
        }
        return result;
    }
}