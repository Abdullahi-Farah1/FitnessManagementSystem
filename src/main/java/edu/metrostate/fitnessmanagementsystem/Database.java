package edu.metrostate.fitnessmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    static {
        try {
            createDatabaseIfNotExists();
            Connection connection = connectDB();
            if (connection != null) {
                initializeDatabase(connection);
                connection.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void createDatabaseIfNotExists() {
        try (Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/", "root", ""); Statement stmt = connection.createStatement()) {
            String createDatabase = "CREATE DATABASE IF NOT EXISTS fitness";
            stmt.execute(createDatabase);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection connectDB() {
        try {
            Connection connect = DriverManager.getConnection("jdbc:mariadb://localhost:3306/fitness", "root", "");
            return connect;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initializeDatabase(Connection connection) {
        String createAdminsTable = "CREATE TABLE IF NOT EXISTS admin (" + "id INT AUTO_INCREMENT PRIMARY KEY, " + "username VARCHAR(50) NOT NULL, " + "password VARCHAR(50) NOT NULL" + ");";

        String createTrainersTable = "CREATE TABLE IF NOT EXISTS trainers (" + "id INT AUTO_INCREMENT PRIMARY KEY, " + "trainerId VARCHAR(50) NOT NULL UNIQUE, " + "name VARCHAR(100), " + "email VARCHAR(100), " + "username VARCHAR(50), " + "password VARCHAR(50), " + "address VARCHAR(200), " + "gender VARCHAR(10), " + "phoneNum BIGINT DEFAULT 0, " + "status VARCHAR(20) DEFAULT ''" + ");";

        String createClientsTable = "CREATE TABLE IF NOT EXISTS client (" + "id INT AUTO_INCREMENT PRIMARY KEY, " + "clientId VARCHAR(50) NULL UNIQUE, " + "email VARCHAR(100) DEFAULT '', " + "name VARCHAR(100) DEFAULT '', " + "username VARCHAR(50) DEFAULT '', " + "password VARCHAR(50) DEFAULT '', " + "address VARCHAR(200) DEFAULT '', " + "gender VARCHAR(10) DEFAULT '', " + "phoneNum BIGINT DEFAULT 0, " + "status VARCHAR(20) DEFAULT '', " + "trainerId VARCHAR(50) DEFAULT NULL, " + "FOREIGN KEY (trainerId) REFERENCES trainers(trainerId) ON DELETE SET NULL ON UPDATE CASCADE" + ");";

        String createFitnessPlanTable = "CREATE TABLE IF NOT EXISTS fitnessplan (" + "id INT AUTO_INCREMENT PRIMARY KEY, " + "clientId VARCHAR(50) NOT NULL, " + "plan TEXT, " + "FOREIGN KEY (clientId) REFERENCES client(clientId) ON DELETE CASCADE ON UPDATE CASCADE" + ");";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createAdminsTable);
            stmt.execute(createTrainersTable);
            stmt.execute(createClientsTable);
            stmt.execute(createFitnessPlanTable);

            // Check if admin exists
            String checkAdminQuery = "SELECT COUNT(*) FROM admin WHERE username = 'admin'";
            ResultSet rs = stmt.executeQuery(checkAdminQuery);
            rs.next();
            if (rs.getInt(1) == 0) {
                // Insert admin only if it doesn't exist
                String insertAdmin = "INSERT INTO admin (username, password) VALUES ('admin', 'password')";
                stmt.execute(insertAdmin);
            }

            // Check if trainers exist
            String checkTrainerQuery = "SELECT COUNT(*) FROM trainers";
            rs = stmt.executeQuery(checkTrainerQuery);
            rs.next();
            if (rs.getInt(1) == 0) {
                // Insert trainers only if the table is empty
                String insertTrainerTemplate = "INSERT INTO trainers (trainerId,name, email, username, password, address, gender, phoneNum, status) " + "VALUES (%d, 'Trainer%d', 'email%d', 'trainer%d', 'password%d', 'Address%d', 'Male', %d, 'Active')";
                for (int i = 1; i <= 20; i++) {
                    String insertTrainer = String.format(insertTrainerTemplate, i, i, i, i, i, i, 1234567890 + i);
                    stmt.execute(insertTrainer);
                }
            }

            String checkClientQuery = "SELECT COUNT(*) FROM client";
            rs = stmt.executeQuery(checkTrainerQuery);
            rs.next();
            if (rs.getInt(1) == 0) {
                // Insert trainers only if the table is empty
                String insertClientTemplate = "INSERT INTO trainers (clientId,email, name, username, password, address, gender, phoneNum, status) " + "VALUES (%d, 'email%d', 'trainer%d', 'password%d', 'Address%d', 'Male', %d, 'Active')";
                for (int i = 1; i <= 20; i++) {
                    String insertClient = String.format(insertClientTemplate, i, i, i, i, i, i, 1234567890 + i);
                    stmt.execute(insertClient);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Consider using a logger instead
        }
    }

}
