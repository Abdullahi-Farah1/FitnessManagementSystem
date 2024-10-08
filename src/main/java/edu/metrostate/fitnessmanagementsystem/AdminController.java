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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.XYChart;
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


public class AdminController implements Initializable {


    @FXML
    private TableColumn<ClientData, String> cientCol_Id;

    @FXML
    private TableColumn<ClientData, String> cientCol_address;

    @FXML
    private TableColumn<ClientData, String> cientCol_gender;

    @FXML
    private TableColumn<ClientData, String> cientCol_name;

    @FXML
    private TableColumn<ClientData, Integer> cientCol_phoneNum;

    @FXML
    private TableColumn<ClientData, String> cientCol_status;

    @FXML
    private TableColumn<ClientData, String> cientCol_username;

    @FXML
    private Button btn_clients;

    @FXML
    private Button btn_trainers;

    @FXML
    private Button client_addBtn;

    @FXML
    private AnchorPane client_form;

    @FXML
    private TableView<ClientData> client_tableView;

    @FXML
    private Button client_updateBtn;

    @FXML
    private Button close_btn;

    @FXML
    private Button dashboard_btn;

    @FXML
    private AnchorPane dashboard_form;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize_btn;

    @FXML
    private PasswordField password_trainer;

    @FXML
    private TextField trainer_Id;

    @FXML
    private Button trainer_addbtn;

    @FXML
    private TextArea trainer_address;

    @FXML
    private Button trainer_clearbtn;

    @FXML
    private TableColumn<TrainerData, String> trainer_col_trainerAddress;

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
    private TableColumn<TrainerData, String> trainer_col_trainerUsername;

    @FXML
    private TableColumn<TrainerData, String> trainer_col_trainerEmail;

    @FXML
    private Button trainer_deletebtn;

    @FXML
    private AnchorPane trainer_form;

    @FXML
    private ComboBox<String> trainer_gender;

    @FXML
    private TextField trainer_name;

    @FXML
    private TextField trainer_phoneNum;

    @FXML
    private TextField trainer_email;

    @FXML
    private ComboBox<String> trainer_status;

    @FXML
    private TableView<TrainerData> trainer_tableView;

    @FXML
    private Button trainer_updatebtn;

    @FXML
    private Label clientCount;

    @FXML
    private Label trainerCount;

    @FXML
    private Label totalActiveUsers;

    @FXML
    private TextField username_trainer;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;
    private Statement statement;

    @FXML
    private AreaChart<String, Number> totalUsersDashBoardChart;

