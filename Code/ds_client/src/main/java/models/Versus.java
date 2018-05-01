package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Versus {
    private Player player;              // joueur local
    private BufferedReader in = null;
    private PrintWriter out = null;
    private static final Logger LOG = Logger.getLogger(Versus.class.getName());


    public Versus(Player player) {
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
        recieveCMD(in.readLine());

        LOG.log(Level.INFO, "Player " + player.getName() + " exit VERSUS Mode");

    }

    public void recieveCMD(String cmd) throws IOException {
        switch (cmd) {
            case "QUIT":
                break;
            case "FIGHT":
                    Fight fight = new Fight(player);
                    fight.start();
                break;

            default:
                LOG.log(Level.INFO, "UNKNOW COMMAND");
                return;
        }
    }

    public void challenge(Player opponent) {

    }
}
