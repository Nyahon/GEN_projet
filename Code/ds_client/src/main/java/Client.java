import models.Challenge;
import models.Player;
import models.WaitChallenge;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.*;

public class Client {

    private static final Logger LOG = Logger.getLogger(Client.class.getName());
    static private Socket socket;
    private int serverListenPort;
    private String serverAddress;
    private Scanner scanner = new Scanner(System.in);
    private boolean isConnected;
    private Player player;

    private BufferedReader input;
    private PrintWriter output;

    /***
     * Constructor for a simple client. It only take a server address.
     * @param serverAddress the address of the server.
     */
    public Client(String serverAddress, int serverListenPort) {
        this.serverAddress = serverAddress;
        this.serverListenPort = serverListenPort;
    }

    public void connect() throws IOException, InterruptedException {
        LOG.log(Level.INFO, "Connection with the server...");
        socket = new Socket(serverAddress, serverListenPort);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));


        // Enter Player ID (Inscription)
        System.out.println(input.readLine());
        String identifier = scanner.nextLine();
        player = new Player(identifier);
        player.setSocket(socket);
        output.println(identifier);
        output.flush();
        System.out.println(input.readLine());


        LOG.log(Level.INFO, "Connected !");
        isConnected = true;

        // Send Commands to the server
        while (isConnected) {
            System.out.println(input.readLine());
            sendCMD(scanner.nextLine().toUpperCase().trim());
        }


    }

    public void sendCMD(String cmd) throws IOException, InterruptedException {
        output.println(cmd);
        output.flush();
        switch (cmd) {
            case "EXIT" :
                input.close();
                output.close();
                socket.close();
                LOG.log(Level.INFO, "End of connection...");
                isConnected = false;
                break;

            case "LIST_PLAYERS":
                System.out.println(input.readLine());
                break;

            case "VERSUS":
                WaitChallenge mode = new WaitChallenge(player);
                mode.start();
                break;

            case "CHALLENGE":
                Challenge challenge = new Challenge(player);
                challenge.launch();
                break;

            default :

        }
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 4500);
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        try {
            client.connect();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        LOG.log(Level.INFO, "Disconnection...");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.log(Level.INFO, "Disconnected !");

    }





}
