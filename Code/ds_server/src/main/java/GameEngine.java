import java.util.List;

public class GameEngine implements Runnable{

    private ServerConnectionHandler serverConnectionHander;
    private Thread thread;

    private Connexions connections;
    private Connexions challengers;

    public GameEngine() {

        thread = new Thread(this);
        thread.start();

        serverConnectionHander = new ServerConnectionHandler(4500, this);

        connections = new Connexions();
        challengers = new Connexions();
    }

    public void run(){
        while(true){

        }
    }

    public void register(Player player){
        connections.addPlayer(player);
    }

    public Boolean isConnected(Player player){
        return connections.isConnected(player);
    }

    public void remove(Player player){
        connections.remove(player);
    }

    public List<Player> getConnectedPlayers(){
        return connections.getConnectedPlayers();
    }

    public void addChallenger(Player player){
        challengers.addPlayer(player);
    }

    public void removeChallenger(Player player){
        challengers.remove(player);
    }

    public List<Player> getChallengers(){
        return challengers.getConnectedPlayers();
    }

    public Player getOpponent(String playerName){
        return challengers.getPlayer(playerName);
    }

    public boolean isChallenger(Player player){
        return challengers.isConnected(player);
    }

    public void startFight(Player player, Player opponent){
        opponent.notifyWaitingConnection();
    }
}
