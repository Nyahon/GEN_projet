package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Fight {
    private Player player1;
    private Player player2;

    private BufferedReader inPlayer1;
    private PrintWriter outPlayer1;

    private BufferedReader inPlayer2;
    private PrintWriter outPlayer2;



    public Fight(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        try {
            inPlayer1 = new BufferedReader(new InputStreamReader(player1.getSocket().getInputStream()));
            inPlayer2 = new BufferedReader(new InputStreamReader(player2.getSocket().getInputStream()));
            outPlayer1 = new PrintWriter(player1.getSocket().getOutputStream());
            outPlayer2 = new PrintWriter(player2.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void nextAction() {

    }

    public void getState() {

    }

    private int getPlayer1HP() {
        return 0;
    }

    private int getPlayer2HP() {
        return 0;
    }

    public void startFight() {
        outPlayer1.println(player2.getName() + " is challenging you !");
        outPlayer1.flush();
        outPlayer2.println( player1.getName() + " is challenging you !");
        outPlayer2.flush();
    }
}
