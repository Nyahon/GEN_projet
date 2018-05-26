package controllers;

import Protocol.Pinfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.JsonCreator;

import java.io.*;
import java.net.Socket;

public class Connect extends mainController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Label errorUsername;

    @FXML
    private Button valider;

    @FXML
    private Button close;

    private PrintWriter output;
    private BufferedReader input;
    private Socket socket;

    @FXML
    protected void initialize() {
        this.socket = getSocket();
        try {
            input = getInput();
            output = getOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void validateData() {
        try {

            // ENVOIE LE USERNAME AU SERVEUR
            System.out.println(input.readLine());
            output.println(username.getText());
            output.flush();

            // ENVOIE LE PASSWORD AU SERVEUR
            System.out.println(input.readLine());
            output.println(hashPass(password.getText()));
            output.flush();

            String response = "";

            response = input.readLine();
            if (response.equals(Pinfo.FAILURE)) {
                errorUsername.setTextFill(Color.RED);
                errorUsername.setText("This user doesn't exist");
            } else {
                errorUsername.setTextFill(Color.GREEN);
                errorUsername.setText("login OK");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
                mainController main = fxmlLoader.<mainController>getController();

                setLoginOk(true);
                setSocket(socket);

                // Creation of the player object -------------------------------------------------------------------------------
                String playerPayloadJson = null;


                playerPayloadJson = input.readLine();

                setPlayer(JsonCreator.readPlayer(playerPayloadJson));
            }
            input.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    private void returnHome() {
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
    }
}
