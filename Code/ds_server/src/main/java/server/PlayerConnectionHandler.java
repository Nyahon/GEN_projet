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
                    out.println("SUCCESS");
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

            case "STORY":
                storyCMD();
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
            LOG.log(Level.INFO,"ASK OR ANSWER : " + variable);
            out.println(variable);
            out.flush();

            String tmp = "";
            switch (variable) {
                case "ASK":
                    // Choix question du joueur
                    tmp = in.readLine();
                    LOG.log(Level.INFO,"CHOIX QUESTION DU JOUEUR : " + tmp);
                    player.setFightMessageOut(tmp);

                    break;
                case "ANSWER":
                    // Reception de la question
                    tmp = player.getFightMessageIn();
                    LOG.log(Level.INFO,"RECEPTION QUESTION : " + tmp);
                    out.println(tmp);
                    out.flush();
                    // Reception ds choix de réponse
                    tmp = player.getFightMessageIn();
                    LOG.log(Level.INFO,"RECEPTION CHOIX REPONSES : " + tmp);
                    out.println(tmp);
                    out.flush();
                    // Envoi de la réponse choisie
                    tmp = in.readLine();
                    LOG.log(Level.INFO,"ENVOIE REPONSE CHOISI : " + tmp);
                    player.setFightMessageOut(tmp);

                    // Réponse juste ou fausse ?
                    tmp = player.getFightMessageIn();
                    LOG.log(Level.INFO,"REPONSE JUSTE OU FAUX : " + tmp);
                    out.println(tmp);
                    out.flush();
                    break;
            }

            // Payload état de l'adversaire
            tmp = player.getFightMessageIn();
            LOG.log(Level.INFO,"PAYLOAD ETAT ADVERSAIRE : "+tmp);
            out.println(tmp);
            out.flush();
            // Payload état du joueur
            tmp = player.getFightMessageIn();
            LOG.log(Level.INFO,"PAYLOAD ETAT JOUEUR : "+tmp);
            out.println(tmp);
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