package StudentConsultationSystem.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.ArrayList;

public class DbHelper {
    private static Connection conn;

    public static Connection getConnection() throws Exception{
        if(conn == null || conn.isClosed()){
            conn = DriverManager.getConnection(AppConfig.get().getConnectionString());
        }
        return conn;
    }

    public static void closeConnection() throws Exception{
        if(conn != null || !conn.isClosed()){
            conn.close();
        }
    }

    public static void migrate() throws Exception {
        String driverType = AppConfig.get().getDriverType();
        String autoIncFunc = driverType.equals("mysql") ? "AUTO_INCREMENT" : "AUTOINCREMENT";

        ArrayList<String> queries = new ArrayList<>();
        queries.add(
                String.format("CREATE TABLE IF NOT EXISTS student (id INTEGER NOT NULL PRIMARY KEY %s, username VARCHAR(50) NOT NULL,password VARCHAR(255),name VARCHAR(50) NOT NULL, gender VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL, salt VARCHAR(255) NOT NULL)", autoIncFunc)
        );
        queries.add(
                String.format("CREATE TABLE IF NOT EXISTS professor (id INTEGER NOT NULL PRIMARY KEY %s, username VARCHAR(50) NOT NULL,password VARCHAR(255),name VARCHAR(50) NOT NULL, gender VARCHAR(50) NOT NULL, email VARCHAR(50) NOT NULL, salt VARCHAR(255) NOT NULL,phone VARCHAR(50) NOT NULL,website VARCHAR(50) NOT NULL)", autoIncFunc)
        );
        queries.add(
                String.format("CREATE TABLE IF NOT EXISTS admin (id INTEGER NOT NULL PRIMARY KEY %s, username VARCHAR(50) NOT NULL,password VARCHAR(255))",autoIncFunc)
        );

        Connection connection = getConnection();
        for (String sql : queries){
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(sql);
        }
    }
}
