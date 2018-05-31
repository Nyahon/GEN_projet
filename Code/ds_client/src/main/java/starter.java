import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;

public class starter extends Application {

    public static Stage stage;

    public static void main(String[] argv) {
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s%6$s%n");

        launch();

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;

        Client client = new Client("localhost", 4500);

        Parent root = FXMLLoader.load(getClass().getResource("/main.fxml"));
        primaryStage.setTitle("Drunk and Smart");
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
