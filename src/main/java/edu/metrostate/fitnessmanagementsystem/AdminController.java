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
    private Button btn_clients;

    @FXML
    private Button btn_trainers;

    @FXML
    private Button client_addBtn;

    @FXML
    private AnchorPane client_form;

    @FXML
    private TableView<?> client_tableView;

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
    private TableColumn<TrainerData,String> trainer_col_trainerAddress;

    @FXML
    private TableColumn<TrainerData,String> trainer_col_trainerGender;

    @FXML
    private TableColumn<TrainerData,String> trainer_col_trainerID;

    @FXML
    private TableColumn<TrainerData,String> trainer_col_trainerName;

    @FXML
    private TableColumn<TrainerData,Integer> trainer_col_trainerPhoneNum;

    @FXML
    private TableColumn<TrainerData,String> trainer_col_trainerStatus;

    @FXML
    private TableColumn<TrainerData,String> trainer_col_trainerUsername;

    @FXML
    private Button trainer_deletebtn;

    @FXML
    private AnchorPane trainer_form;

    @FXML
    private ComboBox<?> trainer_gender;

    @FXML
    private TextField trainer_name;

    @FXML
    private TextField trainer_phoneNum;

    @FXML
    private ComboBox<?> trainer_status;

    @FXML
    private TableView<TrainerData> trainer_tableView;

    @FXML
    private Button trainer_updatebtn;

    @FXML
    private TextField username_trainer;

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

    public void trainersAddBtn() {

        String sql = "INSERT INTO trainers (trainerId, name, Username, password, address, gender, phoneNum, status)"
                        + "VALUES(?,?,?,?,?,?,?,?)";

        connect = Database.connectDB();

        try {



            Alert alert;

            if(trainer_Id.getText().isEmpty() || trainer_name.getText().isEmpty() || username_trainer.getText().isEmpty() ||
                password_trainer.getText().isEmpty() || trainer_address.getText().isEmpty()
                    || trainer_gender.getSelectionModel().getSelectedItem() == null
                    || trainer_phoneNum.getText().isEmpty()
                    || trainer_status.getSelectionModel().getSelectedItem() == null) {
                emptyFields();
            } else {

                String checkData = "SELECT trainerId FROM trainers WHERE trainerId = '"
                        + trainer_Id.getText() + "'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if(result.next()) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Trainer ID: " + trainer_Id.getText() + "Is already taken!");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1,trainer_Id.getText());
                    prepare.setString(2,trainer_name.getText());
                    prepare.setString(3,username_trainer.getText());
                    prepare.setString(4,password_trainer.getText());
                    prepare.setString(5,trainer_address.getText());
                    prepare.setString(6,(String) trainer_gender.getSelectionModel().getSelectedItem());
                    prepare.setString(7,trainer_phoneNum.getText());
                    prepare.setString(8,(String) trainer_status.getSelectionModel().getSelectedItem());

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added!");
                    alert.showAndWait();

                    prepare.executeUpdate();

                    trainersShowData();
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void trainersClearBtn() {
        trainer_Id.setText("");
        trainer_name.setText("");
        username_trainer.setText("");
        password_trainer.setText("");
        trainer_address.setText("");
        trainer_gender.getSelectionModel().clearSelection();
        trainer_phoneNum.setText("");
        trainer_status.getSelectionModel().clearSelection();

    }

   /*public void trainserDeleteBtn {

        String sql = "DELETE FROM trainers WHERE trainer_Id = '"
                + trainer_Id.getText() + "'";
    }*/

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

        if((num -1) < -1) return;

        trainer_Id.setText(td.getTrainerId());
        trainer_name.setText(td.getTrainerId());
        username_trainer.setText(td.getTrainerId());
        password_trainer.setText(td.getTrainerId());
        trainer_address.setText(td.getTrainerId());
        trainer_phoneNum.setText(td.getTrainerId());

    }


    private String gender[] = {"Male", "Female", "Other"};

    public void trainersGenderList() {
        List<String> genderlist = new ArrayList<String>();

        for(String data: gender) {
            genderlist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(genderlist);
        trainer_gender.setItems(listData);
    }

    private String status[] = {"Active", "Inactive"};

    public void trainerStatusList() {
        List<String> statuslist = new ArrayList<String>();

        for(String data: status) {
            statuslist.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(statuslist);
        trainer_status.setItems(listData);
    }

    public void switchForm (ActionEvent event) {
        if (event.getSource() == dashboard_btn) {

            dashboard_form.setVisible(true);
            trainer_form.setVisible(false);
            client_form.setVisible(false);

        } else if (event.getSource() == btn_trainers) {
            trainer_form.setVisible(true);
            client_form.setVisible(false);
            dashboard_form.setVisible(false);

            trainersGenderList();
            trainerStatusList();

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

            if(option.get().equals(ButtonType.OK)) {

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
    }
}

