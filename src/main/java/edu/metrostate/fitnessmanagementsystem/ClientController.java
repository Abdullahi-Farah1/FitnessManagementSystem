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

        String sql = "SELECT * FROM fitnessplan";

        connect = Database.connectDB();


        try {

            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            FitnessPlanData fd;

            while (result.next()) {
                fd = new FitnessPlanData(result.getInt("id"),result.getString("clientId"),result.getString("plan"));

                listData.add(fd);
            }


        }catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<FitnessPlanData> fitnessPlanListData;

    public void fitnessPlanShowData() {

        fitnessPlanListData = fitnessPlanDataList();

        fitnessPlan_col.setCellValueFactory(new PropertyValueFactory<>("plan"));

        fitnessPlan_table.setItems(fitnessPlanListData);

    }

    public void clientSubmitBtn() {

        String sql = "UPDATE client SET clientId = '"
                + client_Id.getText() + "', name = '"
                + client_name.getText() + "', username = '"
                + username_client.getText() + "', password = '"
                + password_client.getText() + "', address = '"
                + client_address.getText() + "', gender = '"
                + client_gender.getSelectionModel().getSelectedItem() + "', phoneNum = '"
                + client_phoneNum.getText() + "', status = '"
                + client_status.getSelectionModel().getSelectedItem() + "' WHERE username = '"
                + username_client.getText() + "'";

        connect = Database.connectDB();

        try {
            Alert alert;

            if (client_Id.getText().isEmpty() || client_name.getText().isEmpty() || username_client.getText().isEmpty() ||
                    password_client.getText().isEmpty() || client_address.getText().isEmpty()
                    || client_gender.getSelectionModel().getSelectedItem() == null
                    || client_phoneNum.getText().isEmpty()
                    || client_status.getSelectionModel().getSelectedItem() == null) {
                emptyFields();
            } else {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to Submit Trainer ID: " + client_Id.getText() + " ? ");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.get().equals(ButtonType.OK)) {

                    prepare = connect.prepareStatement(sql);
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Submitted!");
                    alert.showAndWait();

                } else {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Canceled Submission!");
                    alert.showAndWait();

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
                td = new TrainerData(result.getInt("id"), result.getString("trainerId"),result.getString("name"),
                        result.getString("username"),
                        result.getString("password"), result.getString("address"),
                        result.getString("gender"),result.getInt("phoneNum"),
                        result.getString("status"));

                listData.add(td);
            }


        }catch (Exception e) {
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

        for(String data: gender) {
            genderlist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(genderlist);
        client_gender.setItems(listData);
    }

    private String status[] = {"Active", "Inactive"};

    public void clientStatusList() {
        List<String> statuslist = new ArrayList<String>();

        for(String data: status) {
            statuslist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statuslist);
        client_status.setItems(listData);
    }

    public void switchForm (ActionEvent event) {
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

            if(option.get().equals(ButtonType.OK)) {

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
    }
}
