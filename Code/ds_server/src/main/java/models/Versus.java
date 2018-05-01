package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Versus {
    private Player player;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private static Connexions challengers = new Connexions();
    private static final Logger LOG = Logger.getLogger(Versus.class.getName());


    public Versus (Player player) {
        this.player = player;
        try {
            in = new BufferedReader(new InputStreamReader(player.getClientSocket().getInputStream()));
            out = new PrintWriter(player.getClientSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, InterruptedException{
        LOG.log(Level.INFO, "Player " + player.getName() + " enter in VERSUS Mode");
        challengers.addPlayer(player);
        while (challengers.isPlayerConnected(player)) {
            out.println("VERSUS: Waiting for a Challenger...");
            out.flush();
            Thread.sleep(10000);
            out.println("QUIT");
            challengers.removePlayer(player);
        }

        LOG.log(Level.INFO, "Player " + player.getName() + " exit VERSUS Mode");

    }

    public void recieveCMD(String cmd) throws IOException {
        switch (cmd) {
            case "QUIT":
                challengers.removePlayer(player);
                break;


            default :
                LOG.log(Level.INFO,"UNKNOW COMMAND");
                return;
        }
    }

    public void challenge(Player opponent) {
        if(!challengers.isPlayerConnected(opponent)){
            LOG.log(Level.INFO, "Player is not waiting for a challenge");
            return;
        }

        Fight fight = new Fight(player, opponent);
        fight.startFight();

    }
}
