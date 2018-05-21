import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class starter extends Application {

    public static void main(String[] argv) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        launch();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
        primaryStage.setTitle("Drunk and Smart");
        primaryStage.setScene(new Scene(root));

        Client client = new Client("localhost", 4500);

        primaryStage.setOnCloseRequest(e -> {
            Platform.exit();
            client.quitSystem();
        });
        primaryStage.show();

        /*
        try {
            client.connect();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        */

    }
}
