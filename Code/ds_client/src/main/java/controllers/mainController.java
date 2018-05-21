package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class mainController {

    @FXML
    private Button createAccount;

    @FXML
    private Button connect;

    @FXML
    private Label errorLogin;

    private BufferedReader input;
    private PrintWriter output;

    private Socket socket;
    private int serverListenPort;
    private String serverAddress;

    private boolean isLoginOk = false;

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
            connection.initialize(socket);
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
    public void startVersus(){
        checkLogin();

    }

    @FXML
    public void startStory(){
        checkLogin();
    }

    private void checkLogin(){
        if(isLoginOk){
            errorLogin.setTextFill(Color.GREEN);
            errorLogin.setText("Vous etes connecté");
            createAccount.setCancelButton(true);
            connect.setCancelButton(true);
        }
        else{
            errorLogin.setTextFill(Color.RED);
            errorLogin.setText("Avant de commencer connectez vous ou créer un compte");
        }
    }

    public String hashPass(String password){
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

    public void setLoginOk(boolean loginOk) {
        isLoginOk = loginOk;
    }
}
