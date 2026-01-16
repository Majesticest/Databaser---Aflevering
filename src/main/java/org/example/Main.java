package org.example;
import java.sql.*;


public class Main {
    public static void main(String[] args) {
        connectDB db = new connectDB();
        Connection connection = db.getLocalConnection();
        if (connection != null) {
            try {
                showDB();
                System.out.println("This works");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static void showDB() throws SQLException {
        try{
            connectDB db = new connectDB();
            Connection connection = db.getLocalConnection();

            Statement stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM kunde");

            ResultSetMetaData meta = resultSet.getMetaData();
            int columnCount = meta.getColumnCount();

            // Print column headers
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(meta.getColumnName(i) + "\t");
            }
            System.out.println();

            // Print rows
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(resultSet.getString(i) + "\t");
                }
                System.out.println();
            }
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
}