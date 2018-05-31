package controllers;

import Protocol.Pcmd;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class mainController {

    protected class SceneString{


        private int scene = 0;



        private List<String> listScene = new ArrayList<>(
                Arrays.asList("./main.fxml", "./connect.fxml", "./createAccount.fxml","./hub.xml","./fight.fxml"));
        Map<Integer, Integer>  mapAncestorScenes = new HashMap<Integer, Integer>();

        public SceneString(){
            //define ancestors structures for each scenes
            mapAncestorScenes.put(0,0);
            mapAncestorScenes.put(1,0);
            mapAncestorScenes.put(2,0);
            mapAncestorScenes.put(3,0);
            mapAncestorScenes.put(4,3);


        }
        public void runAncestor(){
            switch(scene){
                case 0:
            }
        }
        public String getScene(int id) {
            return listScene.get(id);
        }

        public int getScene() {
            return scene;
        }

        public void setCurrentScene(String fileFXML) {
            this.scene = listScene.indexOf(fileFXML);
        }

        private String getAncestor(){
            //TODO Sanitarization
            return listScene.get( mapAncestorScenes.get( scene ) );
        }


    }

    protected SceneString SS = new SceneString();

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
    public FXMLLoader goToScene(Event ev, String fileFXML){
        FXMLLoader fxmlLoader = null;
        try {
            Node node=(Node) ev.getSource();
            Stage stage=(Stage) node.getScene().getWindow();

            fxmlLoader = new FXMLLoader(getClass().getResource( fileFXML ));
            SS.setCurrentScene( fileFXML );
            //   Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            Connect connection = fxmlLoader.getController();
            connection.initialize();
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return fxmlLoader;
        }

    }

    @FXML
    public void goBackOneScene(Event ev){
        try {
            Node node=(Node) ev.getSource();
            Stage stage=(Stage) node.getScene().getWindow();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource( SS.getScene(1) ));
            //   Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            Connect connection = fxmlLoader.getController();
            connection.initialize();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    public void connectAccount(Event ev) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource( "/connect.fxml" ));

            Node node=(Node) ev.getSource();
            Stage stage=(Stage) node.getScene().getWindow();
            //   Stage stage = new Stage();
      //      SS.setCurrentScene("./connect.fxml");

            stage.setScene(new Scene(fxmlLoader.load()));

            Connect connection = fxmlLoader.<Connect>getController();
            connection.initialize();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createMyAccount(Event ev) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/createAccount.fxml"));
            Node node=(Node) ev.getSource();
            Stage stage=(Stage) node.getScene().getWindow();
        //    Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            createAccount create = fxmlLoader.<createAccount>getController();
            create.initialize();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void returnHome(Event ev) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Node node=(Node) ev.getSource();
            Stage stage=(Stage) node.getScene().getWindow();
            //    Stage stage = new Stage();
            stage.setScene(new Scene(fxmlLoader.load()));
            createAccount create = fxmlLoader.getController();
            create.initialize();
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String hashPass(String password) {
        MessageDigest digest = null;
        String hex = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));

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

    @FXML
    private void returnBack(Event ev) {

        SS.runAncestor();
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

    public static Player getPlayer() {
        return player;
    }
}
