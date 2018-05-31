package controllers;

import Protocol.Pcmd;
import Protocol.Pinfo;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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

    ToggleGroup chooseClass;
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

        chooseClass = new ToggleGroup();

        cartesien.setToggleGroup(chooseClass);
        hedoniste.setToggleGroup(chooseClass);
        cynique.setToggleGroup(chooseClass);

        this.socket = getSocket();
        try {
            input = getInput();
            output = getOutput();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void validateData(Event ev) {

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
                Node node=(Node) ev.getSource();
                Stage stage=(Stage) node.getScene().getWindow();
                //Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                Hub hub = fxmlLoader.<Hub>getController();
                hub.initialize();
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
