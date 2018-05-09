package server;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import static java.lang.Thread.sleep;

import Protocol.Pcmd;
import Protocol.Pfight;
import Protocol.Pinfo;
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
                    out.println(Pinfo.SUCCESS);
                    out.println(JsonCreator.SendPlayer(player));
                    out.flush();
                }
                else{
                    out.println(Pinfo.FAILURE);
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
            case Pcmd.HELP:

                break;

            case Pcmd.EXIT:
                in.close();
                out.close();
                gameEngine.remove(player);
                clientSocket.close();
                LOG.log(Level.INFO, "models.Player " + player.getName() + " disconnected");
                break;

            case Pcmd.LIST_PALYERS:
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

            case Pcmd.VERSUS:
                versusCMD();
                break;


            case Pcmd.CHALLENGE:
                challengeCMD();
                break;

            case Pcmd.STORY:
                storyCMD();
                break;

            default:
                LOG.log(Level.INFO, Pinfo.UCOM);
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
                    case Pcmd.EXIT:
                        isInChallengeMode = false;
                        break;

                    case Pcmd.LIST_CHALLENGERS:
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

                    case Pcmd.FIGHT:
                        String opponentName = in.readLine();
                        Player opponent = gameEngine.getOpponent(opponentName);
                        if (opponent == null) {
                            LOG.log(Level.INFO, "models.Player not connected or not waiting for a challenge");
                            out.println(Pinfo.FAILURE);
                            out.flush();
                        } else {
                            LOG.log(Level.INFO, player.getName() + " Challenge " + opponent.getName());
                            out.println(Pinfo.SUCCESS);
                            out.flush();
                            PrintWriter signal = new PrintWriter(opponent.getClientSocket().getOutputStream());
                            signal.println(Pcmd.FIGHT);
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
                        LOG.log(Level.INFO, Pinfo.UCOM);
                        return;

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.log(Level.INFO, player.getName() + " EXIT Challenge mode");
    }

    public void storyCMD() throws InterruptedException, IOException {
        LOG.log(Level.INFO, "models.Player " + player.getName() + " enter in STORY Mode");

        gameEngine.startFightStory(player);

        LOG.log(Level.INFO, "models.Player " + player.getName() + " exit STORY Mode");
    }

    private void fight() throws InterruptedException, IOException{

        LOG.log(Level.INFO, player.getName() + " is in FIGHT Mode !");

        // Recuperation Nom et PV adversaire
        out.println(player.getFightMessageIn());
        out.println(player.getFightMessageIn());
        out.flush();

        // Debut combat
        while (player.getInFight()) {
            // Question ou Réponse ?
            String variable = player.getFightMessageIn();
            out.println(variable);
            out.flush();

            switch (variable) {
                case Pfight.ASK:
                    // Choix question du joueur
                    player.setFightMessageOut(in.readLine());

                    // Réponse juste ou fausse ?
                    out.println(player.getFightMessageIn());
                    out.flush();
                    break;
                case Pfight.ANSWER:
                    // Reception de la question
                    out.println(player.getFightMessageIn());
                    out.flush();
                    // Reception ds choix de réponse
                    out.println(player.getFightMessageIn());
                    out.flush();
                    // Envoi de la réponse choisie
                    player.setFightMessageOut(in.readLine());

                    // Réponse juste ou fausse ?
                    out.println(player.getFightMessageIn());
                    out.flush();
                    break;
            }

            // Payload état de l'adversaire
            out.println(player.getFightMessageIn());
            out.flush();
            // Payload état du joueur
            out.println(player.getFightMessageIn());
            out.flush();
            /*synchronized (this) {
                wait();
            }*/
            sleep(3000);
        }

            // Envoi de END
            out.println(player.getFightMessageIn());
            // Envoi de WIN ou LOST
            out.println(player.getFightMessageIn());
            out.flush();

    }
}