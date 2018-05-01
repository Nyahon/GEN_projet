package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WaitChallenge {
    private Player player;
    private BufferedReader in = null;
    private PrintWriter out = null;
    static Connexions challengers = new Connexions();
    private static final Logger LOG = Logger.getLogger(WaitChallenge.class.getName());


    public WaitChallenge(Player player) {
        this.player = player;
        try {
            in = new BufferedReader(new InputStreamReader(player.getClientSocket().getInputStream()));
            out = new PrintWriter(player.getClientSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void start() throws IOException, InterruptedException{
        LOG.log(Level.INFO, "Player " + player.getName() + " enter in VERSUS Mode");
        challengers.addPlayer(player);
        while (challengers.isPlayerConnected(player)) {
            out.println("VERSUS: Waiting for a Challenger...");
            out.flush();
            Thread.sleep(30000); // TODO: Trouver une solution pour l'attente qui dure après un combat si trop court
            if (!player.getInFight()) {
                out.println("QUIT");
                out.flush();
            }
            challengers.removePlayer(player);
        }

        LOG.log(Level.INFO, "Player " + player.getName() + " exit VERSUS Mode");

    }

}