    public void totalActiveUsersChart() {

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Total Active Users");


        String clientSql = "SELECT COUNT(id) AS count FROM client WHERE status='Active'";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(clientSql);
            result = prepare.executeQuery();
            if (result.next()) {
                series.getData().add(new XYChart.Data<>("Clients", result.getInt("count")));
            }


            String trainerSql = "SELECT COUNT(id) AS count FROM trainers WHERE status='Active'";
            prepare = connect.prepareStatement(trainerSql);
            result = prepare.executeQuery();
            if (result.next()) {
                series.getData().add(new XYChart.Data<>("Trainers", result.getInt("count")));
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


        totalUsersDashBoardChart.getData().clear();
        totalUsersDashBoardChart.getData().add(series);
    }


    public void dashboardClientCount() {
        String sql = "select count(id) from client where status='Active'";

        connect = Database.connectDB();

        int clientCounter = 0;

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                clientCounter = result.getInt("count(id)");
            }

            clientCount.setText(String.valueOf(clientCounter));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTrainerCount() {
        String sql = "select count(id) from trainers where status='Active'";

        connect = Database.connectDB();

        int trainerCounter = 0;

        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            if (result.next()) {
                trainerCounter = result.getInt("count(id)");
            }

            trainerCount.setText(String.valueOf(trainerCounter));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dashboardTotalActiveMembers() {
        String clientSql = "SELECT count(id) FROM client WHERE status='Active'";
        String trainerSql = "SELECT count(id) FROM trainers WHERE status='Active'";

        connect = Database.connectDB();

        int totalActiveMembers = 0;

        try {

            prepare = connect.prepareStatement(clientSql);
            result = prepare.executeQuery();

            if (result.next()) {
                totalActiveMembers += result.getInt("count(id)");
            }


            prepare = connect.prepareStatement(trainerSql);
            result = prepare.executeQuery();

            if (result.next()) {
                totalActiveMembers += result.getInt("count(id)");
            }


            totalActiveUsers.setText(String.valueOf(totalActiveMembers));

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
                cd = new ClientData(result.getInt("id"), result.getString("clientId"), result.getString("name"),
                        result.getString("username"),
                        result.getString("password"), result.getString("address"),
                        result.getString("gender"), result.getInt("phoneNum"),
                        result.getString("status"), result.getString("trainerId"));

                listData.add(cd);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;
    }

    private ObservableList<ClientData> clientListData;

    public void clientShowData() {

        clientListData = clientDataList();

        cientCol_Id.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        cientCol_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        cientCol_username.setCellValueFactory(new PropertyValueFactory<>("username"));
        cientCol_address.setCellValueFactory(new PropertyValueFactory<>("address"));
        cientCol_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        cientCol_phoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        cientCol_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        client_tableView.setItems(clientListData);

    }

    public void emptyFields() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Blank Fields");
        alert.setHeaderText(null);
        alert.setContentText("Please do not leave the fields blank.");
        alert.showAndWait();
    }

    public void clientDelete() {
        ClientData selectedClient = client_tableView.getSelectionModel().getSelectedItem();

        if (selectedClient != null) {
            String sql = "DELETE FROM client WHERE clientId = ?";

            connect = Database.connectDB();

            try {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete ClientID: " + selectedClient.getClientId() + " ?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, selectedClient.getClientId());
                    prepare.executeUpdate();

                    clientListData.remove(selectedClient);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    dashboardTrainerCount();
                    dashboardClientCount();
                    dashboardTotalActiveMembers();
                    totalActiveUsersChart();
                    clientShowData();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Deletion canceled!");
                    alert.showAndWait();
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (prepare != null) prepare.close();
                    if (connect != null) connect.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void trainersAddBtn() {
        String sql = "INSERT INTO trainers (trainerId, name, email, username, password, address, gender, phoneNum, status)"
                + "VALUES(?,?,?,?,?,?,?,?,?)";

        connect = Database.connectDB();

        try {
            Alert alert;


            if (trainer_Id.getText().isEmpty() || trainer_name.getText().isEmpty() || trainer_email.getText().isEmpty() ||
                    username_trainer.getText().isEmpty() || password_trainer.getText().isEmpty() ||
                    trainer_address.getText().isEmpty() || trainer_gender.getSelectionModel().getSelectedItem() == null ||
                    trainer_phoneNum.getText().isEmpty() || trainer_status.getSelectionModel().getSelectedItem() == null) {
                emptyFields();
            } else if (password_trainer.getText().length() < 8) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Password must be at least 8 characters long.");
                alert.showAndWait();
            } else {
                String checkData = "SELECT trainerId FROM trainers WHERE trainerId = ?";
                prepare = connect.prepareStatement(checkData);
                prepare.setString(1, trainer_Id.getText());
                result = prepare.executeQuery();

                if (result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Trainer ID: " + trainer_Id.getText() + " is already taken!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, trainer_Id.getText());
                    prepare.setString(2, trainer_name.getText());
                    prepare.setString(3, trainer_email.getText());
                    prepare.setString(4, username_trainer.getText());
                    prepare.setString(5, password_trainer.getText());
                    prepare.setString(6, trainer_address.getText());
                    prepare.setString(7, trainer_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(8, trainer_phoneNum.getText());
                    prepare.setString(9, trainer_status.getSelectionModel().getSelectedItem());

                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    trainersClearBtn();
                    dashboardTrainerCount();
                    dashboardClientCount();
                    dashboardTotalActiveMembers();
                    totalActiveUsersChart();
                    trainersShowData();
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
    }


    public void trainersClearBtn() {
        trainer_Id.setText("");
        trainer_name.setText("");
        trainer_email.setText("");
        username_trainer.setText("");
        password_trainer.setText("");
        trainer_address.setText("");
        trainer_gender.getSelectionModel().clearSelection();
        trainer_phoneNum.setText("");
        trainer_status.getSelectionModel().clearSelection();
    }

    public void trainersUpdateBtn() {
        String sql = "UPDATE trainers SET name = ?, email = ?, username = ?, password = ?, address = ?, gender = ?, phoneNum = ?, status = ? WHERE trainerId = ?";

        connect = Database.connectDB();

        try {
            Alert alert;

            // Check if any field is empty
            if (trainer_Id.getText().isEmpty() || trainer_name.getText().isEmpty() || trainer_email.getText().isEmpty() ||
                    username_trainer.getText().isEmpty() || password_trainer.getText().isEmpty() ||
                    trainer_address.getText().isEmpty() || trainer_gender.getSelectionModel().getSelectedItem() == null ||
                    trainer_phoneNum.getText().isEmpty() || trainer_status.getSelectionModel().getSelectedItem() == null) {
                emptyFields();
            } else if (password_trainer.getText().length() < 8) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Password must be at least 8 characters long.");
                alert.showAndWait();
            } else {

                String checkTrainerSql = "SELECT trainerId FROM trainers WHERE trainerId = ?";
                prepare = connect.prepareStatement(checkTrainerSql);
                prepare.setString(1, trainer_Id.getText());
                result = prepare.executeQuery();

                if (!result.next()) {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Trainer ID: " + trainer_Id.getText() + " does not exist.");
                    alert.showAndWait();
                } else {

                    alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Confirmation Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you sure you want to update Trainer ID: " + trainer_Id.getText() + " ?");
                    Optional<ButtonType> result = alert.showAndWait();

                    if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                        prepare = connect.prepareStatement(sql);
                        prepare.setString(1, trainer_name.getText());
                        prepare.setString(2, trainer_email.getText());
                        prepare.setString(3, username_trainer.getText());
                        prepare.setString(4, password_trainer.getText());
                        prepare.setString(5, trainer_address.getText());
                        prepare.setString(6, trainer_gender.getSelectionModel().getSelectedItem());
                        prepare.setString(7, trainer_phoneNum.getText());
                        prepare.setString(8, trainer_status.getSelectionModel().getSelectedItem());
                        prepare.setString(9, trainer_Id.getText());
                        prepare.executeUpdate();

                        alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Successfully Updated!");
                        alert.showAndWait();

                        dashboardTrainerCount();
                        dashboardClientCount();
                        dashboardTotalActiveMembers();
                        totalActiveUsersChart();
                        trainersShowData();
                        trainersClearBtn();
                    } else {
                        alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Information Message");
                        alert.setHeaderText(null);
                        alert.setContentText("Update canceled!");
                        alert.showAndWait();
                    }
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
    }


    public void trainerDeleteBtn() {
        String sql = "DELETE FROM trainers WHERE trainerId = ?";

        connect = Database.connectDB();

        try {
            Alert alert;

            if (trainer_Id.getText().isEmpty() || trainer_name.getText().isEmpty() || trainer_email.getText().isEmpty() || username_trainer.getText().isEmpty() ||
                    password_trainer.getText().isEmpty() || trainer_address.getText().isEmpty()
                    || trainer_gender.getSelectionModel().getSelectedItem() == null
                    || trainer_phoneNum.getText().isEmpty()
                    || trainer_status.getSelectionModel().getSelectedItem() == null) {
                emptyFields();
            } else {
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete TrainerID: " + trainer_Id.getText() + " ?");
                Optional<ButtonType> result = alert.showAndWait();

                if (result.isPresent() && result.get().equals(ButtonType.OK)) {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, trainer_Id.getText());
                    prepare.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    dashboardTrainerCount();
                    dashboardClientCount();
                    dashboardTotalActiveMembers();
                    totalActiveUsersChart();
                    trainersShowData();
                    trainersClearBtn();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Deletion canceled!");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (prepare != null) prepare.close();
                if (connect != null) connect.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                td = new TrainerData(result.getInt("id"), result.getString("trainerId"), result.getString("name"),
                        result.getString("email"), result.getString("username"),
                        result.getString("password"), result.getString("address"),
                        result.getString("gender"), result.getInt("phoneNum"),
                        result.getString("status"));

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
        trainer_col_trainerEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        trainer_col_trainerUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        trainer_col_trainerAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        trainer_col_trainerGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        trainer_col_trainerPhoneNum.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        trainer_col_trainerStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        trainer_tableView.setItems(trainersListData);
    }

    public void trainersSelect() {
        TrainerData td = trainer_tableView.getSelectionModel().getSelectedItem();
        int num = trainer_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) return;

        trainer_Id.setText(td.getTrainerId());
        trainer_name.setText(td.getName());
        trainer_email.setText(td.getEmail());
        username_trainer.setText(td.getUsername());
        password_trainer.setText(td.getPassword());
        trainer_address.setText(td.getAddress());
        trainer_phoneNum.setText(String.valueOf(td.getPhoneNum()));
    }

    private String[] gender = {"Male", "Female", "Other"};

    public void trainersGenderList() {
        ObservableList<String> listData = FXCollections.observableArrayList(gender);
        trainer_gender.setItems(listData);
    }

    private String[] status = {"Active", "Inactive"};

    public void trainerStatusList() {
        ObservableList<String> listData = FXCollections.observableArrayList(status);
        trainer_status.setItems(listData);
    }

    public void switchForm(ActionEvent event) {
        if (event.getSource() == dashboard_btn) {
            dashboard_form.setVisible(true);
            trainer_form.setVisible(false);
            client_form.setVisible(false);

        } else if (event.getSource() == btn_trainers) {
            trainer_gender.getSelectionModel().clearSelection();
            trainer_status.getSelectionModel().clearSelection();
            trainer_form.setVisible(true);
            client_form.setVisible(false);
            dashboard_form.setVisible(false);

            trainersGenderList();
            trainerStatusList();
            trainersShowData();

        } else if (event.getSource() == btn_clients) {
            trainer_form.setVisible(false);
            client_form.setVisible(true);
            dashboard_form.setVisible(false);
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

            if (option.isPresent() && option.get().equals(ButtonType.OK)) {
                logout_btn.getScene().getWindow().hide();

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        trainersGenderList();
        trainerStatusList();
        trainersShowData();
        clientShowData();
        dashboardTrainerCount();
        dashboardClientCount();
        dashboardTotalActiveMembers();
        totalActiveUsersChart();
    }
}
