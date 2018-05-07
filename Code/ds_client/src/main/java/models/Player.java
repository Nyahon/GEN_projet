package models;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Player {

    private int nbPV;
    private int nbXP;
    private String name;
    private int level;
    private int id;
    private int annee;
    private Socket socket = null;

    public Player(int id, String name, int annee, int pv, int niveau, int xp){
        this.name = name; this.nbPV = pv; this.nbXP = xp; this.level = niveau; this.annee = annee; this.id = id;}


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

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }


    @Override
    public String toString() {
        return "Player{" +
                "nbPV=" + nbPV +
                ", nbXP=" + nbXP +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", id=" + id +
                ", annee=" + annee +
                '}';
    }

}
