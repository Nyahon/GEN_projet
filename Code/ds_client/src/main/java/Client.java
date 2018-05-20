import Protocol.Pcmd;
import Protocol.Pinfo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.*;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    private String response;

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
        socket = new Socket("localhost", 4500);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));


        // Enter Player ID (LOG IN) -----------------------------------------------------------------------------------
        boolean isLoginOk = false;
        String identifier = "";
        String password = "";
        while (!isLoginOk) {
            //ASK IF HE WANT TO CREATE A ACCOUNT
            System.out.println(input.readLine());
            String hasAnAccount = scanner.nextLine();
            output.println(hasAnAccount);
            output.flush();

            if (hasAnAccount.toUpperCase().equals("Y")) {
                //demande nom
                System.out.println(input.readLine());
                identifier = scanner.nextLine();
                output.println(identifier);
                output.flush();

                //demande password
                System.out.println(input.readLine());
                password = scanner.nextLine();
                output.println(hashPass(password));
                output.flush();

                response = input.readLine();
                if (response.equals(Pinfo.FAILURE)) {
                    System.out.println("This user doesn't exist");
                }
                else {
                    isLoginOk = true;
                }
            }
            else{
                //demande nom
                System.out.println(input.readLine());
                identifier = scanner.nextLine();
                output.println(identifier);
                output.flush();

                //demande password
                System.out.println(input.readLine());
                password = scanner.nextLine();
                output.println(hashPass(password));
                output.flush();

                //Demande Classe
                System.out.println(input.readLine());
                System.out.println(input.readLine());
                System.out.println(input.readLine());
                System.out.println(input.readLine());

                String choixClasse = scanner.nextLine();
                output.println(choixClasse);
                output.flush();

                response = input.readLine();
                if (response.equals(Pinfo.FAILURE)) {
                    System.out.println("This user name exist already");
                }
                else {
                    isLoginOk = true;
                }
            }


        }
        // END OF LOG IN -----------------------------------------------------------------------------------------------


        // Creation of the player object -------------------------------------------------------------------------------
        String playerPayloadJson = input.readLine();
        player = JsonCreator.readPlayer(playerPayloadJson);
        player.setSocket(socket);

        System.out.println(player);

        System.out.println(input.readLine());
        // END OF CREATION ---------------------------------------------------------------------------------------------


        // Send Commands to the server ---------------------------------------------------------------------------------
        LOG.log(Level.INFO, "Connected !");
        isConnected = true;
        while (isConnected) {
            System.out.println(input.readLine());
            sendCMD(scanner.nextLine().toUpperCase().trim());
        }
        // END OF THE CLIENT PROGRAMME ---------------------------------------------------------------------------------

    }

    

    public void sendCMD(String cmd) throws IOException, InterruptedException {
        output.println(cmd);
        output.flush();
        switch (cmd) {
            case Pcmd.EXIT :
                input.close();
                output.close();
                socket.close();
                LOG.log(Level.INFO, "End of connection...");
                isConnected = false;
                break;

            case Pcmd.LIST_PALYERS:
                System.out.println(input.readLine());
                break;

            case Pcmd.VERSUS:
                WaitChallenge mode = new WaitChallenge(player);
                mode.start();
                break;

            case Pcmd.CHALLENGE:
                Challenge challenge = new Challenge(player);
                challenge.launch();
                break;
            case Pcmd.STORY:
                System.out.println("You enter Story mode !");
                Story story = new Story(player);
                story.start();
            default :

        }
    }

    public static void quitSystem() {
        LOG.info("System closing");
        // TODO: ADD HERE ALL THINGS WHO NEED TO BE QUIT CORRECTLY

        LOG.log(Level.INFO, "Disconnection...");
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOG.log(Level.INFO, "Disconnected !");

        System.exit(0);
    }

    private String hashPass(String password){
        MessageDigest digest = null;
        String hex ="";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest("12345".getBytes(StandardCharsets.UTF_8));

            hex = DatatypeConverter.printHexBinary(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hex;
    }





}
