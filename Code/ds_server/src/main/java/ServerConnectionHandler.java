import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class NewConnectionHandler implements Runnable {

    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    private int port;

    public NewConnectionHandler(int port){
        this.port = port;
    }
    public void run() {

        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new PlayerConnectionHandler(clientSocket)).start();
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