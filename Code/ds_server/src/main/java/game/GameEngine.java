package game;

import com.fasterxml.jackson.databind.JsonNode;
import models.ConnectionDB;
import models.Connexions;
import models.Player;
import models.Question;
import models.db_models.db_Professeur;
import server.JsonCreator;
import server.ServerConnectionHandler;

import java.util.LinkedList;
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

        ConnectionDB.deleteProfessors();
        ConnectionDB.deleteProfessorQuestion();

        LinkedList<Question> questions = JsonCreator.readQuestionsAndProfFromFile();
        for (Question q : questions){
            if(ConnectionDB.questionExist(q.getId()) == false)
                ConnectionDB.addQuestion(q);
        }

        JsonCreator.createProfessorsFromFile();

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
        Fight fight = new Fight(player, opponent);
    }

    public void startFightStory(Player player, db_Professeur professeur){
        FightStory fight = new FightStory(player, professeur);
    }
}
