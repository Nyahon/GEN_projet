import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnectionHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    private int port;

    private GameEngine gameEngine;

    private Thread thread;

    public ServerConnectionHandler(int port, GameEngine gameEngine){

        this.gameEngine = gameEngine;

        this.port = port;
        thread = new Thread(this);
        thread.start();
    }
    public void run() {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new PlayerConnectionHandler(clientSocket, gameEngine)).start();
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