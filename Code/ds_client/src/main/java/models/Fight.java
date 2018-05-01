package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Fight {
    private Player player1;


    private BufferedReader inPlayer1;
    private PrintWriter outPlayer1;
    private static final Logger LOG = Logger.getLogger(WaitChallenge.class.getName());




    public Fight(Player player1) {
        this.player1 = player1;

        try {
            inPlayer1 = new BufferedReader(new InputStreamReader(player1.getSocket().getInputStream()));
            outPlayer1 = new PrintWriter(player1.getSocket().getOutputStream());
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

    public void startFight() throws IOException {
        LOG.log(Level.INFO, "YOU ARE IN FIGHT !");
        System.out.println(inPlayer1.readLine());
    }
}
