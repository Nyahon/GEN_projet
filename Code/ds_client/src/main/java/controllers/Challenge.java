package controllers;

import Protocol.Pcmd;
import Protocol.Pinfo;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import models.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Challenge extends mainController {

    @FXML
    private ChoiceBox list_Player;

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

            //TODO : Recuperer liste des adversaires possibles et  les ajouter au ChoiceBox
            output.println(Pcmd.LIST_CHALLENGERS);
            output.flush();
            String response = input.readLine();
            String[] challengers = response.substring(1, response.length() - 1).split(",");
            for (String challenger : challengers)
                list_Player.getItems().add(challenger);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startFight(Event ev) {
        try {

            if (list_Player.getValue() == null)
                return;

            String challenger = (String) list_Player.getValue();
            System.out.println("MY FUCKING CHALLENGER IS : " + challenger);
            output.println(Pcmd.FIGHT);
            output.println(challenger);
            output.flush();


            if (input.readLine().equals(Pinfo.SUCCESS)) {
                player.setSocket(socket);
                System.out.println(player);

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fight.fxml"));
                Node node = (Node) ev.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.setScene(new Scene(fxmlLoader.load()));
                Fight fight = fxmlLoader.<Fight>getController();
                fight.initialize(player);
                System.out.println("Start interface graphique from Challenge");
                stage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void returnHome() {

    }
}