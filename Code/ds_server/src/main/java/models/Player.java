package models;

import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import server.PlayerConnectionHandler;

public class Player {

    private int id;
    private int annee;
    private int nbPV;
    private int nbXP;
    private String name;
    private int level;
    private Socket clientSocket = null;
    private PlayerConnectionHandler playerConnectionHandler;
    private boolean inFight = false;
    private LinkedList<Question> questions;

    private BlockingQueue<String> fightMessageIn = new ArrayBlockingQueue<>(500, true);
    private BlockingQueue<String> fightMessageOut = new ArrayBlockingQueue<>(500, true);

    public Player(int id, String name, int annee, int pv, int niveau, int xp){
        this.name = name; this.nbPV = pv; this.nbXP = xp; this.level = niveau; this.annee = annee; questions = new LinkedList<>();this.id = id;}

    // TODO: Implement real question asking
    public boolean askQuestion(){
        // TODO: give to the player a list of question to choice.
        // TODO: send to the server the choice question, then the question will be send to the other player.
        // TODO: Then we receive from the server is the response of the adversery is true or false.
        boolean adversaryResponse = false;

        return adversaryResponse;
    }

    // TODO: Implement real question respond
    public void respondQuestion(){
        // TODO: give to the player a list of 4 responses to ask the question of the adversary.
        // TODO: send to the server his response.
        // TODO: Then the server respond if the response is true or false;
        boolean respondIsRight = false;
        if(respondIsRight){
            nbXP += 10;
            //TODO: test if we are in server vs server or vs player
            //TODO: if we are versus server we win the question asking.
        }
        else{
            nbPV -= 20;
        }
    }

    public boolean IsLive(){
        return nbPV>0;
    }

    // ==========================================================
    // GETTERS SETTERS
    // ==========================================================
    public int getNbPV() {
        return nbPV;
    }

    public void setNbPV(int nbPV) {
        this.nbPV = nbPV;
    }

    public void loosePV(int nbPV){ this.nbPV -= nbPV;}

    public int getNbXP() {
        return nbXP;
    }

    public void setNbXP(int nbXP) {
        this.nbXP = nbXP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setQuestions(LinkedList<Question> questions){
        this.questions = questions;
    }

    public void addQuestions(Question question){
        this.questions.add(question);
    }

    public int getId() {
        return id;
    }

    public LinkedList<Question> getQuestions(){
        return this.questions;

    }

    public String getFightMessageIn() throws InterruptedException {
        return fightMessageIn.take();
    }

    public void setFightMessageIn(String message) throws InterruptedException {
        fightMessageIn.put(message);
    }

    public String getFightMessageOut() throws InterruptedException {
        return fightMessageOut.take();
    }

    public void setFightMessageOut(String message) throws InterruptedException {
        fightMessageOut.put(message);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setClientSocket(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void setPlayerConnectionHandler(PlayerConnectionHandler handler){
        this.playerConnectionHandler = handler;
    }


    public boolean getInFight() {
        return inFight;
    }

    public void setInFight(boolean bool) {
        inFight = bool;
    }


    public int getAnnee() {
        return annee;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", annee=" + annee +
                ", nbPV=" + nbPV +
                ", nbXP=" + nbXP +
                ", name='" + name + '\'' +
                ", level=" + level +
                '}';
    }
}
