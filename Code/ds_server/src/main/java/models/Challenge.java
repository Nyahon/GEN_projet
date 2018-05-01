package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Challenge {

    private Player player;
    private BufferedReader in = null;
    private PrintWriter out = null;
    private boolean isInChallengeMode = true;
    private static final Logger LOG = Logger.getLogger(WaitChallenge.class.getName());
    private static final Connexions challengersWaiting = WaitChallenge.challengers;

    public Challenge(Player player) {
        this.player = player;

        try {
            in = new BufferedReader(new InputStreamReader(player.getClientSocket().getInputStream()));
            out = new PrintWriter(player.getClientSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void launch() throws IOException, InterruptedException {
        LOG.log(Level.INFO, player.getName() + " enter in CHALLENGE Mode");
        while (isInChallengeMode) {
            out.println("CHALLENGE: Enter your command :");
            out.flush();
            receiveCMD(in.readLine());
        }

        LOG.log(Level.INFO, player.getName() + " EXIT Challenge mode");

    }

    public void receiveCMD (String cmd) throws IOException, InterruptedException {

        switch (cmd) {
            case "EXIT":
                isInChallengeMode = false;
                break;

            case "LIST_PLAYERS":
                String playerList = "{";
                List<Player> players = challengersWaiting.getConnectedPlayers();
                int numberOfPlayers = players.size();
                for (int i= 0; i < numberOfPlayers; ++i) {
                    playerList += players.remove(0).getName();
                    if (i != numberOfPlayers - 1) {
                        playerList += ", ";
                    }
                }
                playerList += "}";

                out.println(playerList);
                out.flush();
                break;

            case "FIGHT":
                String opponent = in.readLine();
                Player p = challengersWaiting.getPlayer(opponent);
                if (p == null) {
                    LOG.log(Level.INFO, "Player not connected or not waiting for a challenge");
                    out.println("FAIL");
                    out.flush();
                }
                else {
                    LOG.log(Level.INFO, player.getName() + " Challenge " + p.getName());
                    out.println("SUCCESS");
                    out.flush();
                    PrintWriter signal = new PrintWriter(p.getClientSocket().getOutputStream());
                    signal.println("FIGHT");
                    signal.flush();

                    challengersWaiting.removePlayer(p);
                    Fight fight = new Fight(player, p);
                    fight.startFight();

                    isInChallengeMode = false;


                }
                break;


            default :
                LOG.log(Level.INFO, "UNKNOW COMMAND");
                return;

        }


    }

}

