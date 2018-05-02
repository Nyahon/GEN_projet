import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Connexions {

    private List<Player> connectedPlayers = new ArrayList<>();

    public void addPlayer(Player player) {
        if (player != null) {
            connectedPlayers.add(player);
        }
    }

    public void addPlayers(Player... players) {
        for (Player p : players) {
            addPlayer(p);
        }
    }

    public void addPlayers(Collection<Player> players) {
        connectedPlayers.addAll(players);
    }

    public boolean remove(Player player) {
        return connectedPlayers.remove((Player) player);
    }

    public void remove(Player... players) {
        for (Player p : players) {
            remove(p);
        }
    }

    public Player getPlayer(String name) {
        List<Player> players = getConnectedPlayers();
        for (Player p : players) {
            if (p.getName().equals(name)) {
                return p;
            }
        }

        return null;
    }

    public void clear() {
        connectedPlayers.clear();
    }

    public boolean isConnected(Player player) {
        return connectedPlayers.contains(player);
    }

    public List<Player> getConnectedPlayers() {
        return new ArrayList<>(connectedPlayers);
    }
}
