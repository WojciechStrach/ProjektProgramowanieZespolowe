package Utilize;

import com.sun.rowset.CachedRowSetImpl;
import java.sql.*;

public class DatabaseHandler {

    private static Connection conn = null;
    private static String databaseUrl = DatabaseData.databaseUrl;
    private static String databaseName = DatabaseData.databaseName;
    private static String username = DatabaseData.databaseLogin;
    private static String password = DatabaseData.databasePassword;


    private static void databaseConnect() throws SQLException, ClassNotFoundException {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://" + databaseUrl + "/" + databaseName,username,password);
        } catch (SQLException e) {
            System.out.println("Coś z połączeniem i/lub bazą danych");
            e.printStackTrace();
        }
    }


    private static void databaseDisconnect() throws SQLException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e){
            System.out.println("Nie udało się zamknąć połączenia");
            e.printStackTrace();
        }
    }

        public static ResultSet databaseExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {

            Statement stmt = null;
            ResultSet resultSet = null;
            CachedRowSetImpl crs = null;

            try {

                databaseConnect();
//                System.out.println("Wykonuje zapytanie: " + queryStmt + "\n");

                stmt = conn.createStatement();

                resultSet = stmt.executeQuery(queryStmt);

                crs = new CachedRowSetImpl();
                crs.populate(resultSet);

            } catch (SQLException e) {
                System.out.println("Nie udało się wykonać zapytania: " + e);
                e.printStackTrace();
            } catch (NullPointerException e) {
                System.out.println("Nie udało się wykonać zapytania: " + queryStmt + e);
                e.printStackTrace();
            } catch (ClassNotFoundException e){
                System.out.println("To je surowe");
                e.printStackTrace();

            } finally {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                databaseDisconnect();
            }

            return crs;
        }

        public static void databaseExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {

            Statement stmt = null;

            try {
                databaseConnect();
                stmt = conn.createStatement();
                stmt.executeUpdate(sqlStmt);
            } catch (SQLIntegrityConstraintViolationException e) {
                throw new SQLIntegrityConstraintViolationException();
            } catch (SQLException e) {
                System.out.println("Nie udało się zaktualizwoać danych w bazie : " + e);
                e.printStackTrace();
            } catch(ClassNotFoundException e) {
                System.out.println("Całkowicie nic się nie udało");
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
                databaseDisconnect();
            }
        }
    }

