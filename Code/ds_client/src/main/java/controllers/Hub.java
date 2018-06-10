package controllers;

import Protocol.Pcmd;
import Protocol.Pfight;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.JsonCreator;
import models.Player;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;

public class Hub extends mainController{

    private PrintWriter output;
    private BufferedReader input;
    private Socket socket;
    private Player player;

    @FXML
    protected void initialize() {
        this.socket = getSocket();
        try {
            input = getInput();
            output = getOutput();
            player = getPlayer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startVersus() {

        try {
            player.setSocket(socket);
            System.out.println(player);

            output.println(Pcmd.VERSUS);
            output.flush();

            String resp = input.readLine();
            resp = input.readLine();

            //Charge la page fight
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fight.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            Fight fight = fxmlLoader.<Fight>getController();
            fight.initialize(player);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void startChallenge() {
        try {
            player.setSocket(socket);
            System.out.println(player);

            output.println(Pcmd.CHALLENGE);
            output.flush();

            //Charge la page fight
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/challenge.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            Challenge challenge = fxmlLoader.<Challenge>getController();
            challenge.initialize();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void startStory() {
        try {
            player.setSocket(socket);
            System.out.println(player);

                output.println(Pcmd.STORY);
                output.flush();

                    //Charge la page fight
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fight.fxml"));
                    Stage stage = new Stage();
                    stage.setScene(new Scene(fxmlLoader.load()));
                    Fight fight = fxmlLoader.<Fight>getController();
                    fight.initialize(player);
                    stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
