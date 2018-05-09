package models;

import Protocol.Pcmd;
import Protocol.Pinfo;

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
    private static final Logger LOG = Logger.getLogger(WaitChallenge.class.getName());



    public WaitChallenge(Player player) {
        this.player = player;
        try {
            in = new BufferedReader(new InputStreamReader(player.getSocket().getInputStream()));
            out = new PrintWriter(player.getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException {
        LOG.log(Level.INFO, "Enter in the VERSUS Mode");
        // Attend commande du serveur
        System.out.println(in.readLine());
        recieveCMD( in.readLine());

        LOG.log(Level.INFO, "Player " + player.getName() + " exit VERSUS Mode");

    }

    public void recieveCMD(String cmd) throws IOException {
        switch (cmd) {
            case Pcmd
                    .EXIT:
                break;
            case Pcmd.FIGHT:
                Fight fight = new Fight(player);
                fight.start();
                break;

            default :
                LOG.log(Level.INFO, Pinfo.UCOM);
                return;
        }
    }
}
