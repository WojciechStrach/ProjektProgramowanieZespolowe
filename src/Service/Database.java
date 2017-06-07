package Service;


import java.sql.*;
import java.util.ArrayList;

class Database<T> {
    private static String databaseUrl = "http://sql31.hostinger.pl/u376531145_ramse";
    private static String databaseLogin = "u376531145_ramse";
    private static String databasePassword = "kompletniesurowe";

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