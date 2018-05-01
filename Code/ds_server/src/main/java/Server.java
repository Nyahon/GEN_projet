import models.Connexions;
import models.Player;
import models.Versus;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    private int port;
    private Connexions clientsConnected = new Connexions();

    public Server(int port) {
        this.port = port;
    }

    public void start() {
        new Thread(new Boss()).start();
    }

    // Inner Class that receive the client demand and give it to the Worker (create a client socket and give it to the Worker)
    private class Boss implements Runnable {

        public void run() {

            ServerSocket serverSocket = null;

            try {
                serverSocket = new ServerSocket(port);

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(new Worker(clientSocket)).start();
                }

            }
            catch (IOException e) {

                System.out.println("ERROR");
                return;

            }
            finally {
                try {
                    serverSocket.close();
                } catch (IOException ex) {
                    System.out.println("ERROR");
                    return;
                }
            }


        }
    }


    // Inner Class that receive the client socket from the Boss and serve the client then close the connexion with him
    private class Worker implements Runnable {
        Socket clientSocket;
        PrintWriter out = null;
        BufferedReader in = null;
        Player player = null;

        public Worker(Socket clientSocket) {

            try {
                this.clientSocket = clientSocket;
                out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            }
            catch (IOException e) {
                System.out.println("ERROR");
                return;
            }

        }

        public void run() {

            try {
                // Create and add Player in Connexions
                out.println("SERVER: Entrez votre identifiant: ");
                out.flush();
                player = new Player(in.readLine());
                out.println("SERVER: Welcome to Drunk&Smart " + player.getName() + " !");
                out.flush();
                player.setClientSocket(clientSocket);
                clientsConnected.addPlayer(player);

                LOG.log(Level.INFO, "Player " + player.getName() + " registered !");

                // Receive and treat commands from the client
                while (clientsConnected.isPlayerConnected(player)) {
                    out.println("SERVER: Enter your command: ");
                    out.flush();
                    receiveCMD(in.readLine());
                }

            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return;
            }

        }

        public void receiveCMD (String cmd) throws IOException, InterruptedException {
            switch (cmd) {
                case "EXIT":
                    in.close();
                    out.close();
                    clientsConnected.removePlayer(player);
                    clientSocket.close();
                    LOG.log(Level.INFO, "Player " + player.getName() + " disconnected");
                    break;

                case "LIST_PLAYERS":
                    String playerList = "{";
                    List<Player> players = clientsConnected.getConnectedPlayers();
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

                case "VERSUS":
                    Versus mode = new Versus(player);
                    mode.start();
                    break;


                default :
                    String[] otherCMD = cmd.split(" +");
                    if(otherCMD.length > 0 && otherCMD[0].equalsIgnoreCase("VERSUS")) {
                        LOG.log(Level.INFO, player.getName() + " Challenge " + otherCMD[1] + " !");
                        Player p = clientsConnected.getPlayer(otherCMD[1]);
                        if (p == null) {
                            LOG.log(Level.INFO, "Player not connected");
                            break;
                        }

                        Versus versus = new Versus(player);
                        versus.challenge(p);
                    }
                    else {
                        LOG.log(Level.INFO, "UNKNOW COMMAND");
                        return;
                    }
            }
        }
    }

    // Main Programme that start the Server
    public static void main(String[] args) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        Server server = new Server(4500);
        server.start();
    }
}
