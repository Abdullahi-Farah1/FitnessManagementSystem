package edu.metrostate.fitnessmanagementsystem;


import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class TrainerController implements Initializable {
    @FXML
    private TableColumn<ClientData, String> client_col_clientAddress;

    @FXML
    private TableColumn<ClientData, String> client_col_clientGender;

    @FXML
    private TableColumn<ClientData, String> client_col_clientID;

    @FXML
    private TableColumn<ClientData, String> client_col_clientName;

    @FXML
    private TableColumn<ClientData, Integer> client_col_clientPhoneNum;

    @FXML
    private TableColumn<ClientData, String> client_col_clientStatus;

    @FXML
    private TableColumn<ClientData, String> client_col_clientUsername;

    @FXML
    private Button close_btn;

    @FXML
    private TextArea fitness_plan;

    @FXML
    private Button fitnessplan_btn;

    @FXML
    private Button log_out_btn;

    @FXML
    private Button minimize_btn;

    @FXML
    private TableView<ClientData> trainerClient_tableview;
    @FXML
    private Label trainerUser;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    private Database database = new Database();

    public void displayUsername() {
        String user = SessionManager.username;

        user = user.substring(0,1).toUpperCase() + user.substring(1);

        trainerUser.setText(user);
    }

    @FXML
    private void sendFitnessPlan(ActionEvent event) {
        ClientData selectedClient = trainerClient_tableview.getSelectionModel().getSelectedItem();
        String fitnessPlan = fitness_plan.getText();

        if (selectedClient != null && !fitnessPlan.isEmpty()) {
            String sql = "INSERT INTO fitnessplan(clientId, plan) VALUES(?, ?)";
            connect = Database.connectDB();
            try (PreparedStatement pstmt = connect.prepareStatement(sql)) {
                pstmt.setString(1, selectedClient.getClientId());
                pstmt.setString(2, fitnessPlan);
                pstmt.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Fitness plan sent successfully!");
                alert.showAndWait();
            } catch (Exception e) {
                e.printStackTrace();
            }
            fitness_plan.clear(); // Clear the text area after sending
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a client and write a fitness plan.");
            alert.showAndWait();
        }
    }


    public ObservableList<ClientData> clientDataList() {

        ObservableList<ClientData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM client";

        connect = Database.connectDB();


        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            ClientData cd;

            while (result.next()) {
                cd = new ClientData(result.getInt("id"), result.getString("clientId"),result.getString("name"),
                        result.getString("username"),
                        result.getString("password"), result.getString("address"),
                        result.getString("gender"),result.getInt("phoneNum"),
                        result.getString("status"), result.getString("trainerId"));

                listData.add(cd);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<ClientData> clientListData;

    public void clientShowData() {

        clientListData = clientDataList();

        client_col_clientID.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        client_col_clientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        client_col_clientUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        client_col_clientAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        client_col_clientGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        client_col_clientPhoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        client_col_clientStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        trainerClient_tableview.setItems(clientListData);

    }


    private double x = 0;
    private double y = 0;

    public void logout() {

        try {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if(option.get().equals(ButtonType.OK)) {

                log_out_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("admin-trainer.fxml"));

                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void minimize() {
        Stage stage = (Stage) minimize_btn.getScene().getWindow();
        stage.setIconified(true);
    }

    public void close() {
        Platform.exit();
    }

    public void showClientsForLoggedInTrainer() {
        TrainerData currentTrainer = SessionManager.getCurrentTrainer();

        if (currentTrainer != null) {
            ObservableList<ClientData> filteredClients = FXCollections.observableArrayList();
            for (ClientData client : clientListData) {
                if (currentTrainer.getTrainerId().equals(client.getTrainerId())) {
                    filteredClients.add(client);
                }
            }
            trainerClient_tableview.setItems(filteredClients);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

            clientShowData();
            displayUsername();
        showClientsForLoggedInTrainer();

    }
}
