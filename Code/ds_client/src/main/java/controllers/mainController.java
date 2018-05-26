package controllers;

import Protocol.Pcmd;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import models.*;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainController {

    @FXML
    private Button createAccount;

    @FXML
    private Button connect;

    @FXML
    private Label errorLogin;

    private static BufferedReader input;
    private static PrintWriter output;

    private static Socket socket;
    private int serverListenPort;
    private String serverAddress;

    private static boolean isLoginOk = false;

    private boolean isConnected = false;

    private static Player player;

    private static final Logger LOG = Logger.getLogger(mainController.class.getName());

    @FXML
    protected void initialize() {
        try {
            socket = new Socket("localhost", 4500);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void connectAccount() {
        try {
            System.out.println(input.readLine());

            // Question from server : has an account , respond Y
            output.println("Y");
            output.flush();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/connect.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            Connect connection = fxmlLoader.<Connect>getController();
            connection.initialize();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createMyAccount() {

        try {

            System.out.println(input.readLine());

            // Question from server : has an account , respond N because he create a account
            output.println("N");
            output.flush();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            createAccount createAccount1 = fxmlLoader.<createAccount>getController();
            createAccount1.initialize(socket);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void startVersus() {
        checkLogin();

    }

    @FXML
    public void startStory() {
        try {
            checkLogin();

            player.setSocket(socket);
            System.out.println(player);

            // END OF CREATION PLAYER --------------------------------------------------------------------------------------

            // Send Commands to the server ---------------------------------------------------------------------------------
            LOG.log(Level.INFO, "Connected !");
            isConnected = true;

            if (isConnected) {
                System.out.println(input.readLine());
                output.println("STORY");
                output.flush();

                //Charge la page fight
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fight.fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(fxmlLoader.load()));
                Fight fight = fxmlLoader.<Fight>getController();
                fight.initialize(player);
                stage.show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkLogin() {
        if (isLoginOk) {
            errorLogin.setTextFill(Color.GREEN);
            errorLogin.setText("Vous etes connecté");
            createAccount.setCancelButton(true);
            connect.setCancelButton(true);
        } else {
            errorLogin.setTextFill(Color.RED);
            errorLogin.setText("Avant de commencer connectez vous ou créer un compte");
        }
    }

    public String hashPass(String password) {
        MessageDigest digest = null;
        String hex = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest("12345".getBytes(StandardCharsets.UTF_8));

            hex = DatatypeConverter.printHexBinary(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hex;
    }

    public static void setLoginOk(boolean loginOk) {
        isLoginOk = loginOk;
    }

    public void sendCMD(String cmd) throws IOException, InterruptedException {
        output.println(cmd);
        output.flush();
        switch (cmd) {
            case Pcmd.EXIT:
                input.close();
                output.close();
                socket.close();
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
            default:

        }
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket(Socket socket) {
        mainController.socket = socket;
    }

    public static BufferedReader getInput() {
        return input;
    }

    public static void setInput(BufferedReader input) {
        mainController.input = input;
    }

    public static PrintWriter getOutput() {
        return output;
    }

    public static void setOutput(PrintWriter output) {
        mainController.output = output;
    }

    public static void setPlayer(Player player) {
       mainController.player = player;
    }
}
