import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Properties;

public class starter extends Application {

    public static Stage stage;
    private int port;
    private String ip;

    public static void main(String[] argv) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");
        launch();

    }

    private void getConfig() {
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            ip = prop.getProperty("host");
            port = Integer.parseInt(prop.getProperty("port"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        port = 4500;
        ip = "localhost";
        try {
            getConfig();
        }
        catch (Exception e){

        }
        Client client = new Client(ip, port);

        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("BuzzOn");
        Scene rootScene = new Scene(root);
        rootScene.getStylesheets().add(starter.class.getResource("/bootstrap3.css").toExternalForm());
        primaryStage.setScene(rootScene);


        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            client.quitSystem();
        });
        primaryStage.show();
    }
}
