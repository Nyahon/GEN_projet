package controllers;

import Protocol.Pcmd;
import Protocol.Pinfo;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import models.JsonCreator;

import java.io.*;
import java.net.Socket;

public class createAccount extends mainController {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private Label errorUsername;

    @FXML
    private RadioButton cartesien;

    @FXML
    private RadioButton hedoniste;

    @FXML
    private RadioButton cynique;

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
            output.println(Pcmd.CREATE_ACCOUNT);

            // ENVOIE LE USERNAME AU SERVEUR
            output.println(username.getText());
            output.flush();

            // ENVOIE LE PASSWORD AU SERVEUR
            output.println(hashPass(password.getText()));
            output.flush();

            String choixClasse = "1";

            if (hedoniste.isSelected()) {
                choixClasse = "1";

            }
            if (cynique.isSelected()) {
                choixClasse = "2";

            }
            if (cartesien.isSelected()) {
                choixClasse = "3";
            }

            // ENVOIE LE CHOIX DE PERSONNAGE AU SERVEUR
            output.println(choixClasse);
            output.flush();

            String response = "";

            response = input.readLine();
            if (response.equals(Pinfo.FAILURE)) {
                errorUsername.setTextFill(Color.RED);
                errorUsername.setText("This user already exist");
            } else {
                errorUsername.setTextFill(Color.GREEN);
                errorUsername.setText("create account ok, login OK");

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
                mainController main = fxmlLoader.<mainController>getController();

                setLoginOk(true);
                setSocket(socket);

                // Creation of the player object -------------------------------------------------------------------------------
                String playerPayloadJson = null;


                playerPayloadJson = input.readLine();

                setPlayer(JsonCreator.readPlayer(playerPayloadJson));

                fxmlLoader = new FXMLLoader(getClass().getResource("/hub.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                Hub hub = fxmlLoader.<Hub>getController();
                hub.initialize();
                stage.show();
            }

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
