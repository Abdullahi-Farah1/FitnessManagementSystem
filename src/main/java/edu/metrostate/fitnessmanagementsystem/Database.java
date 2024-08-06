package edu.metrostate.fitnessmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class Database {

    static {
        try {
            Connection connection = connectDB();
            if (connection != null) {
                initializeDatabase(connection);
                connection.close(); // Close the connection after initialization
            }
        } catch (Exception e) {
            e.printStackTrace(); // Consider using a logger instead
        }
    }

    public static Connection connectDB() {
        try {
            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitness", "root", "");
            return connect;
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead
        }
        return null;
    }

    private static void initializeDatabase(Connection connection) {
        String createAdminsTable = "CREATE TABLE IF NOT EXISTS admin (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "username VARCHAR(50) NOT NULL, " +
                "password VARCHAR(50) NOT NULL" +
                ");";

        String createTrainersTable = "CREATE TABLE IF NOT EXISTS trainers (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "trainerId VARCHAR(50) NOT NULL UNIQUE, " +
                "name VARCHAR(100), " +
                "username VARCHAR(50), " +
                "password VARCHAR(50), " +
                "address VARCHAR(200), " +
                "gender VARCHAR(10), " +
                "phoneNum BIGINT DEFAULT 0, " +
                "status VARCHAR(20) DEFAULT ''" +
                ");";

        String createClientsTable = "CREATE TABLE IF NOT EXISTS client (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "clientId VARCHAR(50) NULL UNIQUE, " +
                "email VARCHAR(100) DEFAULT '', " +
                "name VARCHAR(100) DEFAULT '', " +
                "username VARCHAR(50) DEFAULT '', " +
                "password VARCHAR(50) DEFAULT '', " +
                "address VARCHAR(200) DEFAULT '', " +
                "gender VARCHAR(10) DEFAULT '', " +
                "phoneNum INT DEFAULT 0, " +
                "status VARCHAR(20) DEFAULT '', " +
                "trainerId INT DEFAULT NULL, " +
                "FOREIGN KEY (trainerId) REFERENCES trainers(id) ON DELETE SET NULL ON UPDATE CASCADE" +
                ");";

        String createFitnessPlanTable = "CREATE TABLE IF NOT EXISTS fitnessplan (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "clientId VARCHAR(50) NOT NULL, " +
                "plan TEXT, " +
                "FOREIGN KEY (clientId) REFERENCES client(clientId) ON DELETE CASCADE ON UPDATE CASCADE" +
                ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createAdminsTable);
            stmt.execute(createTrainersTable);

           /*String insertTrainerTemplate = "INSERT INTO trainers (trainerId, name, username, password, address, gender, phoneNum, status) " +
                    "VALUES (%d, 'Trainer%d', 'trainer%d', 'password%d', 'Address%d', 'Male', %d, 'Active');";
            String insertAdminTemplate = "INSERT INTO admin (username, password) " +
                    "VALUES ('admin',12);";

            for (int i = 1; i <= 20; i++) {
                String insertTrainer = String.format(insertTrainerTemplate, i, i, i, i, i, i, 1234567890 + i);
                stmt.execute(insertTrainer);
            }

            stmt.execute(insertAdminTemplate);*/

            /*String insertClientTemplate = "INSERT INTO client (clientId, name, username, password, address, gender, phoneNum, status) " +
                    "VALUES (%d, 'Client%d', 'Client%d', 'password%d', 'Address%d', 'Male', 80444%d, 'Active');";

            for (int i = 1; i <= 20; i++) {
                String insertTrainer = String.format(insertClientTemplate, i, i, i, i, i, i, 1234567890 + i);
                stmt.execute(insertTrainer);
            }*/

            stmt.execute(createClientsTable);
            stmt.execute(createFitnessPlanTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}