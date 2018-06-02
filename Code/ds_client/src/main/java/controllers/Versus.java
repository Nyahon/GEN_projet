package controllers;

import Protocol.Pcmd;
import Protocol.Pinfo;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;
import models.Player;

import java.awt.*;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Versus extends mainController {

    @FXML
    private Button stop;

    @FXML
    private Label time;

    @FXML
    private ProgressBar timer;


    private PrintWriter output;
    private BufferedReader input;
    private Socket socket;
    private Player player;

    protected void initialize() {
        this.socket = getSocket();
        try {
            input = getInput();
            output = getOutput();
            player = getPlayer();

            //Reçois le nombre de secondes à attendre !!!!
            int nbrSecondeWait =Integer.parseInt(input.readLine());


            //Fait défiler la progress bar selon les secondes.
            IntegerProperty seconds = new SimpleIntegerProperty();
            timer.progressProperty().bind(seconds.divide(60.0));
            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.ZERO, new KeyValue(seconds, 0)),
                    new KeyFrame(Duration.seconds(nbrSecondeWait), e-> {
                        // do anything you need here on completion...
                        System.out.println("Minute over");
                        time.setText(Integer.toString(nbrSecondeWait));
                    }, new KeyValue(seconds, 60))
            );
            timeline.setCycleCount(Animation.INDEFINITE);
            timeline.play();

            //Recoit SUCESS ou FAILURE
            String goOnFight = input.readLine();
            if (goOnFight.equals(Pinfo.FAILURE))
                returnHome();
            if(goOnFight.equals(Pinfo.SUCCESS)){
                // TODO: ouvre fightS
                }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    @FXML
    private void returnHome() {
        /*Stage stage = (Stage) this.getScene().getWindow();
        stage.close();*/
    }
}
