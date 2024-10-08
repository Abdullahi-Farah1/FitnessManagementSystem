package edu.metrostate.fitnessmanagementsystem;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.ResourceBundle;

public class AdTrLoginController implements Initializable {

    @FXML
    private AnchorPane adminMain_form;

    @FXML
    private AnchorPane admin_form;

    @FXML
    private Button admin_loginBtn;

    @FXML
    private PasswordField admin_password;

    @FXML
    private Button admin_switchBtn;

    @FXML
    private TextField admin_username;

    @FXML
    private Button changetoclient;

    @FXML
    private Button close;

    @FXML
    private FontAwesomeIconView close_Icon;

    @FXML
    private Label edit1_label;

    @FXML
    private Label editable3;

    @FXML
    private Label editable4;

    @FXML
    private FontAwesomeIconView shield;

    @FXML
    private AnchorPane trainer_form;

    @FXML
    private Button trainer_loginBtn;

    @FXML
    private PasswordField trainer_password;

    @FXML
    private Button trainer_switch;

    @FXML
    private TextField trainer_username;

    @FXML
    private FontAwesomeIconView usericcon;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void adminlogin() {
        String sql = "SELECT * FROM admin WHERE username = ? AND password = ?";
        connect = Database.connectDB();

        try {
            if (connect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Cannot connect to the database. Please check your connection.");
                alert.showAndWait();
                return;
            }

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, admin_username.getText());
            prepare.setString(2, admin_password.getText());
            result = prepare.executeQuery();

            Alert alert;

            if (admin_username.getText().isEmpty() || admin_password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please do not leave the fields blank.");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful");
                    alert.showAndWait();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
                    Parent root = loader.load();

                    Stage newStage = new Stage();
                    newStage.initStyle(StageStyle.TRANSPARENT);
                    root.setOnMousePressed((MouseEvent event) -> {
                        x = event.getSceneX();
                        y = event.getSceneY();
                    });

                    root.setOnMouseDragged((MouseEvent event) -> {
                        newStage.setX(event.getScreenX() - x);
                        newStage.setY(event.getScreenY() - y);
                        newStage.setOpacity(.8);
                    });

                    root.setOnMouseReleased((MouseEvent event) -> {
                        newStage.setOpacity(1);
                    });

                    newStage.setScene(new Scene(root));
                    newStage.show();

                    Stage currentStage = (Stage) admin_loginBtn.getScene().getWindow();
                    currentStage.hide();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username or Password");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void trainerLogin() {
        String sql = "SELECT * FROM trainers WHERE username = ? AND password = ?";
        connect = Database.connectDB();

        try {
            if (connect == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Database Error");
                alert.setHeaderText(null);
                alert.setContentText("Cannot connect to the database. Please check your connection.");
                alert.showAndWait();
                return;
            }

            prepare = connect.prepareStatement(sql);
            prepare.setString(1, trainer_username.getText());
            prepare.setString(2, trainer_password.getText());
            result = prepare.executeQuery();

            Alert alert;

            if (trainer_username.getText().isEmpty() || trainer_password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please do not leave the fields blank.");
                alert.showAndWait();
            } else {
                if (result.next()) {
                    TrainerData loggedInTrainer = new TrainerData(result.getInt("id"), result.getString("trainerId"), result.getString("name"), result.getString("email"), result.getString("username"), result.getString("password"), result.getString("address"), result.getString("gender"), result.getInt("phoneNum"), result.getString("status"));

                    SessionManager.setCurrentTrainer(loggedInTrainer);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText(null);
                    alert.setContentText("Login Successful");
                    alert.showAndWait();

                    SessionManager.username = trainer_username.getText();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("trainer.fxml"));
                    Parent root = loader.load();

                    Stage stage = new Stage();
                    stage.initStyle(StageStyle.TRANSPARENT);

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

                    stage.setScene(new Scene(root));
                    stage.show();

                    Stage currentStage = (Stage) trainer_loginBtn.getScene().getWindow();
                    currentStage.hide();
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Wrong Username or Password");
                    alert.showAndWait();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void adminSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(adminMain_form);
        slider1.setToX(350);
        slider1.setDuration(Duration.seconds(.5));
        slider1.play();
        slider1.setOnFinished((ActionEvent event) -> {
            edit1_label.setText("Trainer Login");
            editable3.setVisible(true);
            editable4.setVisible(false);
            usericcon.setVisible(true);
            shield.setVisible(false);

            admin_switchBtn.setVisible(false);
            trainer_switch.setVisible(true);

            close_Icon.setFill(Color.valueOf("#fff"));
        });
    }

    public void trainerSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(adminMain_form);
        slider1.setToX(0);
        slider1.setDuration(Duration.seconds(.5));
        slider1.play();
        slider1.setOnFinished((ActionEvent event) -> {

            edit1_label.setText("Admin Login");
            edit1_label.setText("Admin Login");
            editable3.setVisible(false);
            editable4.setVisible(true);
            usericcon.setVisible(false);
            shield.setVisible(true);

            admin_switchBtn.setVisible(true);
            trainer_switch.setVisible(false);

            close_Icon.setFill(Color.valueOf("#000"));

        });
    }

    public void close() {
        javafx.application.Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private double x = 0;
    private double y = 0;

    public void switchScene() {

        try {

            changetoclient.getScene().getWindow().hide();

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
