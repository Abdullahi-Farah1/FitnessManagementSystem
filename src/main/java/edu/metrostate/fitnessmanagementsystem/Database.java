package edu.metrostate.fitnessmanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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

    public ObservableList<TrainerData> getAllTrainers() {
        ObservableList<TrainerData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM trainers";
        try (Connection connect = connectDB();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

            while (result.next()) {
                TrainerData trainer = new TrainerData(result.getInt("id"),
                        result.getString("trainerId"),
                        result.getString("name"),
                        result.getString("username"),
                        result.getString("password"),
                        result.getString("address"),
                        result.getString("gender"),
                        result.getInt("phoneNum"),
                        result.getString("status"));
                listData.add(trainer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    public ObservableList<ClientData> getClientsByTrainerId(String trainerId) {
        ObservableList<ClientData> clients = FXCollections.observableArrayList();
        String sql = "SELECT * FROM client WHERE trainerId = ?";
        try (Connection connect = connectDB();
             PreparedStatement pstmt = connect.prepareStatement(sql)) {
            pstmt.setString(1, trainerId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                ClientData client = new ClientData(rs.getInt("id"),
                        rs.getString("clientId"),
                        rs.getString("name"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("address"),
                        rs.getString("gender"),
                        rs.getInt("phoneNum"),
                        rs.getString("status"));
                clients.add(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }




}
