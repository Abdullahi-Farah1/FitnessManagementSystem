package edu.metrostate.fitnessmanagementsystem;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private Label clientUser;

    @FXML
    private Button clientProfile_dash;

    @FXML
    private Button clientAdd_trainer;

    @FXML
    private AnchorPane clientProfile_Form;

    @FXML
    private AnchorPane clientTrainer_Form;

    @FXML
    private Button clientTrainer_dash;

    @FXML
    private TextField client_Id;

    @FXML
    private ComboBox<?> client_gender;

    @FXML
    private TextField client_name;

    @FXML
    private TextField email_client;

    @FXML
    private TextField client_phoneNum;

    @FXML
    private ComboBox<?> client_status;

    @FXML
    private Button client_submitProfileBtn;

    @FXML
    private Button close_btn;

    @FXML
    private AnchorPane clientfitness_Form;

    @FXML
    private TableView<FitnessPlanData> fitnessPlan_table;
    @FXML
    private TableColumn<FitnessPlanData, String> fitnessPlan_col;

    @FXML
    private Button fitnessPlan_dash;

    @FXML
    private Button minimize_btn;

    @FXML
    private PasswordField password_client;

    @FXML
    private TextArea client_address;

    @FXML
    private TableColumn<TrainerData, String> trainer_col_trainerGender;

    @FXML
    private TableColumn<TrainerData, String> trainer_col_trainerID;

    @FXML
    private TableColumn<TrainerData, String> trainer_col_trainerName;

    @FXML
    private TableColumn<TrainerData, Integer> trainer_col_trainerPhoneNum;

    @FXML
    private TableColumn<TrainerData, String> trainer_col_trainerStatus;

    @FXML
    private TableView<TrainerData> trainer_tableView;

    @FXML
    private TextField username_client;

    @FXML
    private Button logout_btn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;


    public void displayUsername() {
        String user = SessionManager.username;

        user = user.substring(0, 1).toUpperCase() + user.substring(1);

        clientUser.setText(user);
    }

    public void clientAddTrainer(ActionEvent event) {
        TrainerData selectedTrainer = trainer_tableView.getSelectionModel().getSelectedItem();
        ClientData currentClient = SessionManager.getCurrentClient();

        if (selectedTrainer != null && currentClient != null) {
            connect = Database.connectDB();

            try {

                String checkClientStatusSql = "SELECT status FROM client WHERE clientId = ?";
                prepare = connect.prepareStatement(checkClientStatusSql);
                prepare.setString(1, currentClient.getClientId());
                result = prepare.executeQuery();

                if (result.next()) {
                    String clientStatus = result.getString("status");

                    if ("Inactive".equalsIgnoreCase(clientStatus)) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Must be an ACTIVE Client to choose a Trainer.");
                        alert.showAndWait();
                        return;
                    }
                }


                String checkTrainerStatusSql = "SELECT status FROM trainers WHERE trainerId = ?";
                prepare = connect.prepareStatement(checkTrainerStatusSql);
                prepare.setString(1, selectedTrainer.getTrainerId());
                result = prepare.executeQuery();

                if (result.next()) {
                    String trainerStatus = result.getString("status");

                    if ("Inactive".equalsIgnoreCase(trainerStatus)) {

                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("The selected trainer is INACTIVE and cannot be assigned.");
                        alert.showAndWait();
                        return;
                    }
                }


                String checkTrainerSql = "SELECT trainerId FROM client WHERE clientId = ?";
                prepare = connect.prepareStatement(checkTrainerSql);
                prepare.setString(1, currentClient.getClientId());
                result = prepare.executeQuery();

                if (result.next()) {
                    String assignedTrainerId = result.getString("trainerId");

                    if (assignedTrainerId != null && !assignedTrainerId.isEmpty()) {

                        if (!assignedTrainerId.equals(selectedTrainer.getTrainerId())) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation");
                            alert.setHeaderText(null);
                            alert.setContentText("Client already has a trainer assigned. Do you want to change your trainer to TrainerID: " + selectedTrainer.getTrainerId() + " ?");

                            Optional<ButtonType> confirmationResult = alert.showAndWait();
                            if (confirmationResult.isPresent() && confirmationResult.get() == ButtonType.OK) {

                                currentClient.setTrainerId(selectedTrainer.getTrainerId());
                                String updateSql = "UPDATE client SET trainerId = ? WHERE clientId = ?";
                                prepare = connect.prepareStatement(updateSql);
                                prepare.setString(1, selectedTrainer.getTrainerId());
                                prepare.setString(2, currentClient.getClientId());
                                prepare.executeUpdate();

                                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                                successAlert.setTitle("Success");
                                successAlert.setHeaderText(null);
                                successAlert.setContentText("Trainer updated successfully!");
                                successAlert.showAndWait();
                            } else {
                            }
                        } else {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("No Change");
                            alert.setHeaderText(null);
                            alert.setContentText("The selected trainer is already assigned to this client.");
                            alert.showAndWait();

                        }
                    } else {

                        currentClient.setTrainerId(selectedTrainer.getTrainerId());
                        String updateSql = "UPDATE client SET trainerId = ? WHERE clientId = ?";
                        prepare = connect.prepareStatement(updateSql);
                        prepare.setString(1, selectedTrainer.getTrainerId());
                        prepare.setString(2, currentClient.getClientId());
                        prepare.executeUpdate();

                        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                        successAlert.setTitle("Success");
                        successAlert.setHeaderText(null);
                        successAlert.setContentText("Trainer assigned successfully!");
                        successAlert.showAndWait();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (result != null) result.close();
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select a trainer.");
            alert.showAndWait();
        }
    }


    public void emptyFields() {
        Alert alert;
        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Blank Fields");
        alert.setHeaderText(null);
        alert.setContentText("Please do not leave the fields blank.");
        alert.showAndWait();
    }

    public ObservableList<FitnessPlanData> fitnessPlanDataList() {
        ObservableList<FitnessPlanData> listData = FXCollections.observableArrayList();
        ClientData currentClient = SessionManager.getCurrentClient();

        if (currentClient != null) {
            String sql = "SELECT * FROM fitnessplan WHERE clientId = ?";
            connect = Database.connectDB();

            try {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, currentClient.getClientId());
                result = prepare.executeQuery();

                while (result.next()) {
                    FitnessPlanData fd = new FitnessPlanData(result.getInt("id"), result.getString("clientId"), result.getString("plan"));
                    listData.add(fd);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return listData;
    }


    private ObservableList<FitnessPlanData> fitnessPlanListData;

    public void fitnessPlanShowData() {

        ClientData currentClient = SessionManager.getCurrentClient();


        ObservableList<FitnessPlanData> filteredPlans = FXCollections.observableArrayList();


        if (currentClient != null) {
            // Get all fitness plans
            fitnessPlanListData = fitnessPlanDataList();


            for (FitnessPlanData plan : fitnessPlanListData) {
                if (currentClient.getClientId().equals(plan.getClientId())) {
                    filteredPlans.add(plan);
                }
            }


            fitnessPlan_col.setCellValueFactory(new PropertyValueFactory<>("plan"));
            fitnessPlan_table.setItems(filteredPlans);
        }
    }


    public void clientClearBtn() {
        client_Id.setText("");
        client_name.setText("");
        email_client.setText("");
        username_client.setText("");
        password_client.setText("");
        client_address.setText("");
        client_gender.getSelectionModel().clearSelection();
        client_phoneNum.setText("");
        client_status.getSelectionModel().clearSelection();

    }

    public void clientSubmitBtn() {
        String sql = "UPDATE client SET name = ?, username = ?, password = ?, address = ?, gender = ?, phoneNum = ?, status = ? WHERE clientId = ?";

        try (Connection connect = Database.connectDB()) {
            Alert alert;
            String clientIdInput = client_Id.getText().trim();
            String loggedInClientId = SessionManager.getCurrentClient().getClientId();


            if (!clientIdInput.equals(loggedInClientId)) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid Client Id!");
                alert.showAndWait();
                return;
            }

            // Check if any field is empty
            if (clientIdInput.isEmpty() || client_name.getText().isEmpty() || email_client.getText().isEmpty() || username_client.getText().isEmpty() || password_client.getText().isEmpty() || client_address.getText().isEmpty() || client_gender.getSelectionModel().getSelectedItem() == null || client_phoneNum.getText().isEmpty() || client_status.getSelectionModel().getSelectedItem() == null) {
                emptyFields();
                return;
            }

            String checkData = "SELECT clientId, username, password, email FROM client WHERE clientId = ?";

            try (PreparedStatement prepare = connect.prepareStatement(checkData)) {
                prepare.setString(1, clientIdInput);
                try (ResultSet result = prepare.executeQuery()) {
                    if (!result.next()) {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Client ID: " + clientIdInput + " is the wrong Client ID, try again!");
                        alert.showAndWait();
                    } else {
                        boolean usernameChanged = !result.getString("username").equals(username_client.getText());
                        boolean passwordChanged = !result.getString("password").equals(password_client.getText());
                        boolean emailChanged = !result.getString("email").equals(email_client.getText());

                        if (usernameChanged || passwordChanged || emailChanged) {
                            alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Confirmation Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Are you sure you want to update the username, email, and/or password?");
                            Optional<ButtonType> confirmationResult = alert.showAndWait();

                            if (!confirmationResult.isPresent() || !confirmationResult.get().equals(ButtonType.OK)) {
                                alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Information Message");
                                alert.setHeaderText(null);
                                alert.setContentText("Update canceled!");
                                alert.showAndWait();
                                return;
                            }
                        }


                        try (PreparedStatement updateStmt = connect.prepareStatement(sql)) {
                            updateStmt.setString(1, client_name.getText());
                            updateStmt.setString(2, username_client.getText());
                            updateStmt.setString(3, password_client.getText());
                            updateStmt.setString(4, client_address.getText());
                            updateStmt.setString(5, (String) client_gender.getSelectionModel().getSelectedItem());
                            updateStmt.setString(6, client_phoneNum.getText());
                            updateStmt.setString(7, (String) client_status.getSelectionModel().getSelectedItem());
                            updateStmt.setString(8, clientIdInput);

                            updateStmt.executeUpdate();

                            alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Information Message");
                            alert.setHeaderText(null);
                            alert.setContentText("Successfully Updated!");
                            alert.showAndWait();

                            clientClearBtn();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ObservableList<TrainerData> trainersDataList() {

        ObservableList<TrainerData> listData = FXCollections.observableArrayList();

        String sql = "SELECT * FROM trainers";

        connect = Database.connectDB();


        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            TrainerData td;

            while (result.next()) {
                td = new TrainerData(result.getInt("id"), result.getString("trainerId"), result.getString("name"), result.getString("email"), result.getString("username"), result.getString("password"), result.getString("address"), result.getString("gender"), result.getInt("phoneNum"), result.getString("status"));

                listData.add(td);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<TrainerData> trainersListData;

    public void trainersShowData() {

        trainersListData = trainersDataList();

        trainer_col_trainerID.setCellValueFactory(new PropertyValueFactory<>("trainerId"));
        trainer_col_trainerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        trainer_col_trainerGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        trainer_col_trainerPhoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        trainer_col_trainerStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        trainer_tableView.setItems(trainersListData);

    }


    private String gender[] = {"Male", "Female", "Other"};

    public void clientGenderList() {
        List<String> genderlist = new ArrayList<String>();

        for (String data : gender) {
            genderlist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(genderlist);
        client_gender.setItems(listData);
    }

    private String status[] = {"Active", "Inactive"};

    public void clientStatusList() {
        List<String> statuslist = new ArrayList<String>();

        for (String data : status) {
            statuslist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statuslist);
        client_status.setItems(listData);
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == clientProfile_dash) {

            clientProfile_Form.setVisible(true);
            clientTrainer_Form.setVisible(false);
            clientfitness_Form.setVisible(false);

        } else if (event.getSource() == clientTrainer_dash) {
            clientProfile_Form.setVisible(false);
            clientTrainer_Form.setVisible(true);
            clientfitness_Form.setVisible(false);

        } else if (event.getSource() == fitnessPlan_dash) {
            clientProfile_Form.setVisible(false);
            clientTrainer_Form.setVisible(false);
            clientfitness_Form.setVisible(true);
        }
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

            if (option.get().equals(ButtonType.OK)) {

                logout_btn.getScene().getWindow().hide();

                Parent root = FXMLLoader.load(getClass().getResource("main-login.fxml"));

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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trainersShowData();
        clientStatusList();
        clientGenderList();
        fitnessPlanShowData();
        displayUsername();

        clientAdd_trainer.setOnAction(this::clientAddTrainer);
    }
}
