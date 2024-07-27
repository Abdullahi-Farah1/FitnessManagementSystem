package edu.metrostate.fitnessmanagementsystem;

import com.sun.tools.javac.Main;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;

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
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Button changtoadmin;

    @FXML
    private Button client_loginBtn;

    @FXML
    private PasswordField client_password;

    @FXML
    private TextField client_username;

    @FXML
    private AnchorPane clientlogin_form;

    @FXML
    private Button close;

    @FXML
    private FontAwesomeIconView close_Icon;

    @FXML
    private Label edit_label;

    @FXML
    private AnchorPane main_form;

    @FXML
    private AnchorPane signup_form;

    @FXML
    private TextField su_email;

    @FXML
    private Button su_signupBtn;

    @FXML
    private TextField su_username;

    @FXML
    private AnchorPane sub_form;

    @FXML
    private Button sub_loginBtn;

    @FXML
    private PasswordField su_password;

    @FXML
    private Button sub_signupBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private ResultSet result;

    public void login() {
        String sql = "SELECT * FROM client WHERE username = ? AND password = ?";
        connect = Database.connectDB();

        try {
            prepare = connect.prepareStatement(sql);
            prepare.setString(1, client_username.getText());
            prepare.setString(2, client_password.getText());
            result = prepare.executeQuery();

            Alert alert;

            if (client_username.getText().isEmpty() || client_password.getText().isEmpty()) {
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

                    // Store client ID in session
                    UserSession.getInstance().setClientId(result.getString("clientId"));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
                    Parent root = loader.load();

                    Stage stage = (Stage) client_loginBtn.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
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

    public void signup() {
        String sql = "INSERT INTO client (email, username, password) VALUES(?,?,?)";

        connect = Database.connectDB();

        try {
            Alert alert;

            if(su_email.getText().isEmpty() || su_username.getText().isEmpty() || su_password.getText().isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Please do not leave the fields blank.");
                alert.showAndWait();
            } else {
                if(su_password.getText().length() < 8) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Password length must be at least 8");
                    alert.showAndWait();
                } else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1, su_email.getText());
                    prepare.setString(2, su_username.getText());
                    prepare.setString(3, su_password.getText());

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Important Information");
                    alert.setHeaderText(null);
                    alert.setContentText("New Account Created!");
                    alert.showAndWait();

                    prepare.executeUpdate();

                    su_email.setText("");
                    su_username.setText("");
                    su_password.setText("");

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void signupSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(sub_form);
        slider1.setToX(350);
        slider1.setDuration(Duration.seconds(.5));
        slider1.play();
        slider1.setOnFinished((ActionEvent event) -> {
            edit_label.setText("Login Account");

            sub_signupBtn.setVisible(false);
            sub_loginBtn.setVisible(true);

            close_Icon.setFill(Color.valueOf("#fff"));
        });
    }

    public void loginSlider() {
        TranslateTransition slider1 = new TranslateTransition();
        slider1.setNode(sub_form);
        slider1.setToX(0);
        slider1.setDuration(Duration.seconds(.5));
        slider1.play();
        slider1.setOnFinished((ActionEvent event) -> {

            edit_label.setText("Create An Account");

            sub_signupBtn.setVisible(true);
            sub_loginBtn.setVisible(false);

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

            changtoadmin.getScene().getWindow().hide();

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}