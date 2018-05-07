package server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.Thread.sleep;
import game.GameEngine;
import models.ConnectionDB;
import models.Player;


// Class that receive the client socket from the ConnectionHandler and serve the client then close the connexion with him
public class PlayerConnectionHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(PlayerConnectionHandler.class.getName());

    private GameEngine gameEngine;

    private Socket clientSocket;
    private PrintWriter out = null;
    private BufferedReader in = null;

    private Player player = null;

    public PlayerConnectionHandler(Socket clientSocket, GameEngine gameEngine) {

        this.clientSocket = clientSocket;
        this.gameEngine = gameEngine;

        try {
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("ERROR");
            return;
        }
    }

    public void run() {

        try {
            // Create and add models.Player in models.Connexions to the game engine
            boolean loginIsOk = false;
            while (!loginIsOk) {
                out.println("SERVER: Entrez votre identifiant: ");
                out.flush();
                String identifiant = in.readLine();
                player = ConnectionDB.getJoueurByName(identifiant);
                if(player != null) {
                    player.setQuestions(ConnectionDB.getQuestionByPlayer(player.getId()));
                    loginIsOk = true;
                    out.println("SUCESS");
                    out.println(JsonCreator.SendPlayer(player));
                    out.flush();
                }
                else{
                    out.println("FAILURE");
                    out.flush();
                }
            }

            out.println("SERVER: Welcome to Drunk&Smart " + player.getName() + " !");
            out.flush();
            player.setClientSocket(clientSocket);
            player.setPlayerConnectionHandler(this);
            gameEngine.register(player);

            LOG.log(Level.INFO, "models.Player " + player.getName() + " registered !");

            // Receive and treat commands from the client
            while (gameEngine.isConnected(player)) {

                out.println("SERVER: Enter your command: ");
                out.flush();
                receiveCMD(in.readLine());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

    }

    public void receiveCMD(String cmd) throws IOException, InterruptedException {
        switch (cmd) {
            case "EXIT":
                in.close();
                out.close();
                gameEngine.remove(player);
                clientSocket.close();
                LOG.log(Level.INFO, "models.Player " + player.getName() + " disconnected");
                break;

            case "LIST_PLAYERS":
                String playerList = "{";
                List<Player> players = gameEngine.getConnectedPlayers();
                int numberOfPlayers = players.size();
                for (int i = 0; i < numberOfPlayers; ++i) {
                    playerList += players.remove(0).getName();
                    if (i != numberOfPlayers - 1) {
                        playerList += ", ";
                    }
                }
                playerList += "}";

                out.println(playerList);
                out.flush();
                break;

            case "VERSUS":
                versusCMD();
                break;


            case "CHALLENGE":
                challengeCMD();
                break;

            default:
                LOG.log(Level.INFO, "UNKNOW COMMAND");
                return;
        }
    }

    public void versusCMD() throws InterruptedException, IOException {
        LOG.log(Level.INFO, "models.Player " + player.getName() + " enter in VERSUS Mode");
        gameEngine.addChallenger(player);

        while (gameEngine.isChallenger(player)) {
            out.println("VERSUS: Waiting for a Challenger...");
            out.flush();

            synchronized (player) {
                player.wait();
            }


            fight();

            gameEngine.removeChallenger(player);
        }

        LOG.log(Level.INFO, "models.Player " + player.getName() + " exit VERSUS Mode");
    }

    public void challengeCMD() throws InterruptedException {
        LOG.log(Level.INFO, player.getName() + " enter in CHALLENGE Mode");
        boolean isInChallengeMode = true;
        try {
            while (isInChallengeMode) {
                out.println("CHALLENGE: Enter your command :");
                out.flush();

                switch (in.readLine().toUpperCase()) {
                    case "EXIT":
                        isInChallengeMode = false;
                        break;

                    case "LIST_PLAYERS":
                        String playerList = "{";
                        List<Player> challengersWaiting = gameEngine.getChallengers();
                        int numberOfPlayers = challengersWaiting.size();
                        for (int i = 0; i < numberOfPlayers; ++i) {
                            playerList += challengersWaiting.remove(0).getName();
                            if (i != numberOfPlayers - 1) {
                                playerList += ", ";
                            }
                        }
                        playerList += "}";

                        out.println(playerList);
                        out.flush();
                        break;

                    case "FIGHT":
                        String opponentName = in.readLine();
                        Player opponent = gameEngine.getOpponent(opponentName);
                        if (opponent == null) {
                            LOG.log(Level.INFO, "models.Player not connected or not waiting for a challenge");
                            out.println("FAIL");
                            out.flush();
                        } else {
                            LOG.log(Level.INFO, player.getName() + " Challenge " + opponent.getName());
                            out.println("SUCCESS");
                            out.flush();
                            PrintWriter signal = new PrintWriter(opponent.getClientSocket().getOutputStream());
                            signal.println("FIGHT");
                            signal.flush();
                            synchronized (opponent) {
                                opponent.notify();
                            }


                            gameEngine.startFight(player, opponent);

                            fight();

                            isInChallengeMode = false;

                        }
                        break;


                    default:
                        LOG.log(Level.INFO, "UNKNOW COMMAND");
                        return;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.log(Level.INFO, player.getName() + " EXIT Challenge mode");
    }

    private void fight() throws InterruptedException, IOException{

        LOG.log(Level.INFO, player.getName() + " is in FIGHT Mode !");

        out.println(player.getFightMessageIn());
        out.println(player.getFightMessageIn());
        out.flush();

        while (player.getInFight()) {
            String variable = player.getFightMessageIn();
            out.println(variable);
            out.flush();

            switch (variable) {
                case "ASK":
                    player.setFightMessageOut(in.readLine());
                    break;
                case "ANSWER":
                    out.println(player.getFightMessageIn());
                    out.flush();
                    player.setFightMessageOut(in.readLine());
                    break;
            }

            out.println(player.getFightMessageIn());
            out.println(player.getFightMessageIn());
            out.flush();
            /*synchronized (this) {
                wait();
            }*/
            sleep(3000);
        }

            out.println(player.getFightMessageIn());
            out.println(player.getFightMessageIn());
            out.flush();

    }
}