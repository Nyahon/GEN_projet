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
import models.PlayerClass;
import models.db_models.db_Professeur;

/* selon moi c'est inversé, à voir

 commHandler -> Fight : setFightMessageOut - getFightMessageOut
 commHandler <- Fight : getFightMessageIn  - setFichetMessageIn
 */


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
            final String QUESTION_IDENTIFIANT = "SERVER: Entrez votre identifiant: ";
            final String QUESTION_PASSWORD = "SERVER; Entrez votre mot de passe: ";
            final String QUESTION_CLASSE = "veuillez choisir parmis un de ces classes via leur numéro: ";
            final String HEDONISTE_DESCRIPTION = "1 : " + PlayerClass.Hedoniste.name() + " : " + PlayerClass.Hedoniste.getDescription();
            final String CYNIQUE_DESCRIPTION = "2 : " + PlayerClass.Cynique.name() + " : " + PlayerClass.Cynique.getDescription();
            final String CARTESIEN_DESCRIPTION = "3 : " + PlayerClass.Cartesien.name() + " : " + PlayerClass.Cartesien.getDescription();

            connectionMenu();

            // le moteur de jeu enregistre le player.
            player.setClientSocket(clientSocket);
            player.setPlayerConnectionHandler(this);
            gameEngine.register(player);

            LOG.log(Level.INFO, "models.Player " + player.getName() + " registered !");

            // Receive and treat commands from the client
            while (gameEngine.isConnected(player)) {
                receiveCMD(in.readLine());
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return;
        }

    }

    public void connectionMenu() throws IOException {
        boolean success = false;
        String command;
        String username;
        String password;
        while (!success) {

            switch (command = in.readLine()) {
                case Pcmd.CONNECT:
                    username = in.readLine();
                    password = in.readLine();
                    //CONTROLER PASSWORD
                    player = ConnectionDB.getJoueurByName(username);

                    if (player != null && password.equals(ConnectionDB.getPasswordByJoueurId(player.getId()))) {

                        player.setQuestions(ConnectionDB.getQuestionByPlayer(player.getId()));

                        out.println(Pinfo.SUCCESS);
                        out.flush();

                        out.println(JsonCreator.SendPlayer(player));
                        out.flush();

                        success = true;

                    } else {

                        out.println(Pinfo.FAILURE);
                        out.flush();

                        success = false;

                    }

                    break;
                case Pcmd.CREATE_ACCOUNT:

                    username = in.readLine();
                    password = in.readLine();
                    String classChoice = in.readLine();

                    PlayerClass pc = PlayerClass.Hedoniste;
                    switch (Integer.parseInt(classChoice)) {
                        case 1:
                            pc = PlayerClass.Hedoniste;
                            break;
                        case 2:
                            pc = PlayerClass.Cynique;
                            break;
                        case 3:
                            pc = PlayerClass.Cartesien;
                            break;
                    }

                    // joueur existe déjà
                    if (ConnectionDB.getJoueurByName(username) != null) {
                        out.println(Pinfo.FAILURE);
                        out.flush();

                        success = false;
                    } else {
                        player = new Player(username, 1, Player.INITIAL_PV, Player.INITIAL_LEVEL, Player.INITIAL_XP, pc, Player.INITIAL_IMAGE);
                        ConnectionDB.insertJoueur(player, password);
                        player.setId(ConnectionDB.getJoueurByName(player.getName()).getId());
                        player.initItemsPlayer();
                        player.initQuestionPlayer();

                        out.println(Pinfo.SUCCESS);

                        out.println(JsonCreator.SendPlayer(player));
                        out.flush();

                        success = true;
                    }
                    break;
            }

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

            case Pcmd.LIST_PLAYERS:
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

        switch (player.getLevel()) {
            case 0:
                db_Professeur Miguel = ConnectionDB.getProfesseurById(3);
                gameEngine.startFightStory(player, Miguel);
                fight();
                out.println("CONTINUE");
                out.flush();
            case 1:
                db_Professeur Liechti = ConnectionDB.getProfesseurById(2);
                gameEngine.startFightStory(player,Liechti);
                fight();
                out.println("CONTINUE");
                out.flush();

            case 2:
                db_Professeur Rentsch = ConnectionDB.getProfesseurById(1);
                gameEngine.startFightStory(player, Rentsch);
                fight();
                out.println("END");
                out.flush();
        }
        LOG.log(Level.INFO, "models.Player " + player.getName() + " exit STORY Mode");
    }

    private void fight() throws InterruptedException, IOException {

        LOG.log(Level.INFO, player.getName() + " is in FIGHT Mode !");

        // Recuperation Nom et PV adversaire (payload json)
        //out.println(player.getFightMessageIn());
        out.println(player.getFightMessageIn());
        out.flush();

        // Debut combat
        while (player.getInFight()) {

            // Mode question ou Réponse ?
            String variable = player.getFightMessageIn();
            out.println(variable);
            out.flush();

            switch (variable) {
                case Pfight.ASK:
                    // Choix question du joueur(client)
                    player.setFightMessageOut(in.readLine());

                    // Réponse juste ou fausse ?
                    out.println(player.getFightMessageIn());
                    out.flush();
                    break;
                case Pfight.ANSWER:
                    // Reception de la question, envoie au client
                    out.println(player.getFightMessageIn());
                    out.flush();

                    // Reception du choix de réponse, envoie au client
                    out.println(player.getFightMessageIn());
                    out.flush();

                    // Reception des objets du player envoie au client.
                    out.println(player.getFightMessageIn());
                    out.flush();

                    //reçois si utilise réponse ou objet
                    boolean hasAnswer = false;
                    while (!hasAnswer) {

                        String action = in.readLine();
                        player.setFightMessageOut(action);
                        switch (action) {
                            case Pfight.USE_ITEM:

                                // récupère l'item choisi par le client.
                                String item = in.readLine();

                                // transfert du choix d'item au fight
                                player.setFightMessageOut(item);

                                // envoie des choix de réponse possible
                                out.println(player.getFightMessageIn());
                                out.flush();

                                // Payload état du joueur
                                out.println(player.getFightMessageIn());
                                out.flush();
                                break;

                            case Pfight.ANSWER:

                                // réception de la réponse du joueur.
                                player.setFightMessageOut(in.readLine());

                                // Réponse juste ou fausse ?
                                out.println(player.getFightMessageIn());
                                out.flush();

                                hasAnswer = true;

                                break;
                        }
                    }
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