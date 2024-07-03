package edu.metrostate.fitnessmanagementsystem;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connectDB() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            Connection connect;
            connect = DriverManager.getConnection("jdbc:mysql://localhost:3306/fitness","root","");
            return connect;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
