package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import models.Player;

import java.io.BufferedReader;
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

        }
        catch (Exception e){
            e.printStackTrace();
        }

        //TODO : Recuperer liste des adversaires possibles et  les ajouter au ChoiceBox
        list_Player.getItems().add("mon adversaire");
    }

    @FXML
    public void startFight(){
        if(list_Player.getValue() == null)
            return;

        String challenger = (String) list_Player.getValue();
        //TODO LANCER LE COMBAT AVEC LE CHALLENGER VU DESSUS.
    }

    @FXML
    public void returnHome(){

    }
}