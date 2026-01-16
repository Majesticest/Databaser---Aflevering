package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connectDB {
    private final Connection connection;
    // to see debug text change debugMode = 1; else keep it at 0;
    int debugMode = 0;

    public connectDB(){
        String url = "jdbc:sqlite:EntityDB.db";
        try {
            connection = DriverManager.getConnection(url);
            if(debugMode ==1){
            System.out.println("Con success");
            }
        } catch (SQLException e) {
            System.out.println("Con fail");
            throw new RuntimeException(e);
        }
    }
    public Connection getLocalConnection(){
        return connection;
    }
    public void closeLocalConnection(){
        if(connection != null){
            try {
                connection.close();
                System.out.println("Close Success");
            } catch (SQLException e) {
                System.out.println("Close fail");
                throw new RuntimeException(e);
            }
        }
    }
}
